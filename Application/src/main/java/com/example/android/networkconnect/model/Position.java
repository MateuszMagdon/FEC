package com.example.android.networkconnect.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mateu_000 on 2015-04-26.
 */
public class Position {
    public float Latitude;
    public float Longitude;

    public Position(float longitude, float latitude) {
        Longitude = longitude;
        Latitude = latitude;
    }

    public JSONObject toJSON(){
        JSONObject object = new JSONObject();
        try {
            object.put("Latitude", this.Latitude);
            object.put("Longitude", this.Longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
