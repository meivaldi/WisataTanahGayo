package com.meivaldi.wisatatanohgayo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meivaldi.wisatatanohgayo.R;
import com.meivaldi.wisatatanohgayo.Ulasan;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context context;
    private List<Ulasan> reviewList;

    public ReviewAdapter(Context context, List<Ulasan> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rating_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Ulasan ulasan = reviewList.get(position);

        String uri = "user_photo/" + ulasan.getNama() + "/user.png";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(uri);

        storageReference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                             Glide.with(context)
                                .load(task.getResult())
                                .apply(RequestOptions.circleCropTransform())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(holder.userPhoto);
                        } else {
                            Glide.with(context)
                                    .load(R.drawable.users)
                                    .apply(RequestOptions.circleCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(holder.userPhoto);
                        }
                    }
                });

        holder.userName.setText(ulasan.getNama());
        holder.userReview.setText(ulasan.getUlasan());

        for (int i=0; i<ulasan.getRating(); i++) {
            holder.stars[i].setBackgroundResource(R.drawable.star);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView userPhoto;
        public TextView userName, userReview;
        private ImageView[] stars;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.user_photo);
            userName = itemView.findViewById(R.id.user_name);
            userReview = itemView.findViewById(R.id.review);

            stars = new ImageView[5];
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
        }
    }
}
