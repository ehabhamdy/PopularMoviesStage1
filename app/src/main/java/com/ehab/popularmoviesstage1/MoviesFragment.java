package com.ehab.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by ehabhamdy on 2/15/17.
 */

public class MoviesFragment extends Fragment{

    private static final String ARG_CRIME_ID = "crime_id";

    public static MoviesFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Retreiving passed data from the fragment arguments
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
    }
}
