package com.meivaldi.wisatatanohgayo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meivaldi.wisatatanohgayo.Image;
import com.meivaldi.wisatatanohgayo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<Image> imageList;

    private Dialog dialog;
    private ImageView detailImage;
    private RelativeLayout close;

    public ImageAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;

        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.detail_image_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        detailImage = dialog.findViewById(R.id.detail_image);
        close = dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Image image = imageList.get(position);

        StorageReference reference = FirebaseStorage.getInstance().getReference(image.getImageUrl());
        reference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(context)
                                    .load(task.getResult())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .thumbnail(0.5f)
                                    .into(holder.imageView);
                        }
                    }
                });

        reference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(context)
                                    .load(task.getResult())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .thumbnail(0.5f)
                                    .into(detailImage);
                        }
                    }
                });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_item);
        }
    }

}
