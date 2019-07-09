package com.meivaldi.wisatatanohgayo.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.meivaldi.wisatatanohgayo.MainActivity;
import com.meivaldi.wisatatanohgayo.R;
import com.meivaldi.wisatatanohgayo.User;

public class RegisterFragment extends Fragment {

    private Button login, register;
    private FirebaseAuth mAuth;
    private EditText nameET, emailET, passwordET;

    private ProgressDialog dialog;
    private DatabaseReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/IndieFlower.ttf");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Memroses...");

        register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(name, email, password);
                }
            }
        });

        login = view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });

        nameET.setTypeface(typeface);
        emailET.setTypeface(typeface);
        passwordET.setTypeface(typeface);
        login.setTypeface(typeface);
        register.setTypeface(typeface);

        return view;
    }

    private void registerUser(final String name, final String email, final String password) {
        dialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        /*User userObj = new User(name, email, password);
                                        db.child("users").child(name).setValue(userObj);*/

                                        Toast.makeText(getContext(), "Berhasil mendaftarkan akun", Toast.LENGTH_SHORT).show();
                                        nameET.setText("");
                                        emailET.setText("");
                                        passwordET.setText("");
                                        dialog.dismiss();

                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.putExtra("fragment", 2);
                                        getContext().startActivity(intent);

                                        getActivity().finish();
                                    }
                                }
                            });
                        } else {
                            //startAnimation();

                            nameET.setText("");
                            emailET.setText("");
                            passwordET.setText("");
                            dialog.dismiss();

                            Toast.makeText(getContext(), "Gagal mendaftarkan akun. Silahkan coba lagi", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void startAnimation() {
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        nameET.setAnimation(shake);
        emailET.setAnimation(shake);
        passwordET.setAnimation(shake);
    }

}
