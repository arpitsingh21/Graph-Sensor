package com.vikrantchoudhary.graphsensors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by db2admin on 8/2/2016.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public FrameLayout frameLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base,null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.frameDisplay);

        getLayoutInflater().inflate(layoutResID,frameLayout,true);

        super.setContentView(drawerLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView userName = (TextView) findViewById(R.id.tvUserName);
        TextView userMail = (TextView) findViewById(R.id.tvUserEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("SENSORSNAME", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("SENSORSNAME","").equals("")) {
            userName.setText(sharedPreferences.getString("SENSORSNAME", ""));
            userMail.setText(sharedPreferences.getString("SENSORSEMAIL", ""));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.nav_observe) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sensor){
            Intent intent = new Intent(this,SensorsListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_experiment) {
//            Intent intent = new Intent(this,SensorsListActivity.class);
//            startActivity(intent);

        } else if (id == R.id.nav_website) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://google.com"));
            startActivity(intent);

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}