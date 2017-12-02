package com.ghifa.mobile.kelompokproyek2.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghifa.mobile.kelompokproyek2.R;
import com.ghifa.mobile.kelompokproyek2.component.ControllerUrl;

/**
 * Created by Mobile on 12/1/2017.
 */

public class home extends Fragment {

    TextView tvNama,tvEmail,tvTglLahir;
    SharedPreferences session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvNama = (TextView) view.findViewById(R.id.tvNama);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvTglLahir = (TextView) view.findViewById(R.id.tvTglLahir);

        session = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tvNama.setText(session.getString("nama",""));
        tvEmail.setText(session.getString("email",""));
        tvTglLahir.setText(session.getString("tgllahir",""));

    }

}
