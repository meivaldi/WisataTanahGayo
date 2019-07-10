package com.meivaldi.wisatatanohgayo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.wisatatanohgayo.CircleTransform;
import com.meivaldi.wisatatanohgayo.Place;
import com.meivaldi.wisatatanohgayo.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    private Context context;
    private List<Place> places;

    public CardAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_place_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IndieFlower.ttf");
        Place place = places.get(position);

        holder.placeName.setTypeface(typeface);
        holder.placeName.setText(place.getNama_tempat());

        Glide.with(context).load(R.drawable.place)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        int starCount = place.getRating();

        for (int i=0; i<starCount; i++) {
            holder.stars[i].setBackgroundResource(R.drawable.star_filled);
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView placeName;
        private ImageView image;
        private ImageView[] stars;

        public MyViewHolder(@NonNull View view) {
            super(view);
            placeName = view.findViewById(R.id.name);
            image = view.findViewById(R.id.image);

            stars = new ImageView[5];
            stars[0] = view.findViewById(R.id.star1);
            stars[1] = view.findViewById(R.id.star2);
            stars[2] = view.findViewById(R.id.star3);
            stars[3] = view.findViewById(R.id.star4);
            stars[4] = view.findViewById(R.id.star5);
        }
    }

}
