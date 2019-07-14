package com.meivaldi.wisatatanohgayo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meivaldi.wisatatanohgayo.fragment.DiscoverFragment;
import com.meivaldi.wisatatanohgayo.fragment.HomeFragment;
import com.meivaldi.wisatatanohgayo.fragment.LoginFragment;
import com.meivaldi.wisatatanohgayo.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Memroses...");

        mAuth = FirebaseAuth.getInstance();

        int frag = getIntent().getIntExtra("fragment", -1);

        if (frag != -1) {
            switch (frag) {
                case 0:
                    loadFragment(new HomeFragment());

                    FirebaseUser usr = mAuth.getCurrentUser();
                    Toast.makeText(this, "Selamat Datang " + usr.getDisplayName(), Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    loadFragment(new DiscoverFragment());
                    break;
                case 2:
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null)
                        loadFragment(new ProfileFragment());
                    else
                        loadFragment(new LoginFragment());

                    bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

                    break;
            }
        } else {
            loadFragment(new HomeFragment());
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_explore:
                    loadFragment(new DiscoverFragment());
                    return true;
                case R.id.navigation_profile:
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (user != null)
                        loadFragment(new ProfileFragment());
                    else
                        loadFragment(new LoginFragment());

                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            dialog.show();
            final Uri selectedFile = data.getData();

            StorageReference reference = FirebaseStorage.getInstance().getReference("user_photo")
                    .child(mAuth.getCurrentUser().getEmail()).child("user.png");

            UploadTask task = reference.putFile(selectedFile);

            task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(selectedFile).build();
                        mAuth.getCurrentUser().updateProfile(request)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Berhasil mengganti foto profil",
                                                    Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("fragment", 2);
                                            startActivity(intent);

                                            finish();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Gagal mengganti foto profil", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal mengupload file", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
                }
            });
        }
    }
}
