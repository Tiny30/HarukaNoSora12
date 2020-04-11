package com.iflysse.face;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;

public class FaceInfo {

	public List<GetPersonListResult> personList = new ArrayList<GetPersonListResult>();

	public boolean get_person_list() {
		JSONObject result = null;
		try {

			result = FaceBase.getHttpRequests().infoGetPersonList();
			JSONArray personArr = result.getJSONArray("person");
			for (int i = 0; i < personArr.length(); i++) {
				JSONObject personObj = personArr.getJSONObject(i);
				GetPersonListResult person = new GetPersonListResult();
				person.person_id = personObj.getString("person_id");
				person.person_name = personObj.getString("person_name");
				person.tag = personObj.getString("tag");

				personList.add(person);
			}

		} catch (FaceppParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result != null;
	}

	public class GetPersonListResult {
		public String person_name;
		public String person_id;
		public String tag;
	}
}
