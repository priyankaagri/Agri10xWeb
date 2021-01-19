package com.mobile.agri10x.Model.UnverifiedUserModels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.mobile.agri10x.R;

public class Fragment_User extends Fragment {

    //the recyclerview
    private ImageView imageView;
    public Fragment_User() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fragment__user, container, false);
        imageView = v.findViewById(R.id.imageViewFragment);
        String count = getArguments().getString("message");
        if(count.equals("1")) {
            imageView.setImageResource(R.drawable.farmer1);

        }
        if(count.equals("2")) {
            imageView.setImageResource(R.drawable.farmer2);

        }
        return v;
    }


}
