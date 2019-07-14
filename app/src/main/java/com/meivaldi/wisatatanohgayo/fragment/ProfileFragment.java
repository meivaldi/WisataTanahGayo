package com.meivaldi.wisatatanohgayo.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.meivaldi.wisatatanohgayo.CircleTransform;
import com.meivaldi.wisatatanohgayo.MainActivity;
import com.meivaldi.wisatatanohgayo.R;

public class ProfileFragment extends Fragment {

    private Button logout;
    private FirebaseAuth auth;
    private ImageView photo;

    private Dialog changeNameDialog, changeEmailDialog, changePasswordDialog;
    private RelativeLayout changeName, changeEmail, changePassword;

    private AppCompatEditText newNameET, newEmailET, newPasswordET;
    private Button changeNameBT, changeEmailBT, changePasswordBT;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/IndieFlower.ttf");

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Memroses...");
        progressDialog.setCancelable(false);

        TextView name = view.findViewById(R.id.name);
        TextView email = view.findViewById(R.id.email);
        TextView usernameLabel = view.findViewById(R.id.username_label);
        TextView passwordLabel = view.findViewById(R.id.password_label);
        TextView emailLabel = view.findViewById(R.id.email_label);
        logout = view.findViewById(R.id.logout);

        name.setTypeface(typeface);
        email.setTypeface(typeface);
        usernameLabel.setTypeface(typeface);
        passwordLabel.setTypeface(typeface);
        emailLabel.setTypeface(typeface);
        logout.setTypeface(typeface);

        changeName = view.findViewById(R.id.change_name);
        changeEmail = view.findViewById(R.id.change_email);
        changePassword = view.findViewById(R.id.change_password);

        changeNameDialog = new Dialog(getContext());
        changeNameDialog.setCancelable(true);
        changeNameDialog.setContentView(R.layout.name_dialog);
        changeNameDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        changeEmailDialog = new Dialog(getContext());
        changeEmailDialog.setCancelable(true);
        changeEmailDialog.setContentView(R.layout.email_dialog);
        changeEmailDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        changePasswordDialog = new Dialog(getContext());
        changePasswordDialog.setCancelable(true);
        changePasswordDialog.setContentView(R.layout.password_dialog);
        changePasswordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        newNameET = changeNameDialog.findViewById(R.id.new_name);
        newNameET.setTypeface(typeface);

        changeNameBT = changeNameDialog.findViewById(R.id.change_name);
        changeNameBT.setTypeface(typeface);
        changeNameBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String newName = newNameET.getText().toString();

                if (!newName.isEmpty()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build();

                    FirebaseUser user = auth.getCurrentUser();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Berhasil mengubah nama", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        intent.putExtra("fragment", 2);
                                        startActivity(intent);

                                        changeNameDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Gagal mengubah nama", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            }
        });

        newEmailET = changeEmailDialog.findViewById(R.id.new_email);
        newEmailET.setTypeface(typeface);

        changeEmailBT = changeEmailDialog.findViewById(R.id.change_email);
        changeEmailBT.setTypeface(typeface);
        changeEmailBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String newEmail = newEmailET.getText().toString();

                if (!newEmail.isEmpty()) {
                    FirebaseUser user = auth.getCurrentUser();

                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Berhasil mengubah email", Toast.LENGTH_SHORT).show();

                                        changeEmailDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Gagal mengubah email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            }
        });

        newPasswordET = changePasswordDialog.findViewById(R.id.new_password);
        newPasswordET.setTypeface(typeface);

        changePasswordBT = changePasswordDialog.findViewById(R.id.change_password);
        changePasswordBT.setTypeface(typeface);
        changePasswordBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String newPassword = newPasswordET.getText().toString();

                if (!newPassword.isEmpty()) {
                    FirebaseUser user = auth.getCurrentUser();

                    user.updatePassword(newPassword)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Berhasil mengubah password", Toast.LENGTH_SHORT).show();

                                        changePasswordDialog.dismiss();
                                    } else {
                                        Toast.makeText(getContext(), "Gagal mengubah password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameDialog.show();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailDialog.show();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.show();
            }
        });

        photo = view.findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                getActivity().startActivityForResult(Intent.createChooser(intent, "Pilih file"), 100);
            }
        });

        if (auth.getCurrentUser().getPhotoUrl() == null) {
            Glide.with(getContext()).load(R.drawable.users)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photo);
        } else {
            Glide.with(getContext()).load(auth.getCurrentUser().getPhotoUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .apply(RequestOptions.circleCropTransform())
                    .into(photo);
        }

        FirebaseUser user = auth.getCurrentUser();
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("fragment", 2);
                getContext().startActivity(intent);

                getActivity().finish();
            }
        });


        return view;
    }

}
