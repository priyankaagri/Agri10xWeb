package com.mobile.agri10x;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ScreenSlidePagerActivity extends FragmentActivity {
    private static final int NUM_PAGES = 2;
    private FragmentStateAdapter pagerAdapter;
    private ViewPager2 viewPager;
    TabLayout tabIndicator;
    private CheckBox checkPrivacyPolicy;
    private Button agree,btnnext;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (restorePrefData()) {
//            Intent i = new Intent(getApplicationContext(),LoginActivity.class );
//            startActivity(i);

            Intent  intent = new Intent(ScreenSlidePagerActivity.this,WebPage.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_screen_slide);
        checkPrivacyPolicy=findViewById(R.id.checkP);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        tabIndicator = findViewById(R.id.tab_indicator);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        //tabIndicator.setupWithViewPager(viewPager);
        savePrefsData();
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabIndicator, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int p) {

            }
        });
        tabLayoutMediator.attach();

    }


    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void buttonClick(View view) {
        switch (view.getId()){
            case R.id.agreePrivacyPolicy:
                //if(findViewById(R.id.checkP).isSelected()) {

//                    Intent intent = new Intent(ScreenSlidePagerActivity.this,LoginActivity.class);
//                    savePrefsData();
//                    startActivity(intent);


                Intent  intent = new Intent(ScreenSlidePagerActivity.this,WebPage.class);
                startActivity(intent);
                //}
                //else {
                //    Toast.makeText(this, "Accept terms and conditions.", Toast.LENGTH_SHORT).show();
                //}
                break;
            case R.id.btn_next:
                position = viewPager.getCurrentItem();
                if (position < 1) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
                break;
            case R.id.checkP:
                boolean isChecked = ((CheckBox) view).isChecked();
                /*checkPrivacyPolicy=findViewById(R.id.checkP);
                checkPrivacyPolicy.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            findViewById(R.id.agreePrivacyPolicy).setEnabled(true);
                        }
                        if(!isChecked){
                            findViewById(R.id.agreePrivacyPolicy).setEnabled(false);
                        }
                    }
                });*/
                //if(!isChecked){
                findViewById(R.id.agreePrivacyPolicy).setEnabled(isChecked);
                break;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0: return new ScreenSlidePageFragment();
                case 1: return new ScreenSlidePageFragmentTwo();
                default : return new ScreenSlidePageFragmentTwo();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
