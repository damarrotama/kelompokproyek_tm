package com.ghifa.mobile.kelompokproyek2.model;

public class Pengumuman {
    String id_pengumuman        = null;
    String judul                = null;
    String isi                  = null;
    String tanggal_mulai        = null;
    String tanggal_selesai      = null;
    String is_read              = null;
    String penerima             = null;

    public Pengumuman(String id_pengumuman, String judul, String isi, String tanggal_mulai, String tanggal_selesai, String is_read, String penerima) {
        this.id_pengumuman      = id_pengumuman;
        this.judul              = judul;
        this.isi                = isi;
        this.tanggal_mulai      = tanggal_mulai;
        this.tanggal_selesai    = tanggal_selesai;
        this.is_read            = is_read;
        this.penerima           = penerima;
    }

    public String getId_pengumuman() {
        return id_pengumuman;
    }

    public String getJudulPengumuman() {
        return judul;
    }

    public String getIsiPengumuman() {
        return isi;
    }

    public String getTanggalMulai() {
        return tanggal_mulai;
    }

    public String getTanggalSelesai() {
        return tanggal_selesai;
    }

    public String getIsread() {
        return is_read;
    }

    public String getPenerima() {
        return penerima;
    }

}
