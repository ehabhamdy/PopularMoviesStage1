package com.ehab.popularmoviesstage1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ehab.popularmoviesstage1.NetworkUtilities.NetworkUtils;

import java.net.URL;

/**
 * Created by ehabhamdy on 2/15/17.
 */

public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<MovieDetail[]>{

    public static final int MOVIES_LOADER_ID = 22;

    private static final String ARG_CRIME_ID = "crime_id";

    RecyclerView mMoviesRecyclerview;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    PopularMoviesAdapter mAdapter;

    public static MoviesFragment newInstance(){
        Bundle args = new Bundle();
        //args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putString(ARG_CRIME_ID, "sdfds");
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Retreiving passed data from the fragment arguments
        //UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies, container, false);

        mMoviesRecyclerview = (RecyclerView) v.findViewById(R.id.movies_recyclerview);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mMoviesRecyclerview.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerview.setHasFixedSize(true);

        mAdapter = new PopularMoviesAdapter(getContext());

        mMoviesRecyclerview.setAdapter(mAdapter);

        mLoadingIndicator = (ProgressBar) v.findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) v.findViewById(R.id.tv_error_message_display);

        int loaderId = MOVIES_LOADER_ID;

        LoaderManager.LoaderCallbacks<MovieDetail[]> callback = this;

        // We created bundle to pass extra data through initloader that can be accessed inside onCreateLoader
        Bundle bundleForLoader = null;
        getActivity().getSupportLoaderManager().initLoader(loaderId, bundleForLoader, callback);

        return v;
    }

    @Override
    public Loader<MovieDetail[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<MovieDetail[]>(getContext()) {
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
                            .getMoviesDetailsFromJson((MainActivity) getActivity(), jsonMoviesResponse);

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
