package com.meivaldi.wisatatanohgayo.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.meivaldi.wisatatanohgayo.CircleTransform;
import com.meivaldi.wisatatanohgayo.MainActivity;
import com.meivaldi.wisatatanohgayo.R;

public class ProfileFragment extends Fragment {

    private Button logout;
    private FirebaseAuth auth;
    private ImageView photo;

    private Dialog changeNameDialog, changeEmailDialog, changePasswordDialog;
    private RelativeLayout changeName, changeEmail, changePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/IndieFlower.ttf");

        auth = FirebaseAuth.getInstance();

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
        Glide.with(getContext()).load(R.drawable.user)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo);

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
