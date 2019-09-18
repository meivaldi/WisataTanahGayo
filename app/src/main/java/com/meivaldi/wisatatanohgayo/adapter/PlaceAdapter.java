package com.meivaldi.wisatatanohgayo.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meivaldi.wisatatanohgayo.CircleTransform;
import com.meivaldi.wisatatanohgayo.DetailTempatWisata;
import com.meivaldi.wisatatanohgayo.MyApppGlideModule;
import com.meivaldi.wisatatanohgayo.Place;
import com.meivaldi.wisatatanohgayo.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder>
    implements Filterable {

    private Activity activity;
    private Context context;
    private List<Place> places;
    private List<Place> placesFiltered;

    public PlaceAdapter(Activity activity, Context context, List<Place> places) {
        this.activity = activity;
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
    public void onBindViewHolder(@NonNull final PlaceAdapter.MyViewHolder holder, final int position) {
        final Place place = placesFiltered.get(position);

        holder.placeName.setText(place.getNama_tempat());

        StorageReference reference = FirebaseStorage.getInstance().getReference(place.getFoto());

        reference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Glide.with(context)
                                    .load(task.getResult())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .thumbnail(0.5f)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(holder.image);
                        }
                    }
                });

        int starCount = place.getRating();

        for (int i=0; i<starCount; i++) {
            holder.stars[i].setBackgroundResource(R.drawable.star);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                        holder.image, "image");

                Intent intent = new Intent(context, DetailTempatWisata.class);
                intent.putExtra("position", place.getId());
                context.startActivity(intent, options.toBundle());
            }
        });
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
        private RelativeLayout root;

        public MyViewHolder(@NonNull View view) {
            super(view);
            root = view.findViewById(R.id.root);
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
