package com.ghifa.mobile.kelompokproyek2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ghifa.mobile.kelompokproyek2.component.ControllerUrl;
import com.ghifa.mobile.kelompokproyek2.component.Fungsi;
import com.ghifa.mobile.kelompokproyek2.http.HttpClient;
import com.ghifa.mobile.kelompokproyek2.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;
    SharedPreferences session;
    ControllerUrl link_url;
    String username, password, emailuser;
    private UserLoginTask mAuthTask = null;
    private EditText emailEditText, passEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference FdbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading..");
        dialog.setTitle("Please Wait");
        dialog.setCancelable(false);

        // field inputan pada android
        emailEditText   = (EditText) findViewById(R.id.username);
        passEditText    = (EditText) findViewById(R.id.password);

        session     = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        link_url    = new ControllerUrl(MainActivity.this);

        int login = session.getInt(link_url.TAG_IS_LOGIN, 0);

        mAuth = FirebaseAuth.getInstance();
        FdbReference = FirebaseDatabase.getInstance().getReference();

        Log.d("status_login", Integer.toString(login));


        if(login==link_url.IS_LOGIN) {
            Intent utama = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(utama);
            finish();
            Log.d("status_session","sesion ditemukan");
        }else{
            Log.d("status_session","session not found");
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        FdbReference.child("users").child(userId).setValue(user);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private final String mPassword;
        private final String mToken;
        private String mEmail;

        UserLoginTask(String username, String password, String token, String emailnya) {
            Fungsi generate = new Fungsi();
            mUsername = username;
            //mPassword = generate.MD5_Hash(password) + generate.MD5_Hash(username);
            mPassword = password;
            mToken = token;
            mEmail = emailnya;
        }

        ProgressDialog pDialog;

        protected void onPreExecute() {
            //create and display your alert here
            pDialog = ProgressDialog.show(MainActivity.this,"Please wait...", "Now Loading ..", true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                HttpClient client   = new HttpClient();
                String json         = client.setLoginJson(mUsername, mPassword,mToken);
                String postResponse = client.doPostRequest(link_url.getURLLogin(), json);

                Log.d("onSuccess", postResponse);

                JSONObject response = new JSONObject(postResponse);


                int suksesya = response.getInt("sukses");

                //cek apakah berhasil atau tidak
                if (suksesya==0) {

                    return false;

                }else{

                    mEmail= response.getString("email");

                    Log.e("emailslsl",mEmail);

                    SharedPreferences.Editor setSession = session.edit();
                    /**
                     * menyimpan session login
                     */
                    setSession.putInt(link_url.TAG_IS_LOGIN, response.getInt("sukses"));
                    setSession.putString("idpengguna", response.getString("idpengguna"));
                    setSession.putString("email", response.getString("email"));
                    setSession.putString("tgllahir", response.getString("tgllahir"));
                    setSession.putString("nama", response.getString("name"));
                    setSession.apply();


                }

            } catch (IOException e) {
                Log.d("IOException", e.toString());
                return false;
            }
            catch (JSONException e) {
                Log.d("JSONException", e.toString());
                return false;
            }

            return true;
        }

        protected  void onPostExecute(final Boolean success) {
            pDialog.dismiss();

            if (success) {

                mAuth.createUserWithEmailAndPassword(mEmail, "Password12345")
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("", "Authentication successful");

                                if (task.isSuccessful()) {

                                    mAuth.signInWithEmailAndPassword(mEmail, "Password12345").addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                                            FirebaseUser user = task.getResult().getUser();

                                            writeNewUser(user.getUid(),mUsername,mEmail);
                                            pDialog.dismiss();

                                            Intent utama = new Intent(MainActivity.this, Main2Activity.class);
                                            startActivity(utama);

                                            finish();

                                        }
                                    });

                                } else{

                                    mAuth.signInWithEmailAndPassword(mEmail, "Password12345").addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());


                                            FirebaseUser user = task.getResult().getUser();

                                            writeNewUser(user.getUid(),mUsername,mEmail);
                                            pDialog.dismiss();

                                            Intent utama = new Intent(MainActivity.this, Main2Activity.class);
                                            startActivity(utama);

                                            finish();
                                        }
                                    });

                                }
                        }

                });


            }else{
                Toast.makeText(getApplicationContext(), "username atau password anda salah !!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void checkLogin(View arg0) {

        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            emailEditText.setError("Username tidak boleh kosong");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            passEditText.setError("Password tidak boleh kosong");
        }

        if(isValidEmail(email) && isValidPassword(pass))
        {
            final String tokenID = FirebaseInstanceId.getInstance().getToken();

            username = emailEditText.getText().toString();
            password = passEditText.getText().toString();

            mAuthTask = new UserLoginTask(username, password, tokenID, emailuser);
            mAuthTask.execute((Void) null);

        }
    }

    public void Register(View arg0) {
        Intent utama = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(utama);
        finish();
    }

    // validating email id
    private boolean isValidEmail(String email) {
        if (email != null && email.length() >= 1) {
            return true;
        }
        return false;
    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 1) {
            return true;
        }
        return false;
    }
}
