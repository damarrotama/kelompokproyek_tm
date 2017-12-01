package com.ghifa.mobile.kelompokproyek2.sqlite;

/**
 * Created by ghifa on 06/12/16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Sqlite extends SQLiteOpenHelper{


    private static final String DATABASE_NAME = "db_proyek";

    /**
     * setiap ada perubahan struktur ataupu penambahan tabel
     * DATABASE_VERSION harus dinaikan / +1
     */
    private static final int DATABASE_VERSION = 1;

    public Sqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void createTable(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS PENGUMUMAN ");
        db.execSQL("CREATE TABLE if not exists PENGUMUMAN (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " id_pengumuman TEXT, "
                + " judul TEXT, "
                + " isi TEXT, "
                + " tanggal_mulai TEXT, "
                + " tanggal_selesai TEXT, "
                + " is_read TEXT, "
                + " penerima TEXT "
                + ");");


    }


    public void InsertPengumuman(
            String id_pengumuman,
            String judul_pengumuman,
            String isi_pengumuman,
            String tanggal_mulai,
            String tanggal_selesai,
            String is_read,
            String penerima
    ) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_pengumuman", id_pengumuman);
        values.put("judul", judul_pengumuman);
        values.put("isi", isi_pengumuman);
        values.put("tanggal_mulai", tanggal_mulai);
        values.put("tanggal_selesai", tanggal_selesai);
        values.put("is_read", is_read);
        values.put("penerima", penerima);
        db.insert("PENGUMUMAN", null, values);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PENGUMUMAN ");
        // Create tables again
        createTable(db);
    }

    public Cursor query(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void tutup() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.close();
    }

    public void hapus(String namatabel) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(namatabel,null,null);
    }
}
