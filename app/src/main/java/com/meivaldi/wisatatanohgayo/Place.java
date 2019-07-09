package com.meivaldi.wisatatanohgayo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Place {

    private String alamat, deskripsi, jam_operasional, ketinggian,
        lat, lon, luas, nama_tempat, sumber;

    public Place() {
    }

    public Place(String alamat, String deskripsi, String jam_operasional, String ketinggian,
                 String lat, String lon, String luas, String nama_tempat, String sumber) {
        this.alamat = alamat;
        this.deskripsi = deskripsi;
        this.jam_operasional = jam_operasional;
        this.ketinggian = ketinggian;
        this.lat = lat;
        this.lon = lon;
        this.luas = luas;
        this.nama_tempat = nama_tempat;
        this.sumber = sumber;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getJam_operasional() {
        return jam_operasional;
    }

    public void setJam_operasional(String jam_operasional) {
        this.jam_operasional = jam_operasional;
    }

    public String getKetinggian() {
        return ketinggian;
    }

    public void setKetinggian(String ketinggian) {
        this.ketinggian = ketinggian;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLuas() {
        return luas;
    }

    public void setLuas(String luas) {
        this.luas = luas;
    }

    public String getNama_tempat() {
        return nama_tempat;
    }

    public void setNama_tempat(String nama_tempat) {
        this.nama_tempat = nama_tempat;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }
}
