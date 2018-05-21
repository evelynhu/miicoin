package io.mii.coin.features;


import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import io.mii.coin.R;
import io.mii.coin.features.main.FavoriteFragment;
import io.mii.coin.features.main.MainFragment;

public class MiicoinActivity  extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, FavoriteFragment.OnFragmentInteractionListener {

    private static final int TABS_COUNT = 2;
    public static final int ALL_TAB_POSITION = 0;
    private static final int FAVORITA_TAB_POSITION = 1;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(ALL_TAB_POSITION).setText("ALL");
        mTabLayout.getTabAt(FAVORITA_TAB_POSITION).setText("FAVORITE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFavoriteFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.placeholder_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == ALL_TAB_POSITION) {
                return MainFragment.newInstance("", "");
            } else if (position == FAVORITA_TAB_POSITION) {
                return FavoriteFragment.newInstance("", "");
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Favorite";
            }
            return null;
        }
    }
}
