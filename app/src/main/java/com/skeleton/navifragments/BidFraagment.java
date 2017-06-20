package com.skeleton.navifragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skeleton.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BidFraagment extends Fragment {


    public BidFraagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_bid_fraagment, null);
        return v;
    }

}
