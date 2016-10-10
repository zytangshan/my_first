package com.example.spreadtrumdavidzhao.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-1.
 */
public class Test {

    private UUID m;
    private String title;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mPerson;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_PERSON = "preson";

    public Test() {
        this.m = UUID.randomUUID();
        mDate = new Date();
    }

    public Test(JSONObject json) throws JSONException{
        m = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)) {
            title = json.getString(JSON_TITLE);
        }
        mSolved = json.getBoolean(JSON_SOLVED);
        mDate = new Date(json.getString(JSON_DATE));
        if (json.has(JSON_PHOTO)) {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if (json.has(JSON_PERSON)) {
            mPerson = json.getString(JSON_PERSON);
        }
    }

    public Photo getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public UUID getM() {

        return m;
    }

    public boolean ismSolved() {

        return mSolved;
    }

    public void setmSolved(boolean mSolved) {

        this.mSolved = mSolved;
    }

    public Date getmDate() {

        return mDate;
    }

    public void setmDate(Date mDate) {

        this.mDate = mDate;
    }

    @Override
    public String toString() {

        return title;
    }

    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID,getM().toString());
        json.put(JSON_DATE,getmDate().toString());
        json.put(JSON_TITLE,getTitle());
        json.put(JSON_SOLVED,ismSolved());
        //mPhoto.toJson();
        if (mPhoto != null) {
            json.put(JSON_PHOTO,mPhoto.toJson());
        }
        json.put(JSON_PERSON,mPerson);

        return json;
    }

    public String getmPerson() {
        return mPerson;
    }

    public void setmPerson(String mPerson) {
        this.mPerson = mPerson;
    }
}
