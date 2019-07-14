package com.meivaldi.wisatatanohgayo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.meivaldi.wisatatanohgayo.Place;
import com.meivaldi.wisatatanohgayo.R;
import com.meivaldi.wisatatanohgayo.adapter.PlaceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverFragment extends Fragment {

    private ShimmerFrameLayout shimmerContainer;
    private DatabaseReference db;

    private AppCompatEditText keywordET;
    private RelativeLayout dataContainer;

    private List<Place> places;
    private PlaceAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        keywordET = view.findViewById(R.id.keywordET);
        dataContainer = view.findViewById(R.id.data);

        shimmerContainer = view.findViewById(R.id.shimmer_container);
        shimmerContainer.startShimmerAnimation();

        places = new ArrayList<>();
        adapter = new PlaceAdapter(getContext(), places);

        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        keywordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        db = FirebaseDatabase.getInstance().getReference().child("tempat_wisata");
        db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()) {
                            Place place = data.getValue(Place.class);

                            places.add(place);
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
