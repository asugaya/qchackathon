package com.company;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

public class Demograph {

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
	public static final int ETH_ASIAN = 0;
	public static final int ETH_CAUCASIAN = 0;
	public static final int ETH_HISPANIC = 0;
	public static final int ETH_OTHER = 0;
	public int ethnicity;

	public static final int INCOME_0K_30K = 0;
	public static final int INCOME_30K_60K = 1;
	public static final int INCOME_60K_100K = 2;
	public static final int INCOME_100K_PLUS = 3;
	public int income;

	public boolean kids;

	Vector<String> interests;

	public JSONObject getJSONObject() {

		JSONObject retval = new JSONObject();
		try {
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

}
