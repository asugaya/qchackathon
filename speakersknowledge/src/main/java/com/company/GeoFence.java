package com.company;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeoFence {

	public int id;
	public double latitude;
	public double longitude;
	public int radius;
	private HashMap<Integer, Demograph> demographics;
	private int currentDemoIndex;

	public GeoFence() {
		demographics = new HashMap<Integer, Demograph>();
		currentDemoIndex = 0;
	}

	public JSONObject getJSONObject(boolean includeDemograph) {
		JSONObject retval = new JSONObject();
		try {
			retval.put("id", id);
			retval.put("latitude", latitude);
			retval.put("longitude", longitude);
			retval.put("radius", radius);
			JSONArray demoArray = new JSONArray();
			if (includeDemograph) {
				for (Demograph demo : demographics.values()) {
					demoArray.put(demo.getJSONObject());
				}
			}
			retval.put("demographics", demoArray);
		} catch (JSONException e) {

		}
		return retval;
	}

	public int addDemograph(int age, int gender, int education, int ethnicity,
			int income, boolean kids) {
		Demograph newDemo = new Demograph();
		newDemo.age = age;
		newDemo.gender = gender;
		newDemo.education = education;
		newDemo.ethnicity = ethnicity;
		newDemo.income = income;
		newDemo.kids = kids;
		demographics.put(currentDemoIndex, newDemo);
		currentDemoIndex++;
		return currentDemoIndex - 1;
	}
	
	public Demograph getDemograph(int id) {
		return demographics.get(id);
	}
	
	public void removeDemograph(int id) {
		demographics.remove(id);
	}

	public static GeoFence fromJSON(JSONObject jObject) {
		GeoFence fence = new GeoFence();
		try {
			fence.latitude = jObject.getDouble("latitude");
			fence.longitude = jObject.getDouble("longitude");
			fence.radius = jObject.getInt("radius");
			fence.id = jObject.getInt("id");
			JSONArray demographArray = jObject.getJSONArray("demographics");
			for (int i = 0; i < demographArray.length(); ++i)
			{
				JSONObject demographObject = demographArray.getJSONObject(i);
				fence.addDemograph(demographObject.getInt("age"),
						demographObject.getInt("gender"),
						demographObject.getInt("education"),
						demographObject.getInt("ethnicity"),
						demographObject.getInt("income"),
						demographObject.getBoolean("kids"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fence;
	}
}
