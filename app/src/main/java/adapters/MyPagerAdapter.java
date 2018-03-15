package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import xu_aaabeck.tattoohub.FirstFragment;
import xu_aaabeck.tattoohub.R;
import xu_aaabeck.tattoohub.SecondFragment;

/**
 * Created by root on 15.03.18.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 5;
    private Context context;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public MyPagerAdapter(FragmentManager fragmentManager, Context c) {
        super(fragmentManager);
        context = c;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return FirstFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return FirstFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return SecondFragment.newInstance(2, "Page # 3");
            case 3: // Fragment # 1 - This will show SecondFragment
                return SecondFragment.newInstance(3, "Page # 3");
            case 4: // Fragment # 1 - This will show SecondFragment
                return SecondFragment.newInstance(4, "Page # 3");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }



}

