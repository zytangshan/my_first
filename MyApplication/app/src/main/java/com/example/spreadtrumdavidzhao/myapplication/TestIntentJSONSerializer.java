package com.example.spreadtrumdavidzhao.myapplication;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-9.
 */
public class TestIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public TestIntentJSONSerializer(Context mContext, String mFilename) {
        this.mContext = mContext;
        this.mFilename = mFilename;
    }

    public ArrayList<Test> loadTests() throws IOException,JSONException {
        ArrayList<Test> tests = new ArrayList<Test>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            //Parse json
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();

            for (int i = 0; i < array.length(); i++) {
                tests.add(new Test(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                reader.close();
            }
        }
        return tests;
    }

    public void saveTests (ArrayList<Test> test) throws JSONException,IOException{

            JSONArray array = new JSONArray();
            for (Test t :test) {
                array.put(t.toJson());
            }
            Writer writer = null;
            OutputStream out = null;
        try {
            out = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if (writer!=null) {
                writer.close();
            }
            if (out !=null) {
                out.close();
            }
        }
    }
}
