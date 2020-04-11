package com.iflysse.face;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;

public class FacePerson {
	public CreateResult createRst = new CreateResult();
	public DeleteResult deleteRst = new DeleteResult();
	public AddFaceResult addFaceRst = new AddFaceResult();
	public RemoveFaceResult removeFaceRst = new RemoveFaceResult();
	public SetInfoResult setInfoRst = new SetInfoResult();
	public GetInfoResult getInfoRst = new GetInfoResult();

	public boolean create(String person_name, String face_id, String tag,
			String group_id_name, boolean is_group_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			if (person_name != null && !person_name.equals("")) {
				parameters.setPersonName(person_name);
			}
			if (face_id != null && !face_id.equals("")) {
				parameters.setFaceId(face_id);
			}
			if (tag != null && !tag.equals("")) {
				parameters.setTag(tag);
			}
			if (group_id_name != null && !group_id_name.equals("")) {
				if (is_group_id) {
					parameters.setGroupId(group_id_name);
				} else {
					parameters.setGroupName(group_id_name);
				}
			}
			result = FaceBase.getHttpRequests().personCreate(parameters);

			createRst.added_group = result.getInt("added_group");
			createRst.added_face = result.getInt("added_face");
			createRst.tag = result.getString("tag");
			createRst.person_id = result.getString("person_id");
			createRst.person_name = result.getString("person_name");

			FaceTrain.verify(createRst.person_id);

		return result != null;
	}

	public boolean create(String person_name, String face_id, String tag,
			String group_id_name) throws FaceppParseException, JSONException {
		return create(person_name, face_id, tag, group_id_name, true);
	}

	public boolean delete(String person_id_name, boolean is_id) throws FaceppParseException, JSONException {
		JSONObject result = null;

	
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			result = FaceBase.getHttpRequests().personDelete(parameters);

			deleteRst.deleted = result.getInt("deleted");
			deleteRst.success = result.getBoolean("success");


		return result != null;
	}

	public boolean delete(String person_id_name) throws FaceppParseException, JSONException {
		return delete(person_id_name, true);
	}

	public boolean add_face(String person_id_name, boolean is_id, String face_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			parameters.setFaceId(face_id);
			result = FaceBase.getHttpRequests().personAddFace(parameters);

			addFaceRst.added = result.getInt("added");
			addFaceRst.success = result.getBoolean("success");


		return result != null;
	}

	public boolean remove_face(String person_id_name, boolean is_id,
			String face_id) throws FaceppParseException, JSONException {
		JSONObject result = null;

		
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			parameters.setFaceId(face_id);
			result = FaceBase.getHttpRequests().personRemoveFace(parameters);

			removeFaceRst.removed = result.getInt("removed");
			removeFaceRst.success = result.getBoolean("success");


		return result != null;
	}

	public boolean set_info(String person_id_name, boolean is_id, String name,
			String tag) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			if (name != null && !name.equals("")) {
				parameters.setName(name);
			}
			if (tag != null && !tag.equals("")) {
				parameters.setTag(tag);
			}
			result = FaceBase.getHttpRequests().personSetInfo(parameters);

			setInfoRst.person_id = result.getString("person_id");
			setInfoRst.person_name = result.getString("person_name");
			setInfoRst.tag = result.getString("tag");

		return result != null;
	}

	public boolean get_info(String person_id_name, boolean is_id) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			result = FaceBase.getHttpRequests().personGetInfo(parameters);

			getInfoRst.person_id = result.getString("person_id");
			getInfoRst.person_name = result.getString("person_name");
			getInfoRst.tag = result.getString("tag");

			JSONArray faceArr = result.getJSONArray("face");
			for (int i = 0; i < faceArr.length(); i++) {
				JSONObject faceObj = faceArr.getJSONObject(i);
				GetInfoFace face = new GetInfoFace();
				face.face_id = faceObj.getString("face_id");
				face.tag = faceObj.getString("tag");
				getInfoRst.faces.add(face);
			}
			JSONArray groupArr = result.getJSONArray("group");
			for (int i = 0; i < groupArr.length(); i++) {
				JSONObject groupObj = groupArr.getJSONObject(i);
				GetInfoGroup group = new GetInfoGroup();
				group.group_id = groupObj.getString("group_id");
				group.group_name = groupObj.getString("group_name");
				group.tag = groupObj.getString("tag");
				getInfoRst.groups.add(group);
			}

		return result != null;
	}

	/**
	 * 各种操作的结果类
	 * 
	 * @author Administrator
	 * 
	 */
	public class CreateResult {
		public int added_group; // 成功被加入的group数量
		public int added_face; // 成功加入的face数量
		public String tag; // person相关的tag
		public String person_name; // 相应person的name
		public String person_id; // 相应person的id
	}

	public class DeleteResult {
		public int deleted; // 成功删除的Person数量
		public boolean success; // 表示操作是否成功
	}

	public class AddFaceResult {
		public int added; // 成功加入的face数量
		public boolean success; // 表示操作是否成功
	}

	public class RemoveFaceResult {
		public int removed; // 成功删除的face数量
		public boolean success; // 表示操作是否成功
	}

	public class SetInfoResult {
		public String person_id;
		public String person_name;
		public String tag;
	}

	public class GetInfoResult {

		public String person_id;
		public String person_name;
		public String tag;
		public List<GetInfoFace> faces = new ArrayList<GetInfoFace>();
		public List<GetInfoGroup> groups = new ArrayList<GetInfoGroup>();

	}

	public class GetInfoFace {
		public String face_id;
		public String tag;
	}

	public class GetInfoGroup {
		public String group_id;
		public String group_name;
		public String tag;
	}
}
