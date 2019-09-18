package com.meivaldi.wisatatanohgayo.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.meivaldi.wisatatanohgayo.Place;
import com.meivaldi.wisatatanohgayo.R;
import com.meivaldi.wisatatanohgayo.adapter.CardAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private RelativeLayout dataContainer;
    private ShimmerFrameLayout shimmerContainer;
    private DatabaseReference db;

    private List<Place> places;
    private CardAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dataContainer = view.findViewById(R.id.container);

        shimmerContainer = view.findViewById(R.id.shimmer_container);
        shimmerContainer.startShimmerAnimation();

        places = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), getContext(), places);

        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        db = FirebaseDatabase.getInstance().getReference().child("tempat_wisata");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                places.clear();
                ArrayList<Place> tempPlaces = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Place place = data.getValue(Place.class);
                    tempPlaces.add(place);
                }

                Collections.sort(tempPlaces, Collections.<Place>reverseOrder());

                for (int i=0; i<5; i++) {
                    places.add(tempPlaces.get(i));
                }

                adapter.notifyDataSetChanged();

                shimmerContainer.stopShimmerAnimation();
                shimmerContainer.setVisibility(View.GONE);
                dataContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Gagal memuat data: " + databaseError.toException(), Toast.LENGTH_SHORT).show();

                shimmerContainer.stopShimmerAnimation();
                shimmerContainer.setVisibility(View.GONE);
                dataContainer.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

}
