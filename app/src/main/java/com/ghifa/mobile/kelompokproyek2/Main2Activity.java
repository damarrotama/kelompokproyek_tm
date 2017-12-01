package com.ghifa.mobile.kelompokproyek2;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ghifa.mobile.kelompokproyek2.fragment.home;
import com.ghifa.mobile.kelompokproyek2.fragment.pengumuman;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences session;
    FragmentManager managerFragment;
    FragmentTransaction transaksiFragment;
    home fragmentDepan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        managerFragment = getSupportFragmentManager();
        transaksiFragment = managerFragment.beginTransaction();

        fragmentDepan = new home();
        transaksiFragment.replace(R.id.drawer_kontent, fragmentDepan);
        transaksiFragment.commitAllowingStateLoss();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        transaksiFragment = managerFragment.beginTransaction();

        transaksiFragment.replace(R.id.drawer_kontent, fragmentDepan);

        if (id == R.id.nav_depan) {

            transaksiFragment.replace(R.id.drawer_kontent, fragmentDepan);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_pengumuman) {

            Log.d("tes", "btn pengumuman");
            pengumuman pengumuman = new pengumuman();
            transaksiFragment.replace(R.id.drawer_kontent, pengumuman);

        }  else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_exit) {
            new AlertDialog.Builder(Main2Activity.this)
                    .setMessage("Keluar dari aplikasi ?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            SharedPreferences.Editor setSession = session.edit();
                            setSession.clear();
                            setSession.commit();

                            //new clearTokenTask().execute((Void) null);

                            finish();

                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }

        transaksiFragment.commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
