package com.company;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoFence {

	public GeoFence() {
		demographics = new HashMap<String, Demograph>();
	}

	public double latitude;
	public double longitude;
	public int radius;
	public HashMap<String, Demograph> demographics;

	public JSONObject getJSONObject() {
		JSONObject retval = new JSONObject();
		try {
			retval.put("latitude", latitude);
			retval.put("longitude", longitude);
			retval.put("radius", radius);
			JSONArray demoArray = new JSONArray();
			for (Demograph demo : demographics.values()) {
				demoArray.put(demo.getJSONObject());
			}
			retval.put("demographics", demoArray);
		} catch (JSONException e) {

		}
		return retval;
	}

}
