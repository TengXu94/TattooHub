package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragments.CategoriesFragment;
import fragments.HomeFragment;
import fragments.ProfileFragment;

/**
 * Created by root on 15.03.18.
 */

public class FragmentsManager extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;

    public FragmentsManager(FragmentManager fragmentManager) {
        super(fragmentManager);
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
            case 0: // Fragment # 0 - This will show ProfileFragment
                return ProfileFragment.newInstance();
            case 1: // Fragment #  1 - This will show HomeFragment
                return HomeFragment.newInstance();
            case 2: // Fragment # 2 - This will show CategoriesFragment
                return CategoriesFragment.newInstance();
            default:
                return null;
        }
    }
}

