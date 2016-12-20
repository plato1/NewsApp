package com.abnanodegree.jk.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * https://lab.getbase.com/introduction-to-coordinator-layout-on-android/
 * https://github.com/saulmm/CoordinatorBehaviorExample
 *http://saulmm.github.io/mastering-coordinator
 */

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // create empty arraylist of Book objects into which queried book titles will be placed
    private ArrayList<News> newsList;

    private SearchView searchView;
    // listview will be handled by ArrayAdapter
    private ListView lv;
    //TextView that is displayed when the list is empty...managed by listview
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore state members from saved instance
        if (savedInstanceState == null || !savedInstanceState.containsKey("NEWSLIST")) {
            newsList = new ArrayList<News>();
        } else {
            newsList = savedInstanceState.getParcelableArrayList("NEWSLIST");
        }

        setContentView(R.layout.activity_main);

        // Setup empty view to display when no news in list
        lv = (ListView) findViewById(R.id.listView);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        lv.setEmptyView(mEmptyStateTextView);
        // create adapter
        NewsAdapter newsAdapter = new NewsAdapter(this, newsList);
        // Assign adapter to ListView
        lv.setAdapter(newsAdapter);

        // a Navigation View consisting of a sliding drawer made up of a header and a menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check if there is an internet connection
        if (!networkUp()) {
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Call the super method so that the states of our views are saved
        super.onSaveInstanceState(savedInstanceState);
        // Save local vars
        savedInstanceState.putParcelableArrayList("NEWSLIST", newsList);

    }


    private boolean networkUp() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
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
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("onQueryTextSubmit", " this worked");

                // moved searchView var from this method into the class variable (global)
                // must manually release focus that is on searchView...else keyboard will cover part of ui view
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("onQueryTextChange", " newText= " + newText);
                return false;
            }

        });
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Snackbar.make(getCurrentFocus(), "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            // Toast.makeText(getApplicationContext(),"Replace with your own action", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
