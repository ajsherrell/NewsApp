package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// used some code from the QuakeReport App Udacity

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>,
 SharedPreferences.OnSharedPreferenceChangeListener {

    // URL with my private key from Guardian API
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    // api key
    private static final String MY_API_KEY = "c275cc44-cc41-4cb6-a339-aa1c72d60bf0";

    // adapter for the list of news
    private NewsAdapter mAdapter;

    // constant value for the news loader Id.
    private static final int NEWS_LOADER_ID = 1;

    // log for logcat
    private static final String TAG = MainActivity.class.getName();

    // empty text view when no date is available
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        // connect and set empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // create a new adapter that takes an empty list of news articles as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // obtain a reference to the SharedPreference file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // and register to be notified of preference changes
        // so we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        // set click listener to go to website
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // find the current news article that was clicked on
                News currentNewsArticle = mAdapter.getItem(i);

                // convert the String URL into a URI object (to pass into the Intent
                // constructor)
                Uri newsUri = Uri.parse(currentNewsArticle.getUrl());

                // create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state
        // of network connectivity
        // --used from Quake Report App
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();
            Log.i(TAG, "onCreate: Hey this is from LoaderManager!!!!");

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            Log.i(TAG, "onCreate: This one is initLoader!!");
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_editions_key)) ||
                key.equals(getString(R.string.settings_order_by_key))) {
            // Clear the ListView as a new query will be kicked off
            mAdapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            mEmptyStateTextView.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter
        // is the default value for this preference.
        String editions = sharedPrefs.getString(
                getString(R.string.settings_editions_key),
                getString(R.string.settings_editions_default)
        );

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // start building URL with Uri.parse
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // add parameters
        uriBuilder.appendQueryParameter("editions", editions);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", "10");
        uriBuilder.appendQueryParameter("show-fields", "byline");
        uriBuilder.appendQueryParameter("api-key", MY_API_KEY);
        // get full url in log 'https://content.guardianapis.com/search?q=editions?q=us&order-by=newest&page-size=10&show-fields=byline'
        Log.i(TAG, uriBuilder.toString());

        // create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // hide loading indicator because data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        Log.i(TAG, "onLoadFinished: The load has finished!!!!!!!!");

        // set empty state text to display "No news articles found."
        mEmptyStateTextView.setText(R.string.no_news_articles);

        // if there is a valid list of {@link News} objects,
        // then add them to the adapter's data set. This will trigger
        // the ListView to update.
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // loader reset, so we can clear out our existing data
        mAdapter.clear();
        Log.i(TAG, "onLoaderReset: The loader has reset!!!!!!!!!!!");
    }

    @Override
    // this method initializes the contents of the activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the options menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
