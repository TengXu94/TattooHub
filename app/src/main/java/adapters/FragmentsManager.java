package adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.CategoriesFragment;
import fragments.HomeFragment;

/**
 * Created by root on 15.03.18.
 */

public class FragmentsManager extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 5;
    private Context context;

    public FragmentsManager(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public FragmentsManager(FragmentManager fragmentManager, Context c) {
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
            case 0: // Fragment # 0 - This will show HomeFragment
                return HomeFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show HomeFragment different title
                return HomeFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show CategoriesFragment
                return CategoriesFragment.newInstance(2, "Page # 3");
            case 3: // Fragment # 1 - This will show CategoriesFragment
                return CategoriesFragment.newInstance(3, "Page # 3");
            case 4: // Fragment # 1 - This will show CategoriesFragment
                return CategoriesFragment.newInstance(4, "Page # 3");
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

