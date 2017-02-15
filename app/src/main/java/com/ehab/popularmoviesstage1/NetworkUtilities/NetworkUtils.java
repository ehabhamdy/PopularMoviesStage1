package com.ehab.popularmoviesstage1.NetworkUtilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.ehab.popularmoviesstage1.MainActivity;
import com.ehab.popularmoviesstage1.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ehabhamdy on 2/13/17.
 */

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_TOP = "http://api.themoviedb.org/3/movie/top_rated";

    public static final String API_KEY = "f1c9b64bd3fdeef62ed1248d3242eb8c";

    final static String API_KEY_PARAM = "api_key";

    private static final String OWN_RESULTS = "results";


    // the url will be (http://api.themoviedb.org/3/movie/popular?api_key=f1c9b64bd3fdeef62ed1248d3242eb8c)
    public static URL buildUrl( /*String locationQ */int loader_id) {
        Uri builtUri = null;
        if(loader_id == 1) {
            builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        }else{
            builtUri = Uri.parse(BASE_URL_TOP).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        }

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // read the http request into a string using okhttp library
    // you need to add okhttp library in the gradle
    public static String getResponseFromHttpUrl_v2(URL url)throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static boolean isOnline(Context mContext)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static MovieDetail[] getMoviesDetailsFromJson(MainActivity mainActivity, String jsonMoviesResponse) throws JSONException {
        JSONObject moviesJson = new JSONObject(jsonMoviesResponse);
        JSONArray moviesArray = moviesJson.getJSONArray(OWN_RESULTS);
        MovieDetail[] parsedMovieData = new MovieDetail[moviesArray.length()];
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            String posterPath = movie.getString("poster_path");
            int id = movie.getInt("id");
            MovieDetail detail = new MovieDetail();
            detail.setPosterPath(posterPath);
            detail.setId(id);
            parsedMovieData[i] = detail;
            Log.d("Ehaaaab", detail.getPosterPath());

        }
        return parsedMovieData;
    }
}
