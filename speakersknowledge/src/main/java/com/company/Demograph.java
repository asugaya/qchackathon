package com.company;

import java.util.Iterator;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Demograph {
	
	public int id;

	public static final int AGE_13_18 = 0;
	public static final int AGE_18_24 = 1;
	public static final int AGE_25_34 = 2;
	public static final int AGE_35_44 = 3;
	public static final int AGE_45_54 = 4;
	public static final int AGE_55_UP = 5;
	public int age;

	public static final int GENDER_MALE = 0;
	public static final int GENDER_FEMALE = 1;
	public int gender;

	public static final int EDU_NO_COLLEGE = 0;
	public static final int EDU_COLLEGE = 1;
	public static final int EDU_GRAD_SCHOOL = 2;
	public int education;

	public static final int ETH_AFRICAN_AMERICAN = 0;
	public static final int ETH_ASIAN = 1;
	public static final int ETH_CAUCASIAN = 2;
	public static final int ETH_HISPANIC = 3;
	public static final int ETH_OTHER = 4;
	public int ethnicity;

	public static final int INCOME_0K_30K = 0;
	public static final int INCOME_30K_60K = 1;
	public static final int INCOME_60K_100K = 2;
	public static final int INCOME_100K_PLUS = 3;
	public int income;

	public boolean kids;

	Vector<String> interests = new Vector<String>();

	public JSONObject getJSONObject() {

		JSONObject retval = new JSONObject();
		try {
			retval.put("id", id);
			retval.put("age", age);
			retval.put("gender", gender);
			retval.put("education", education);
			retval.put("ethnicity", ethnicity);
			retval.put("income", income);
			retval.put("interests", interests);
			retval.put("kids", kids);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}
	
	public static Demograph fromJSON(JSONObject jObject) {

		Demograph newDemograph = new Demograph();
		jObject.keys();
		Iterator<?> keys = jObject.keys();
		try {
		while(keys.hasNext()){
			String key = (String)keys.next(); 
			//HACK
			if (key.equals("age")){
				newDemograph.age = jObject.getInt(key);
			}else if(key.equals("gender")){
				newDemograph.gender = jObject.getInt(key);
			}else if(key.equals("education")){
				newDemograph.education = jObject.getInt(key);
			}else if(key.equals("ethnicity")){
				newDemograph.ethnicity = jObject.getInt(key);
			}else if(key.equals("income")){
				newDemograph.income = jObject.getInt(key);
			}else if(key.equals("interests")){
				JSONArray interestarray = jObject.getJSONArray(key);
				for (int i = 0; i < interestarray.length(); ++i) {
					newDemograph.interests.add(interestarray.getString(i));
				}
			}else if(key.equals("kids")){
				newDemograph.kids = jObject.getBoolean(key);
			}
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return newDemograph;
	}


}
