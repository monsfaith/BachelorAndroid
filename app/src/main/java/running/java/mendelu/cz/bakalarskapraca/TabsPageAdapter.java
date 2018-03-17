package running.java.mendelu.cz.bakalarskapraca;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monika on 12.02.2018.
 */

class TabsPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentsNames = new ArrayList<>();

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsNames.get(position);
    }

    public TabsPageAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String name){
        fragments.add(fragment);
        fragmentsNames.add(name);
    }
}
