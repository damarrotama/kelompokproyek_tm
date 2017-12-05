package com.ghifa.mobile.kelompokproyek2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ghifa.mobile.kelompokproyek2.component.ControllerUrl;
import com.ghifa.mobile.kelompokproyek2.http.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private EditText form_nama,form_password,form_email;
    String nama, password, emailnya;
    ControllerUrl link_url;
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // field inputan pada android
        form_nama         = (EditText) findViewById(R.id.nama);
        form_password     = (EditText) findViewById(R.id.password);
        form_email        = (EditText) findViewById(R.id.email);
        link_url          = new ControllerUrl(RegisterActivity.this);

    }

    public void checkBorang(View arg0) {

        new AlertDialog.Builder(RegisterActivity.this)
                .setMessage("Apakah anda yakin data yang anda isi sudah benar ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        final String cek_nama = form_nama.getText().toString();
                        if (!isValidNama(cek_nama)) {
                            //Set error message for nama field
                            form_nama.setError("Nama tidak boleh kosong");
                        }

                        final String cek_hp = form_password.getText().toString();
                        if (!isValidTelp(cek_hp)) {
                            //Set error message for hp field
                            form_password.setError("Password tidak boleh kosong");
                        }

                        final String email = form_email.getText().toString();
                        if (!isValidEmail(email)) {
                            //Set error message for email field
                            form_email.setError("Masukan email dengan benar");
                        }

                        if(isValidEmail(email) && isValidTelp(cek_hp) && isValidNama(cek_nama))
                        {
                            nama        = form_nama.getText().toString();
                            password          = form_password.getText().toString();
                            emailnya    = form_email.getText().toString();

                            mAuthTask = new UserLoginTask(nama, password, emailnya);
                            mAuthTask.execute((Void) null);

                        }

                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mNama;
        private final String mPassword;
        private final String mEmail;

        ProgressDialog pDialog;

        protected void onPreExecute() {
            //create and display your alert here
            pDialog = ProgressDialog.show(RegisterActivity.this,"Please wait...", "Now Loading ..", true);
        }

        UserLoginTask(String nama, String password, String emailnya) {
            mNama           = nama;
            mPassword       = password;
            mEmail          = emailnya;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                FormBody.Builder formBody = new FormBody.Builder();

                formBody.add("nama", mNama);
                formBody.add("password", mPassword);
                formBody.add("email", mEmail);

                RequestBody body = formBody.build();

                HttpClient client = new HttpClient();
                String postResponse = client.doPostBody(link_url.getRegisterUserPOST(), body);

                Log.e("onSuccess", postResponse);

                JSONObject response = new JSONObject(postResponse);

                // cek apakah berhasil atau tidak
                if (response.getInt(link_url.TAG_SUKSES) == 0 ) {
                    return false;
                }else if (response.getInt(link_url.TAG_SUKSES)==99){
                    return false;
                }

            } catch (IOException e) {
                Log.d("IOException", e.toString());
                return false;
            } catch (JSONException e) {
                Log.d("JSONException", e.toString());
                return false;
            }

            return true;
        }

        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();

            if (success) {
                Intent utama = new Intent(RegisterActivity.this, SuksesDaftarActivity.class);
                startActivity(utama);
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Masukkan data dengan benar/Pastikan email belum pernah digunakan !!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }




    // validating nama
    private boolean isValidNama(String cek_nama) {
        if (cek_nama != null && cek_nama.length() >= 1) {
            return true;
        }
        return false;
    }

    // validating telp
    private boolean isValidTelp(String cek_hp) {
        if (cek_hp != null && cek_hp.length() >= 1) {
            return true;
        }
        return false;
    }

    // validating email id
    private boolean isValidEmail(String email) {
        /*fungsi validasi email*/
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void url_login(View arg0) {
        Intent utama = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(utama);
        finish();
    }



}
