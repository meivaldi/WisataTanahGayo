package com.meivaldi.wisatatanohgayo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meivaldi.wisatatanohgayo.CircleTransform;
import com.meivaldi.wisatatanohgayo.Place;
import com.meivaldi.wisatatanohgayo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder>
    implements Filterable {

    private Context context;
    private List<Place> places;
    private List<Place> placesFiltered;

    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
        this.placesFiltered = places;
    }

    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IndieFlower.ttf");
        Place place = placesFiltered.get(position);

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
        return placesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence sequence) {
                String charString = sequence.toString();

                if (charString.isEmpty()) {
                    placesFiltered = places;
                } else {
                    List<Place> filteredList = new ArrayList<>();

                    for (Place place: places) {
                        if (place.getNama_tempat().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(place);
                        }
                    }

                    placesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = placesFiltered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                placesFiltered = (ArrayList<Place>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView placeName;
        private ImageView image;
        private ImageView[] stars;

        public MyViewHolder(@NonNull View view) {
            super(view);
            placeName = view.findViewById(R.id.place);
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
