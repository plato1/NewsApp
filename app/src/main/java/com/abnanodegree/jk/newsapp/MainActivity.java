package com.abnanodegree.jk.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
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
import java.util.List;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;

//import android.content.Loader;

/**
 * https://lab.getbase.com/introduction-to-coordinator-layout-on-android/
 * https://github.com/saulmm/CoordinatorBehaviorExample
 *http://saulmm.github.io/mastering-coordinator
 * https://developer.android.com/guide/components/loaders.html
 * http://www.androiddesignpatterns.com/2012/07/understanding-loadermanager.html
 */

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // replace STRING with query string recieved from user
    private static final String GNEWS_REQUEST_URL =
            "https://content.guardianapis.com/search?STRING&show-fields=body&api-key=test";
    private String queryString;

    // create empty arraylist of Book objects into which queried book titles will be placed
    private ArrayList<News> newsList;

    private SearchView searchView;
    // listview will be handled by ArrayAdapter
    private ListView lv;
    //TextView that is displayed when the list is empty...managed by listview
    private TextView mEmptyStateTextView;

    /** Adapter for the list of newses */
    private NewsAdapter newsAdapter;
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    // Get a reference to the LoaderManager, in order to interact with loaders.
    private android.app.LoaderManager loaderManager;
    // The callbacks through which we will interact with the LoaderManager.
    private android.app.LoaderManager.LoaderCallbacks<List<News>> mCallbacks;

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
        newsAdapter = new NewsAdapter(this, newsList);
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

        // The Activity (which implements the LoaderCallbacks<Cursor>
        // interface) is the callbacks object through which we will interact
        // with the LoaderManager. The LoaderManager uses this object to
        // instantiate the Loader and to notify the client when data is made
        // available/unavailable.
        mCallbacks = this;

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getLoaderManager();
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
//        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, mCallbacks);
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
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        return new NewsLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        newsAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newses != null && !newses.isEmpty()) {
            newsAdapter.addAll(newses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        newsAdapter.clear();
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
                if (query.length() > 0) {
                    // clear current arraylist of all elements in prep for new search string
                    // garbage collector will automatically reclaim freed memory
                    newsList.clear();
                    // check that network is available....if not inform user
                    if (!networkUp()) {
                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        return false;
                    }
                    else {
                        mEmptyStateTextView.setVisibility(View.GONE);
                    }
                    // Build query string
                    queryString = GNEWS_REQUEST_URL;
                    queryString = queryString.replace("STRING",query);

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
           //         loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, mCallbacks);
                    loaderManager.restartLoader(EARTHQUAKE_LOADER_ID, null, mCallbacks);
                    return true;
                } else
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
