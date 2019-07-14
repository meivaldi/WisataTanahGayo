package com.meivaldi.wisatatanohgayo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Ulasan {

    private String nama, ulasan;
    private int rating;

    public Ulasan(String nama, String ulasan, int rating) {
        this.nama = nama;
        this.ulasan = ulasan;
        this.rating = rating;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUlasan() {
        return ulasan;
    }

    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
