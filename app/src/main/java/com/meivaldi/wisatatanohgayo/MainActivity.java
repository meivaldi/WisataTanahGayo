package com.meivaldi.wisatatanohgayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.meivaldi.wisatatanohgayo.fragment.DiscoverFragment;
import com.meivaldi.wisatatanohgayo.fragment.HomeFragment;
import com.meivaldi.wisatatanohgayo.fragment.LoginFragment;
import com.meivaldi.wisatatanohgayo.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

}
