package com.ehab.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetail[]>,PopularMoviesAdapter.ListItemClickListener{

    RecyclerView mMoviesRecyclerview;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRecyclerview = (RecyclerView) findViewById(R.id.movies_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMoviesRecyclerview.setLayoutManager(linearLayoutManager);
        mMoviesRecyclerview.setHasFixedSize(true);

        PopularMoviesAdapter adapter = new PopularMoviesAdapter(this, this);

        mMoviesRecyclerview.setAdapter(adapter);

    }

    @Override
    public void onListItemClick(MovieDetail movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
    }




    @Override
    public Loader<MovieDetail[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDetail[]>(this) {
            MovieDetail[]  mMoviesDetails;

            @Override
            protected void onStartLoading() {
                if(args != null)
                    deliverResult(mMoviesDetails);
            }

            @Override
            public MovieDetail[] loadInBackground() {
                return new MovieDetail[0];
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

    }

    @Override
    public void onLoaderReset(Loader<MovieDetail[]> loader) {

    }
}
