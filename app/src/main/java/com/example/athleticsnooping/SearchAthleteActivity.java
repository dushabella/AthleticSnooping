package com.example.athleticsnooping;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchAthleteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_athlete);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //remove navigation bar (bars from the bottom and up of the page)
        View overlay = findViewById(R.id.drawer_layout);

        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN); //

        //TODO: Get rid of this. This is only to demonstrate searching
        SearchTask st = new SearchTask();
        st.execute((Void) null);
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
        getMenuInflater().inflate(R.menu.search_athlete, menu);
        return false;
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
        Intent intent = null;

        switch(id) {
            case R.id.nav_login:
                intent = new Intent(SearchAthleteActivity.this, LoginActivity.class);
                break;
            case R.id.nav_athlete:
                intent = new Intent(SearchAthleteActivity.this, SearchAthleteActivity.class);
                break;
            case R.id.nav_competition:
                intent = new Intent(SearchAthleteActivity.this, SearchCompetitionActivity.class);
                break;
            case R.id.nav_info:
                intent = new Intent(SearchAthleteActivity.this, InfoActivity.class);
                break;
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SearchTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            DatabaseConnector dc = new DatabaseConnector();
            Map<String, String> queryFields = new HashMap<>();
            queryFields.put("Imie", "Marcin");
            queryFields.put("Nazwisko", "Lewandowski");
            JSONArray results = dc.searchWithQuery(queryFields, "athletes");
            for(int i=0; i < results.length(); i++) {
                try {
                    JSONObject res = results.getJSONObject(i);
                    double score = res.getDouble("_score");
                    String name = res.getJSONObject("_source").getString("Imie");
                    String surname = res.getJSONObject("_source").getString("Nazwisko");
                    System.out.println(name + " " + surname + " -> score = " + score);
                } catch(JSONException je) {
                    je.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
                finish();
        }

        @Override
        protected void onCancelled() {
        }
    }
}