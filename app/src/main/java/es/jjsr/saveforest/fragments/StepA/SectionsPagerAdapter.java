package es.jjsr.saveforest.fragments.StepA;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import es.jjsr.saveforest.R;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        if(position == 0){
            return Step1Fragment.newInstance();
        }else {
            return Step2Fragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = context.getResources();
        String [] tabs = res.getStringArray(R.array.tabs_name);
        return tabs[position];
    }
}
