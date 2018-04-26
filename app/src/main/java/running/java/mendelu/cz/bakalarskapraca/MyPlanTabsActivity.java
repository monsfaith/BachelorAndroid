package running.java.mendelu.cz.bakalarskapraca;


import android.app.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import running.java.mendelu.cz.bakalarskapraca.db.Exam;
import running.java.mendelu.cz.bakalarskapraca.db.ExamMainRepository;
import running.java.mendelu.cz.bakalarskapraca.db.HabitAdapter;


public class MyPlanTabsActivity extends AppCompatActivity {

    private TabsPageAdapter tabsPageAdapter;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan_tabs);

        android.app.FragmentManager fm = getFragmentManager();
        tabsPageAdapter = new TabsPageAdapter(fm);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FragmentInterface fragmentInt = (FragmentInterface) tabsPageAdapter.instantiateItem(viewPager, position);
                if (fragmentInt != null) {
                    fragmentInt.fragmentSwitchToVisible();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        //tabLayout.setOnTabSelectedListener();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewPager(ViewPager viewPager){
        TabsPageAdapter tabsPageAdapter = new TabsPageAdapter(getFragmentManager());
        MyPlanTab1Fragment myPlanTab = new MyPlanTab1Fragment();
        tabsPageAdapter.addFragment(myPlanTab,"Moje plány");

        MyActivitiesTab2Fragment myActivitiesTab2Fragment = new MyActivitiesTab2Fragment();
        //myActivitiesTab2Fragment.setListeners(myPlanTab);
        myActivitiesTab2Fragment.setMyPlanTabFragment(myPlanTab);
        tabsPageAdapter.addFragment(myActivitiesTab2Fragment, "Aktivity");

        viewPager.setAdapter(tabsPageAdapter);
    }


}
