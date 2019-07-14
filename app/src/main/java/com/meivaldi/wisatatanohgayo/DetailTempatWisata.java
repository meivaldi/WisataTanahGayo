package com.meivaldi.wisatatanohgayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meivaldi.wisatatanohgayo.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailTempatWisata extends AppCompatActivity {

    private ImageView image;
    private TextView namaTempat, content, sumber, alamat, luas, ketinggian;
    private DatabaseReference db;

    private List<Image> imageList;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_tempat_wisata);

        image = findViewById(R.id.news_image);
        namaTempat = findViewById(R.id.name);
        content = findViewById(R.id.content);
        sumber = findViewById(R.id.source);
        alamat = findViewById(R.id.address);
        luas = findViewById(R.id.luas);
        ketinggian = findViewById(R.id.ketinggian);

        int position = getIntent().getIntExtra("position", 0);

        db = FirebaseDatabase.getInstance().getReference().child("tempat_wisata").child(String.valueOf(position));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Place place = dataSnapshot.getValue(Place.class);

                StorageReference reference = FirebaseStorage.getInstance().getReference(place.getFoto());

                reference.getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Glide.with(getApplicationContext())
                                            .load(task.getResult())
                                            .thumbnail(0.5f)
                                            .into(image);
                                }
                            }
                        });

                namaTempat.setText(place.getNama_tempat());
                content.setText(place.getDeskripsi());
                sumber.setText("Sumber: " + place.getSumber());
                alamat.setText(place.getAlamat());
                luas.setText(place.getLuas() + " km2");
                ketinggian.setText(place.getKetinggian() + " m");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageList = new ArrayList<>();
        adapter = new ImageAdapter(this, imageList);

        recyclerView = findViewById(R.id.image_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (position == 0) {
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image1.jpg"));
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image2.JPG"));
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image3.jpg"));
        } else {
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image1.jpg"));
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image2.jpg"));
            imageList.add(new Image("tempat_wisata/" + String.valueOf(position) + "/image3.jpg"));
        }

        adapter.notifyDataSetChanged();
    }
}
