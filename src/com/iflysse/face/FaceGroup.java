package com.iflysse.face;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;

public class FaceGroup {
	public CreateResult createResult = new CreateResult();
	public DeleteResult deleteResult = new DeleteResult();
	public AddResult addResult = new AddResult();
	public RemoveResult removeResult = new RemoveResult();
	public SetInfoResult setInfoResult = new SetInfoResult();
	public GetInfoResult getInfoResult = new GetInfoResult();

	/**
	 * 创建一个group
	 * 
	 * @param group_name
	 * @param person_id_name
	 * @param is_id
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean create(String group_name, String person_id_name,
			boolean is_id) throws FaceppParseException, JSONException {
		JSONObject result = null;

	
			PostParameters parameters = new PostParameters();
			parameters.setGroupName(group_name);
			if (person_id_name != null && !person_id_name.equals("")) {
				if (is_id) {
					parameters.setPersonId(person_id_name);
				} else {
					parameters.setPersonName(person_id_name);
				}
			}
			result = FaceBase.getHttpRequests().groupCreate(parameters);

			createResult.added_person = result.getInt("added_person");
			createResult.group_id = result.getString("group_id");
			createResult.group_name = result.getString("group_name");
			createResult.tag = result.getString("tag");


		return result != null;
	}

	/**
	 * 删除一个/组 Group
	 * 
	 * @param group_name_id
	 * @param is_id
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean delete(String group_name_id, boolean is_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
		
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_name_id);
			} else {
				parameters.setGroupName(group_name_id);
			}
			result = FaceBase.getHttpRequests().groupDelete(parameters);

			deleteResult.deleted = result.getInt("deleted");
			deleteResult.success = result.getBoolean("success");

		return result != null;
	}

	/**
	 * 将一组person加入到一个group中
	 * 
	 * @param group_id_name
	 * @param is_id
	 * @param person_id_name
	 * @param is_person_id
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean add_person(String group_id_name, boolean is_id,
			String person_id_name, boolean is_person_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
		
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_id_name);
			} else {
				parameters.setGroupName(group_id_name);
			}
			if (is_person_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			result = FaceBase.getHttpRequests().groupAddPerson(parameters);

			addResult.added = result.getInt("added");
			addResult.success = result.getBoolean("success");

			FaceTrain.identify(group_id_name, is_id);

		return result != null;
	}

	/**
	 * 从group中删除一组person
	 * 
	 * @param group_id_name
	 * @param is_id
	 * @param person_id_name
	 * @param is_person_id
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean remove_person(String group_id_name, boolean is_id,
			String person_id_name, boolean is_person_id) throws FaceppParseException, JSONException {
		JSONObject result = null;

			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_id_name);
			} else {
				parameters.setGroupName(group_id_name);
			}
			if (is_person_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			result = FaceBase.getHttpRequests().groupRemovePerson(parameters);

			removeResult.removed = result.getInt("removed");
			removeResult.success = result.getBoolean("success");

		return result != null;
	}

	/**
	 * 设置group的name和tag
	 * 
	 * @param group_id_name
	 * @param is_id
	 * @param name
	 * @param tag
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean set_info(String group_id_name, boolean is_id, String name,
			String tag) throws FaceppParseException, JSONException {
		JSONObject result = null;

			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_id_name);
			} else {
				parameters.setGroupName(group_id_name);
			}

			if (name != null && !name.equals("")) {
				parameters.setGroupName(name);
			}
			if (tag != null && !tag.equals("")) {
				parameters.setTag(tag);
			}

			result = FaceBase.getHttpRequests().groupSetInfo(parameters);

			setInfoResult.tag = result.getString("tag");
			setInfoResult.group_id = result.getString("group_id");
			setInfoResult.group_name = result.getString("group_name");

		return result != null;
	}

	/**
	 * 获取group的信息，包括group中的person列表，group的tag等信息
	 * 
	 * @param group_id_name
	 * @param is_id
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean get_info(String group_id_name, boolean is_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_id_name);
			} else {
				parameters.setGroupName(group_id_name);
			}

			result = FaceBase.getHttpRequests().groupGetInfo(parameters);

			getInfoResult.group_id = result.getString("group_id");
			getInfoResult.group_name = result.getString("group_name");
			getInfoResult.tag = result.getString("tag");

			JSONArray personArray = result.getJSONArray("person");
			for (int i = 0; i < personArray.length(); i++) {
				JSONObject personObj = personArray.getJSONObject(i);
				GroupPerson person = new GroupPerson();
				person.person_id = personObj.getString("person_id");
				person.person_name = personObj.getString("person_name");
				person.tag = personObj.getString("tag");
				getInfoResult.persons.add(person);
			}


		return result != null;
	}

	public class CreateResult {
		public int added_person;
		public String tag;
		public String group_name;
		public String group_id;
	}

	public class DeleteResult {
		public int deleted;
		public boolean success;
	}

	public class AddResult {
		public int added;
		public boolean success;
	}

	public class RemoveResult {
		public int removed;
		public boolean success;
	}

	public class SetInfoResult {
		public String tag;
		public String group_name;
		public String group_id;
	}

	public class GetInfoResult {
		public List<GroupPerson> persons = new ArrayList<GroupPerson>();
		public String tag;
		public String group_name;
		public String group_id;
	}

	public class GroupPerson {
		public String person_id;
		public String person_name;
		public String tag;
	}
}
