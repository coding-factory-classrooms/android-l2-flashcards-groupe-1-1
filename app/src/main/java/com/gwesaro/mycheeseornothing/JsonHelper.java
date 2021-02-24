package com.gwesaro.mycheeseornothing;

import android.content.Context;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonHelper {

    public String loadJSONFromRes(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.questions);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public ArrayList<String> jsonToArrayList(JSONArray jArray){
        ArrayList<String> listdata = new ArrayList<String>();
        if (jArray != null) {
            try {
                for (int i = 0; i < jArray.length(); i++) {
                    listdata.add(jArray.getString(i));
                }
            }catch (Exception e){
            }
        }
        return listdata;
    }
}
