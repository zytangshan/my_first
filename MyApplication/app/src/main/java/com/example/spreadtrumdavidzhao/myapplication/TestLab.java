package com.example.spreadtrumdavidzhao.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-7.
 */
public class TestLab {

    private static final String TAG = "TestLab";
    private static final String FILENAME = "tests.json";

    private ArrayList<Test> mTests;
    private TestIntentJSONSerializer mSerializer;
    private Context context;

    private static TestLab ourInstance;

    public static TestLab getInstance(Context c) {

        if (ourInstance == null) {
            ourInstance = new TestLab(c.getApplicationContext());
        }

        return ourInstance;
    }

    public ArrayList<Test> getmTests() {

        return mTests;
    }

    public Test getTest(UUID id) {
        for (Test c : mTests) {
            if (c.getM().equals(id)) {
                return c ;
            }
        }
        return null ;
    }

    private TestLab(Context con) {
        context = con;
        //mTests = new ArrayList<Test>();
        mSerializer = new TestIntentJSONSerializer(context,FILENAME);
        try {
            mTests = mSerializer.loadTests();
        } catch (Exception e) {
            mTests = new ArrayList<Test>();
            Log.e(TAG,"Error Loading",e);
        }
    }

    public void addTest(Test test){

        mTests.add(test);
    }
    public boolean saveTests(){
        try {

            mSerializer.saveTests(mTests);
            Log.d(TAG,"test save to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG,"fail to save file");
            /*Dialog dialog = new AlertDialog.Builder(this.context)
                    .setTitle("ERROR")
                    .setMessage("fail to save")
                    .create();
            dialog.show();*/
            e.printStackTrace();

            return false;
        }
    }

    public void deleteTest(Test test) {
        mTests.remove(test);
    }
}
