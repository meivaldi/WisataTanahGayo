package com.meivaldi.wisatatanohgayo.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.meivaldi.wisatatanohgayo.R;

public class HomeFragment extends Fragment {

    private ShimmerFrameLayout shimmerContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/IndieFlower.ttf");

        TextView title = view.findViewById(R.id.title);
        title.setTypeface(typeface);

        shimmerContainer = view.findViewById(R.id.shimmer_container);
        shimmerContainer.startShimmerAnimation();

        return view;
    }

}
