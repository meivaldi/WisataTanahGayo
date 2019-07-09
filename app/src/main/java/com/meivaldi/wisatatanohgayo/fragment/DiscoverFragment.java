package com.meivaldi.wisatatanohgayo.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

public class DiscoverFragment extends Fragment {

    private ShimmerFrameLayout shimmerContainer;
    private DatabaseReference db;

    private AppCompatEditText keywordET;
    private RecyclerView recyclerView;
    private RelativeLayout dataContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        keywordET = view.findViewById(R.id.keywordET);
        recyclerView = view.findViewById(R.id.recycler_view);
        dataContainer = view.findViewById(R.id.data);

        shimmerContainer = view.findViewById(R.id.shimmer_container);
        shimmerContainer.startShimmerAnimation();

        db = FirebaseDatabase.getInstance().getReference("wisata-tanoh-gayo/1");

        db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Place place = dataSnapshot.getValue(Place.class);

                        Toast.makeText(getContext(), place.getNama_tempat(), Toast.LENGTH_SHORT).show();

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
