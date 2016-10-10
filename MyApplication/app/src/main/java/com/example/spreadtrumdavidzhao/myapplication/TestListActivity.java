package com.example.spreadtrumdavidzhao.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Switch;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-7.
 */


public class TestListActivity extends SingleFragmentActivity {
    public static boolean sub_show = false;
    @Override
    protected Fragment createFragment() {
        if (sub_show) {
            getSupportActionBar().setSubtitle("subtitle");
        }
        return new TestListFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_test_menu,menu);
        if (sub_show) {
            getSupportActionBar().setSubtitle("subtitle");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add :
                Test test = new Test();
                TestLab.getInstance(this).addTest(test);
                Intent intent = new Intent(TestListActivity.this,TestPagerActivity.class);
                intent.putExtra(TestFragment.EXTRA_TEST_ID,test.getM());
                startActivity(intent);
                return true;
            case R.id.menu_show_subtitle:
                if (getSupportActionBar().getSubtitle() == null) {
                    getSupportActionBar().setSubtitle("subtitle");
                    item.setTitle("subtitle");
                    sub_show = true;
                }else {
                    getSupportActionBar().setSubtitle(null);
                    item.setTitle(null);
                    sub_show = false;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.test_list_item_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int position = info.position;
        Test test =TestListFragment.testAdapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_test:
                TestLab.getInstance(this).deleteTest(test);
                TestListFragment.testAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_add_test:
                /*TestLab.getInstance(this).addTest(test);
                TestListFragment.testAdapter.notifyDataSetChanged();*/
                Test addtest = new Test();
                TestLab.getInstance(this).addTest(addtest);
                Intent intent = new Intent(TestListActivity.this,TestPagerActivity.class);
                intent.putExtra(TestFragment.EXTRA_TEST_ID,addtest.getM());
                startActivity(intent);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.two_pane;
    }
}
