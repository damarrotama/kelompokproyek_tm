package com.ghifa.mobile.kelompokproyek2;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ghifa.mobile.kelompokproyek2.sqlite.Sqlite;

import java.util.StringTokenizer;

import me.leolin.shortcutbadger.ShortcutBadger;

public class PengumumanActivity extends AppCompatActivity {

    Sqlite dbhelper;
    String id_pengumuman,isi_pengumuman, judul_pengumuman, penerima, tanggal_mulai;
    TextView tvJudul, tvIsi, tvLeft, tvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengumuman);

        tvJudul = (TextView) findViewById(R.id.tvJudul);
        tvIsi = (TextView) findViewById(R.id.tvIsi);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);

        id_pengumuman = getIntent().getStringExtra("id_pengumuman");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pengumuman");
        setSupportActionBar(toolbar);

        ShortcutBadger.removeCount(this); //for 1.1.4+

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbhelper = new Sqlite(PengumumanActivity.this);

        Cursor dataPengumuman = dbhelper.query("SELECT * FROM PENGUMUMAN where id_pengumuman = '" + id_pengumuman + "' ");
        if (dataPengumuman != null && dataPengumuman.moveToFirst()) {
            judul_pengumuman    = dataPengumuman.getString(2);
            isi_pengumuman      = dataPengumuman.getString(3);
            penerima            = dataPengumuman.getString(7);
            tanggal_mulai       = dataPengumuman.getString(4);
        }

        StringTokenizer tokens = new StringTokenizer(tanggal_mulai, "-");
        String tahun_f = tokens.nextToken();
        String bulan_f = tokens.nextToken();
        String tanggal_f = tokens.nextToken();

        String tg_mul = tanggal_f+"-"+bulan_f+"-"+tahun_f;

        tvJudul.setText(judul_pengumuman);
        tvIsi.setText(isi_pengumuman);
        tvLeft.setText("Dari : " + penerima);
        tvRight.setText(tg_mul);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
