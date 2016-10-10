package com.example.spreadtrumdavidzhao.myapplication;

import android.support.v4.app.Fragment;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-12.
 */
public class TestCameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TestCameraFragment();
    }
}
