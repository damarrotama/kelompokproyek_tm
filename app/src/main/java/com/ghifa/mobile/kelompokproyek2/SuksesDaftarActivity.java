package com.ghifa.mobile.kelompokproyek2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SuksesDaftarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sukses_daftar);
    }

    public void url_login(View arg0) {
        Intent utama = new Intent(SuksesDaftarActivity.this, MainActivity.class);
        startActivity(utama);
        finish();
    }
}
