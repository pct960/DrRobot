package com.example.tejasvi.drrobot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
// TODO: MUST HAVE A WAY TO RECEIVE THE DIGNOSED DISEASE AND PRESENT IT TO THE USER 

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean doubleBackToExitPressedOnce;
    FragmentManager fragmentManager = getSupportFragmentManager();
    ArrayList<String> chosensymptoms=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Perform a quick diagnosis");
        fragmentManager.beginTransaction().replace(R.id.fm1,  new diagnose()).addToBackStack(null).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        doubleBackToExitPressedOnce=false;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(doubleBackToExitPressedOnce && fragmentManager.getBackStackEntryCount()==1)
        {
            moveTaskToBack(true);
            MainActivity.this.finish();
        }
        else if(fragmentManager.getBackStackEntryCount()==1) {
            doubleBackToExitPressedOnce = true;
            Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        else if(fragmentManager.getBackStackEntryCount()!=1)
        {
            fragmentManager.popBackStack();
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
            setTitle("Settings");
            fragmentManager.beginTransaction().replace(R.id.fm1,new settings()).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diagnose)
        {
            setTitle("Perform a quick diagnosis");
            diagnose d=new diagnose();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("symptoms", chosensymptoms);
            d.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fm1,d).addToBackStack(null).commit();
        }

        else if(id==R.id.nav_settings)
        {
            setTitle("Settings");
            fragmentManager.beginTransaction().replace(R.id.fm1,new settings()).addToBackStack(null).commit();
        }
        else if(id==R.id.nav_learn)
        {
            setTitle("Learn more");
            fragmentManager.beginTransaction().replace(R.id.fm1,new learn()).addToBackStack(null).commit();
        }
        else if(id==R.id.nav_help)
        {
            setTitle("Help");
            fragmentManager.beginTransaction().replace(R.id.fm1,new help()).addToBackStack(null).commit();
        }
        else if(id==R.id.nav_feedback)
        {
            setTitle("Contact Us");
            fragmentManager.beginTransaction().replace(R.id.fm1,new contact()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
