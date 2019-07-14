package com.meivaldi.wisatatanohgayo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meivaldi.wisatatanohgayo.adapter.ImageAdapter;
import com.meivaldi.wisatatanohgayo.adapter.ReviewAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailTempatWisata extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView image;
    private TextView namaTempat, content, sumber, alamat, luas, ketinggian, closerLabel;
    private DatabaseReference db;

    private List<Image> imageList;
    private ImageAdapter adapter;
    private RecyclerView recyclerView;

    private List<Ulasan> reviewList;
    private ReviewAdapter reviewAdapter;
    private RecyclerView reviewRecyclerView;
    private TextView emptyLabel;

    private ProgressDialog pDialog;
    private Dialog dialog;
    private ImageView detailImage;
    private RelativeLayout close;

    private Button ulasan, kirimUlasan;
    private Dialog ulasanDialog;
    private AppCompatEditText ulasanET;
    private ImageView star1, star2, star3, star4, star5;
    private int ratingValue;

    private Place currentPlace;
    private List<Place> closerPlaceList;
    private FrameLayout frameLayout;

    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;

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
        closerLabel = findViewById(R.id.closer_label);
        frameLayout = findViewById(R.id.frame_layout);

        final int position = getIntent().getIntExtra("position", 0);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Memuat...");
        pDialog.setCancelable(false);

        dialog = new Dialog(this);
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
                closerLabel.setText("Tempat wisata lainnya didekat " + place.getNama_tempat());
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int pos) {
                        pDialog.show();
                        StorageReference ref = FirebaseStorage.getInstance().getReference(imageList.get(pos).getImageUrl());
                        ref.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Glide.with(getApplicationContext())
                                                    .load(task.getResult())
                                                    .transition(DrawableTransitionOptions.withCrossFade())
                                                    .thumbnail(0.5f)
                                                    .into(detailImage);
                                        }
                                    }
                                });

                        dialog.show();
                        pDialog.dismiss();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        adapter.notifyDataSetChanged();

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewList);

        reviewRecyclerView = findViewById(R.id.ulasan_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(reviewAdapter);

        emptyLabel = findViewById(R.id.empty);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("ulasan_user")
                .child(String.valueOf(position));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Ulasan ulasan = data.getValue(Ulasan.class);
                    reviewList.add(ulasan);
                }

                if (reviewList.size() == 0) {
                    emptyLabel.setVisibility(View.VISIBLE);
                    reviewRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyLabel.setVisibility(View.GONE);
                    reviewRecyclerView.setVisibility(View.VISIBLE);
                }

                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ulasanDialog = new Dialog(this);
        ulasanDialog.setContentView(R.layout.rating_dialog);
        ulasanDialog.setCancelable(true);
        ulasanDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        star1 = ulasanDialog.findViewById(R.id.star1);
        star2 = ulasanDialog.findViewById(R.id.star2);
        star3 = ulasanDialog.findViewById(R.id.star3);
        star4 = ulasanDialog.findViewById(R.id.star4);
        star5 = ulasanDialog.findViewById(R.id.star5);

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star_empty);
                star3.setImageResource(R.drawable.star_empty);
                star4.setImageResource(R.drawable.star_empty);
                star5.setImageResource(R.drawable.star_empty);

                ratingValue = 1;
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star_empty);
                star4.setImageResource(R.drawable.star_empty);
                star5.setImageResource(R.drawable.star_empty);

                ratingValue = 2;
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star_empty);
                star5.setImageResource(R.drawable.star_empty);

                ratingValue = 3;
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star_empty);

                ratingValue = 4;
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);

                ratingValue = 5;
            }
        });

        ulasanET = ulasanDialog.findViewById(R.id.ulasanET);
        kirimUlasan = ulasanDialog.findViewById(R.id.kirim);

        kirimUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog.show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String ulasan = ulasanET.getText().toString();
                String namaUser = user.getDisplayName();

                Ulasan ulasanBaru = new Ulasan(namaUser, ulasan, ratingValue);

                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("ulasan_user")
                        .child(String.valueOf(position)).child(user.getDisplayName());

                dbRef.setValue(ulasanBaru)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DetailTempatWisata.this, "Ulasan Terkirim", Toast.LENGTH_SHORT).show();

                                    ulasanDialog.dismiss();
                                    pDialog.dismiss();
                                } else {
                                    Toast.makeText(DetailTempatWisata.this, "Gagal Mgengirim Ulasan", Toast.LENGTH_SHORT).show();

                                    ulasanET.setText("");
                                    ulasanDialog.dismiss();
                                    pDialog.dismiss();
                                }
                            }
                        });
            }
        });

        ulasan = findViewById(R.id.ulasan);
        ulasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    ulasanDialog.show();
                } else {
                    Toast.makeText(DetailTempatWisata.this, "Harap login dahulu untuk memberikan ulasan",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        closerPlaceList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        int position = getIntent().getIntExtra("position", 0);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("tempat_wisata")
                .child(String.valueOf(position));

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Place place = dataSnapshot.getValue(Place.class);

                LatLng currentPlace = new LatLng(place.getLat(), place.getLon());
                gMap.addMarker(new MarkerOptions().position(currentPlace).title("Marker in Sydney"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace, 12));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(DetailTempatWisata.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));

                                    PolylineOptions options = new PolylineOptions().width(5).color(Color.GREEN)
                                            .geodesic(true);

                                    options.add(new LatLng(location.getLatitude(), location.getLongitude()));
                                    options.add(new LatLng(place.getLat(), place.getLon()));

                                    gMap.addPolyline(options);
                                } else {
                                    Toast.makeText(DetailTempatWisata.this, "Hidupkan GPS Anda", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
