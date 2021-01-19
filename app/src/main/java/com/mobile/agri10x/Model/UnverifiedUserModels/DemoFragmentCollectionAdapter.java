package com.mobile.agri10x.Model.UnverifiedUserModels;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class DemoFragmentCollectionAdapter extends FragmentStatePagerAdapter {

    public DemoFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment_User fu = new Fragment_User();
        Bundle bundle = new Bundle();
        i = i+1;
        bundle.putString("message",""+i);
        fu.setArguments(bundle);
        return fu;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
