package com.example.spreadtrumdavidzhao.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-7.
 */
public class TestListFragment extends ListFragment {
    private ArrayList<Test> mTests;
    public static TestAdapter testAdapter;
    //public static boolean sub_show;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("List");
        mTests = TestLab.getInstance(getActivity()).getmTests();
        //ArrayAdapter<Test> adapter =
        //        new ArrayAdapter<Test>(getActivity(),android.R.layout.simple_list_item_1,mTests);
        TestAdapter adapter = new TestAdapter(mTests);
        setListAdapter(adapter);
        testAdapter = (TestAdapter) getListAdapter();
        //setRetainInstance(true);
        //sub_show =false;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Test t = (Test) getListAdapter().getItem(position);
        Test t = ((TestAdapter)getListAdapter()).getItem(position);
        //Dialog dialog = new AlertDialog.Builder(getActivity()).create();
        //dialog.setTitle(t.getTitle().toString());
        //dialog.show();
        Intent intent = new Intent(getActivity(),TestPagerActivity.class);
        intent.putExtra(TestFragment.EXTRA_TEST_ID,t.getM());
        startActivity(intent);
    }

    public class TestAdapter extends ArrayAdapter<Test> {

        public TestAdapter (ArrayList<Test> mTests) {

            super(getActivity(),0,mTests);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_test,null);
            }

            Test t = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.test_list_item_title);
            titleTextView.setText(t.getTitle());
            TextView dataTextView =
                    (TextView)convertView.findViewById(R.id.test_list_item_data);
            dataTextView.setText(t.getmDate().toString());
            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.test_list_item_checkbox);
            solvedCheckBox.setChecked(t.ismSolved());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TestAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //TextView textView;

        //textView = (TextView) view.findViewById(R.id.empty_text);
        //ListView listView = (ListView)view.findViewById(android.R.id.list);
        final ListView listView = (ListView) view.findViewById(android.R.id.list);
        View view1 = inflater.inflate(R.layout.mptylist,null,false);
        ((ViewGroup)listView.getParent()).addView(view1);
        listView.setEmptyView(view1);

        Button addButton = (Button)view1.findViewById(R.id.button_add_data);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test test = new Test();
                TestLab.getInstance(getActivity()).addTest(test);
                Intent intent = new Intent(getActivity(),TestPagerActivity.class);
                intent.putExtra(TestFragment.EXTRA_TEST_ID,test.getM());
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            /*if (sub_show) {
                getActivity().getActionBar().setSubtitle("subtitle");


            }*/
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            getActivity().registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.test_list_item_context,menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return true;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_test:
                            TestLab testLab = TestLab.getInstance(getActivity());
                            for (int i = testAdapter.getCount() - 1;i >= 0;i--) {
                                Test t = ((TestAdapter)getListAdapter()).getItem(i);
                                if (getListView().isItemChecked(i)) {
                                    Log.d("david"," checked i= " + i + t.getmDate().toString());
                                    testLab.deleteTest(t);
                                }else {
                                    Log.d("david"," not checked i= " + i + t.getmDate().toString());
                                }
                            }
                            mode.finish();
                            testAdapter.notifyDataSetChanged();
                            return true;
                        case R.id.menu_item_add_test:
                            Test addtest = new Test();
                            TestLab.getInstance(getActivity()).addTest(addtest);
                            Intent intent = new Intent(getActivity(),TestPagerActivity.class);
                            intent.putExtra(TestFragment.EXTRA_TEST_ID,addtest.getM());
                            startActivity(intent);
                            return true;
                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
