package com.example.spreadtrumdavidzhao.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-7.
 */
public class TestPagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<Test> mTests;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        if (getTitle()==null) {
            setTitle("add new");
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB
                && NavUtils.getParentActivityName(this)!=null) {
            //getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mTests = TestLab.getInstance(this).getmTests();
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                //return null;
                Test test = mTests.get(position);
                return TestFragment.newInstance(test.getM());
            }

            @Override
            public int getCount() {
                //return 0;
                return mTests.size();
            }
        });

        UUID uuid = (UUID)getIntent().getSerializableExtra(TestFragment.EXTRA_TEST_ID);
        for (int i = 0;i < mTests.size();i++) {
            if (mTests.get(i).getM().equals(uuid)) {
                mViewPager.setCurrentItem(i);
                setTitle(mTests.get(i).getTitle());
                break;
            }
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Test test = mTests.get(position);
                setTitle(test.getTitle());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                if (NavUtils.getParentActivityName(this)!=null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
