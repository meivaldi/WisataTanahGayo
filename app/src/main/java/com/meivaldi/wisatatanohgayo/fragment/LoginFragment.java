package com.meivaldi.wisatatanohgayo.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.meivaldi.wisatatanohgayo.MainActivity;
import com.meivaldi.wisatatanohgayo.R;

public class LoginFragment extends Fragment {

    private Button register, login;
    private EditText emailET, passwordET;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        dialog.setMessage("Memroses");

        register = view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new RegisterFragment());
            }
        });

        login = view.findViewById(R.id.login);
        emailET = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    loginUser(email, password);
                }
            }
        });

        return view;
    }

    private void loginUser(final String email, final String password) {
        dialog.show();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("fragment", 0);

                            getContext().startActivity(intent);
                            getActivity().finish();

                            dialog.dismiss();
                        } else {
                            //startAnimation();

                            emailET.setText("");
                            passwordET.setText("");

                            Toast.makeText(getContext(), "Terjadi kesalahan, silahkan coba lagi", Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
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
        emailET.setAnimation(shake);
        passwordET.setAnimation(shake);
    }

}
