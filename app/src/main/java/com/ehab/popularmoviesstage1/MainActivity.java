package com.ehab.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ehab.popularmoviesstage1.NetworkUtilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetail[]>,PopularMoviesAdapter.ListItemClickListener{

    public static final int MOVIES_LOADER_ID = 22;

    RecyclerView mMoviesRecyclerview;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    PopularMoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerview = (RecyclerView) findViewById(R.id.movies_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerview.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerview.setHasFixedSize(true);

        mAdapter = new PopularMoviesAdapter(this, this);

        mMoviesRecyclerview.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        int loaderId = MOVIES_LOADER_ID;

        LoaderManager.LoaderCallbacks<MovieDetail[]> callback = MainActivity.this;

        // We created bundle to pass extra data through initloader that can be accessed inside onCreateLoader
        Bundle bundleForLoader = null;
        getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);


    }

    @Override
    public void onListItemClick(MovieDetail movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, movie);
        startActivity(intent);
    }




    @Override
    public Loader<MovieDetail[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDetail[]>(this) {
            MovieDetail[]  mMoviesDetails;

            @Override
            protected void onStartLoading() {
                if(args != null)
                    deliverResult(mMoviesDetails);
                else{
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public MovieDetail[] loadInBackground() {
                URL moviesRequestURL = NetworkUtils.buildUrl();
                Log.d("Activityyyyy", moviesRequestURL.toString());

                try {
                    String jsonMoviesResponse = NetworkUtils
                            .getResponseFromHttpUrl(moviesRequestURL);

                    MovieDetail[] allMovies = NetworkUtils
                            .getMoviesDetailsFromJson(MainActivity.this, jsonMoviesResponse);

                    return allMovies;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(MovieDetail[] data) {
                mMoviesDetails = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieDetail[]> loader, MovieDetail[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.setNewData(data);
        if(data == null)
            showErrorMessage();
        else
            showMovieDataView();
    }

    @Override
    public void onLoaderReset(Loader<MovieDetail[]> loader) {

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mMoviesRecyclerview.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mMoviesRecyclerview.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
