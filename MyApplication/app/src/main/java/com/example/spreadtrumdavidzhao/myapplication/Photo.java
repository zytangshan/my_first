package com.example.spreadtrumdavidzhao.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-13.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFilename;

    public Photo(String mFilename) {
        this.mFilename = mFilename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_FILENAME,mFilename);

        return jsonObject;
    }

    public String getFilename() {
        return mFilename;
    }
}
