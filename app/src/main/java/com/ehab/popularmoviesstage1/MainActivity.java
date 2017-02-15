package com.ehab.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements PopularMoviesAdapter.ListItemClickListener{

    public static final int MOVIES_LOADER_ID = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = MoviesFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    @Override
    public void onListItemClick(MovieDetail movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_PACKAGE_NAME, movie);
        startActivity(intent);
    }


}
