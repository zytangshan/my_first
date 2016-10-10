package com.example.spreadtrumdavidzhao.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-1.
 */
public class TestActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag);


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragContainer);
        if (fragment == null) {
            //fragment = new TestFragment();
            UUID uuid = (UUID) getIntent().getSerializableExtra(TestFragment.EXTRA_TEST_ID);
            fragment = TestFragment.newInstance(uuid);
            fm.beginTransaction().add(R.id.fragContainer, fragment).commit();
        }

    }
}

