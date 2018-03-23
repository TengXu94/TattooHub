package adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import fragments.CategoriesFragment;
import fragments.HomeFragment;
import fragments.ProfileFragment;

/**
 * Created by root on 15.03.18.
 */

public class FragmentsManager extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;
    private Fragment mCurrentFragment;
    private int position;
    private Fragment profileFragment = new ProfileFragment();
    private Fragment homeFragment = new HomeFragment();
    private Fragment categoriesFragment = new CategoriesFragment();

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
                return profileFragment;
            case 1: // Fragment #  1 - This will show HomeFragment
                return homeFragment;
            case 2: // Fragment # 2 - This will show CategoriesFragment
                return categoriesFragment;
            default:
                return null;
        }
    }
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public int getPosition(){
        return position;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
            position = position;
        }
        super.setPrimaryItem(container, position, object);
    }

}

