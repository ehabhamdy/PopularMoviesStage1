package com.ehab.popularmoviesstage1;

import android.os.AsyncTask;

import com.ehab.popularmoviesstage1.NetworkUtilities.NetworkUtils;
import com.ehab.popularmoviesstage1.model.MovieDetail;

import java.net.URL;

/**
 * Created by ehabhamdy on 2/15/17.
 */

public class FetchDetailsTask extends AsyncTask<Integer, Void, MovieDetail>{

    @Override
    protected MovieDetail doInBackground(Integer... inputs) {
        int id = inputs[0];

        try {
            URL movieDetailsRequestURL = NetworkUtils.buildUrl(id);

            String jsonMovieDetailResponse = NetworkUtils
                    .getResponseFromHttpUrl_details(movieDetailsRequestURL);

            MovieDetail details = NetworkUtils
                    .getSingleMovieDetailsFromJson(jsonMovieDetailResponse);

            return details;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
