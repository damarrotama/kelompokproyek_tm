package com.ghifa.mobile.kelompokproyek2.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ghifa.mobile.kelompokproyek2.PengumumanActivity;
import com.ghifa.mobile.kelompokproyek2.R;
import com.ghifa.mobile.kelompokproyek2.component.ControllerUrl;
import com.ghifa.mobile.kelompokproyek2.http.HttpClient;
import com.ghifa.mobile.kelompokproyek2.model.Pengumuman;
import com.ghifa.mobile.kelompokproyek2.sqlite.Sqlite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Mobile on 12/1/2017.
 */

public class pengumuman extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ControllerUrl link_url;
    SharedPreferences session;
    private SwipeRefreshLayout mSwipeRefreshWidget;

    Pengumuman susunanPengumuman;
    JSONArray semuaDataPengumuman = null;
    pengumumanAdapter arrayAdapter;
    int PosLast = 0;


    private static ArrayList<Pengumuman> pengumumanList = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;

    Sqlite dbhelper;

    LinearLayout ll_not_found,ll_pengumuman;
    boolean status_loading = false;



    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        session = PreferenceManager.getDefaultSharedPreferences(getActivity());

        link_url = new ControllerUrl(getActivity());
        dbhelper = new Sqlite(getActivity());

        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        //mSwipeRefreshWidget.setColorScheme(R.color.color1);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        arrayAdapter = new pengumumanAdapter(getActivity(), pengumumanList);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onRefresh() {

        bindListView();

    }


    public void showRefreshProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideRefreshProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }
    @Override
    public void onResume() {

        bindListView();
        super.onResume();
    }

    public void bindListView() {
        new getPengumumanAsyncTask().execute();
    }

    class getPengumumanAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showRefreshProgress();
            //ll_not_found.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            return getPengumumanFromDB();
        }

        @Override
        protected void onPostExecute(Boolean sukses) {
            super.onPostExecute(sukses);
            if(sukses){

                if(ll_not_found.getVisibility()!=View.GONE) {
                    TranslateAnimation animate_ll_not_found = new TranslateAnimation(0, ll_not_found.getHeight(), 0, 0);
                    animate_ll_not_found.setDuration(1000);
                    animate_ll_not_found.setFillAfter(true);

                    ll_not_found.startAnimation(animate_ll_not_found);

                    ll_not_found.setVisibility(View.GONE);
                }
                ll_pengumuman.setVisibility(View.VISIBLE);

                if(!arrayAdapter.isEmpty()){
                    arrayAdapter.notifyDataSetChanged();
                    listView.setSelectionFromTop(PosLast, 0);
                }

            }else{
                TranslateAnimation animate_ll_not_found = new TranslateAnimation(ll_not_found.getHeight(),0,0,0);
                animate_ll_not_found.setDuration(1000);
                animate_ll_not_found.setFillAfter(true);

                ll_not_found.startAnimation(animate_ll_not_found);

                ll_not_found.setVisibility(View.VISIBLE);
                //ll_pengumuman.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.INVISIBLE);
            status_loading = true;
            tampildata();

            hideRefreshProgress();
        }
    }

    public boolean getPengumumanFromDB() {

        String tb_ = "PENGUMUMAN";
        dbhelper.hapus(tb_);

        pengumumanList.clear();

        int jikaSukses;
        boolean data;

        try {
            FormBody.Builder formBody = new FormBody.Builder();

            formBody.add("email", "-");

            RequestBody body = formBody.build();

            HttpClient client = new HttpClient();
            String postResponse = client.doPostBody(link_url.getURLPengumuman(), body);

            //Log.e("onSuccess", postResponse);

            JSONObject response = new JSONObject(postResponse);

            jikaSukses              = response.getInt(link_url.TAG_SUKSES);

            Log.e("onSuccess", Integer.toString(jikaSukses));

            Log.d("cek", postResponse);

            if (jikaSukses == 1) {

                semuaDataPengumuman = response.getJSONArray("pengumuman");

                for (int i = 0; i < semuaDataPengumuman.length(); i++) {

                    JSONObject rincianData 	= semuaDataPengumuman.getJSONObject(i);

                    dbhelper.InsertPengumuman(
                            rincianData.getString("id"),
                            rincianData.getString("judul"),
                            rincianData.getString("isi"),
                            rincianData.getString("tglkirim"),
                            rincianData.getString("tanggal_selesai"),
                            rincianData.getString("is_read"),
                            rincianData.getString("penerima")
                    );
                }

                if(semuaDataPengumuman.length()>0) {
                    Log.e("status sqlite", "bisa simpan sqlite karna data lebih dari 0");
                    data = true;
                }else{
                    Log.e("status sqlite", "gagal simpan sqlite karna data kosong");
                    data = false;
                }

            } else {
                Log.e("status sqlite", "gagal simpan sqlite");
                data = false;
            }

        } catch (IOException e) {
            data = false;
            Log.e("status sqlite", "gagal simpan sqlite catch 1");
        } catch (JSONException e) {
            data = false;
            Log.e("status sqlite", "gagal simpan sqlite catch 2");
        }
        return data;
    }

    public void tampildata(){

        pengumumanList.clear();

        Cursor tampilData = dbhelper.query("SELECT * FROM PENGUMUMAN order by is_read ASC,tanggal_mulai DESC  ");
        try {
            while (tampilData.moveToNext()) {
                susunanPengumuman = new Pengumuman(
                        tampilData.getString(1),
                        tampilData.getString(2),
                        tampilData.getString(3),
                        tampilData.getString(4),
                        tampilData.getString(5),
                        tampilData.getString(6),
                        tampilData.getString(7)
                );
                pengumumanList.add(susunanPengumuman);
            }
        } finally {
            if (tampilData != null && !tampilData.isClosed())
                tampilData.close();
        }

        arrayAdapter.notifyDataSetChanged();
    }

    public boolean getPengumuman() {

        boolean data;

        pengumumanList.clear();

        try {

            Cursor data_bayar = dbhelper.query("SELECT * FROM PENGUMUMAN order by is_read ASC,tanggal_mulai DESC  ");

            if (data_bayar != null && data_bayar.moveToFirst()) {
                do{

                    String id_pengumuman    = data_bayar.getString(1);
                    String judul            = data_bayar.getString(2);
                    String isi              = data_bayar.getString(3);
                    String tanggal_mulai    = data_bayar.getString(4);
                    String tanggal_selesai  = data_bayar.getString(5);
                    String is_read          = data_bayar.getString(6);
                    String penerima         = data_bayar.getString(7);

                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    StringTokenizer tokens = new StringTokenizer(tanggal_selesai, "-");
                    String tahun_s = tokens.nextToken();
                    String bulan_s = tokens.nextToken();
                    String tanggal_s = tokens.nextToken();

                    String tg_selesai = tahun_s+bulan_s+tanggal_s;

                    StringTokenizer tokens_data = new StringTokenizer(date, "-");
                    String tahun           = tokens_data.nextToken();
                    String bulan           = tokens_data.nextToken();
                    String tanggal         = tokens_data.nextToken();

                    String tg_now = tahun+bulan+tanggal;

                    susunanPengumuman = new Pengumuman(
                            id_pengumuman,
                            judul,
                            isi,
                            tanggal_mulai,
                            tanggal_selesai,
                            is_read,
                            penerima
                    );

                    pengumumanList.add(susunanPengumuman);

                } while (data_bayar.moveToNext());

                data = true;
            }else{
                data = false;
            }

        } catch (Exception e) {
            Log.d("IOException",e.toString());
            data = false;
        }

        return data;

    }

    private class pengumumanAdapter extends BaseAdapter
    {
        private ArrayList<Pengumuman> pengumumanList;
        private Context context;

        public pengumumanAdapter(Context context, ArrayList<Pengumuman> pengumumanList)
        {
            this.context = context;
            this.pengumumanList = pengumumanList;
        }

        private class ViewHolder
        {
            TextView tvTanggal_mulai, tvJudul, tvPengumuman, tvBaca, tvRight;
            LinearLayout LL_pengumuman;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            ViewHolder holder;

            if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.fragment_pengumuman_detail, parent, false);

                holder = new ViewHolder();

                holder.tvBaca                   = (TextView) convertView.findViewById(R.id.tvRead);
                holder.tvTanggal_mulai          = (TextView) convertView.findViewById(R.id.tvTanggal);
                holder.tvTanggal_mulai          = (TextView) convertView.findViewById(R.id.tvTanggal);
                holder.tvJudul                  = (TextView) convertView.findViewById(R.id.tvJudul);
                // holder.tvPengumuman             = (TextView) convertView.findViewById(R.id.tvPengumuman);
                holder.tvRight                   = (TextView) convertView.findViewById(R.id.tvRight);
                holder.LL_pengumuman            = (LinearLayout) convertView.findViewById(R.id.LL_pengumuman);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(!pengumumanList.isEmpty()){

                if(pengumumanList.size()<position){
                    position = pengumumanList.size() - 1;
                }

                final Pengumuman pengumuman = pengumumanList.get(position);

                StringTokenizer tokens = new StringTokenizer(pengumuman.getTanggalMulai(), "-");
                String tahun_f = tokens.nextToken();
                String bulan_f = tokens.nextToken();
                String tanggal_f = tokens.nextToken();

                String tg_mul = tanggal_f+"-"+bulan_f+"-"+tahun_f;

                String baca;
                if (pengumuman.getIsread().equals("0")){
                    baca = "Belum Dibaca";
                }else {
                    baca = "";
                }

                holder.tvBaca.setText(baca);

                if(baca.equals("Belum Dibaca")) {
                    holder.tvBaca.setVisibility(View.VISIBLE);
                } else {
                    holder.tvBaca.setVisibility(View.GONE);
                }

                holder.tvTanggal_mulai.setText("Dari : " + pengumuman.getPenerima());
                holder.tvTanggal_mulai.setTag(pengumuman);
                holder.tvJudul.setText(pengumuman.getJudulPengumuman());
                String isiPengumuman=pengumuman.getIsiPengumuman();
                String isiPengumumanCut = "";

                if(isiPengumuman.length()>100) {
                    isiPengumumanCut = pengumuman.getIsiPengumuman().substring(0, 100);
                } else {
                    isiPengumumanCut = isiPengumuman;
                }
                // holder.tvPengumuman.setText(isiPengumumanCut + " ... ");
                holder.tvRight.setText(tg_mul);

                holder.LL_pengumuman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent preview = new Intent(getActivity(), PengumumanActivity.class);
                        preview.putExtra("id_pengumuman", pengumuman.getId_pengumuman());
                        startActivity(preview);
                    }
                });

            }

            return convertView;
        }

        @Override
        public int getCount() {
            return pengumumanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

}
