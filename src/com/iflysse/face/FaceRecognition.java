package com.iflysse.face;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;

public class FaceRecognition {

	public CompareResult compareRst = new CompareResult();
	public VerifyResult verifyRst = new VerifyResult();
	public IdentifyResult identifyRst = new IdentifyResult();
	public SearchResult searchRst = new SearchResult();

	/**
	 * 比较两个面部的相似度
	 * 
	 * @param face_id1
	 *            面部信息id1
	 * @param face_id2
	 *            面部信息id2
	 * @return 是否比较成功
	 */
	public boolean compare(String face_id1, String face_id2) {
		JSONObject result = null;
		try {
			result = FaceBase.getHttpRequests().recognitionCompare(
					new PostParameters().setFaceId1(face_id1).setFaceId2(face_id2));
			// 获取总体相似度
			compareRst.similarity = (float) result.getDouble("similarity");

			// 获取各部位相似度
			JSONObject component_similarity = result.getJSONObject("component_similarity");
			compareRst.similarity_eye = (float) component_similarity.getDouble("eye");
			compareRst.similarity_mouth = (float) component_similarity.getDouble("mouth");
			compareRst.similarity_nose = (float) component_similarity.getDouble("nose");
			compareRst.similarity_eyebrow = (float) component_similarity.getDouble("eyebrow");

		} catch (FaceppParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result != null;
	}

	/**
	 * 判断是否是同一个人
	 * 
	 * @param face_id
	 *            给定的faceId
	 * @param person_id_name
	 *            要判断的人的信息
	 * @param usingID
	 *            使用personId还是personName判断
	 * @return
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean verify(String face_id, String person_id_name, boolean usingID) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			PostParameters parameters = new PostParameters();
			parameters.setFaceId(face_id);
			if (usingID) {
				parameters.setPersonId(person_id_name);
			} else {
				parameters.setPersonName(person_id_name);
			}
			result = FaceBase.getHttpRequests().recognitionVerify(parameters);

			verifyRst.is_same_person = result.getBoolean("is_same_person");
			verifyRst.confidence = (float) result.getDouble("confidence");



		return result != null;
	}

	public boolean verify(String face_id, String person_id_name) throws FaceppParseException, JSONException {
		return verify(face_id, person_id_name, true);
	}

	/**
	 * 对于一个待查询的Face列表（或者对于给定的Image中所有的Face），在一个Group中查询最相似的Person。
	 * 
	 * @param group_id_name
	 *            group的ID或名称
	 * @param is_id
	 *            是否使用ID
	 * @param url_path
	 *            图片路径
	 * @param is_path
	 *            是否是本地路径
	 * @param mode
	 *            模式--oneface或者normal
	 * @param key_face_id
	 *            关键faceid
	 * @return
	 */
	public boolean identify(String group_id_name, boolean is_id, String url_path, boolean is_path, String mode,
			String key_face_id) {
		JSONObject result = null;

		try {
			PostParameters parameters = new PostParameters();
			if (is_id) {
				parameters.setGroupId(group_id_name);
			} else {
				parameters.setGroupName(group_id_name);
			}
			if (is_path) {
				parameters.setImg(new File(url_path));
			} else {
				parameters.setUrl(url_path);
			}
			if (mode != null && !mode.equals("")) {
				parameters.setMode(mode);
			}
			if (key_face_id != null && !key_face_id.equals("")) {
				parameters.setFaceId(key_face_id);
			}
			result = FaceBase.getHttpRequests().recognitionIdentify(parameters);
			JSONArray faces = result.getJSONArray("face");
			for (int i = 0; i < faces.length(); i++) {

				IdentifyFace face = new IdentifyFace();

				JSONObject faceObj = faces.getJSONObject(i);
				JSONArray candidates = faceObj.getJSONArray("candidate");
				for (int j = 0; j < candidates.length(); j++) {
					IdentifyCandidate identifyCandidate = new IdentifyCandidate();
					JSONObject candidateObj = candidates.getJSONObject(j);
					identifyCandidate.confidence = (float) candidateObj.getDouble("confidence");
					identifyCandidate.person_id = candidateObj.getString("person_id");
					identifyCandidate.person_name = candidateObj.getString("person_name");
					identifyCandidate.tag = candidateObj.getString("tag");
					face.identifyCandidate.add(identifyCandidate);
				}
				face.face_id = faceObj.getString("face_id");

				// 获取面部位置信息
				JSONObject facePosition = faceObj.getJSONObject("position");

				JSONObject center = facePosition.getJSONObject("center");
				if (center != null) {
					face.position.center_x = (float) center.getDouble("x");
					face.position.center_y = (float) center.getDouble("y");
				}

				face.position.width = (float) facePosition.getDouble("width");
				face.position.height = (float) facePosition.getDouble("height");

				JSONObject eye_left = facePosition.getJSONObject("eye_left");
				if (eye_left != null) {
					face.position.eye_left_x = (float) eye_left.getDouble("x");
					face.position.eye_left_y = (float) eye_left.getDouble("y");
				}

				JSONObject eye_right = facePosition.getJSONObject("eye_right");
				if (eye_right != null) {
					face.position.eye_right_x = (float) eye_right.getDouble("x");
					face.position.eye_right_y = (float) eye_right.getDouble("y");
				}

				JSONObject mouth_left = facePosition.getJSONObject("mouth_left");
				if (mouth_left != null) {
					face.position.mouth_left_x = (float) mouth_left.getDouble("x");
					face.position.mouth_left_y = (float) mouth_left.getDouble("y");
				}

				JSONObject mouth_right = facePosition.getJSONObject("mouth_right");
				if (mouth_right != null) {
					face.position.mouth_right_x = (float) mouth_right.getDouble("x");
					face.position.mouth_right_y = (float) mouth_right.getDouble("y");
				}

				JSONObject nose = facePosition.getJSONObject("nose");
				if (nose != null) {
					face.position.nose_x = (float) nose.getDouble("x");
					face.position.nose_y = (float) nose.getDouble("y");
				}

				identifyRst.faces.add(face);
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result != null;
	}

	/**
	 * 给定一个Face和一个Faceset，在该Faceset内搜索最相似的Face。提示：若搜索集合需要包含超过10000张人脸，
	 * 可以分成多个faceset分别调用search功能再将结果按confidence顺序合并即可。
	 * 
	 * @param key_face_id
	 *            待搜索的Face的face_id
	 * @param faceset_id_name
	 *            指定搜索范围Faceset的id或者名称
	 * @param is_id
	 *            是否使用id
	 * @param count
	 *            表示一共获取不超过count个搜索结果。默认count=3
	 * @return
	 */
	public boolean search(String key_face_id, String faceset_id_name, boolean is_id, int count) {
		JSONObject result = null;

		try {
			PostParameters parameters = new PostParameters();
			parameters.setKeyFaceId(key_face_id);
			if (is_id) {
				parameters.setFacesetId(faceset_id_name);
			} else {
				parameters.setFacesetName(faceset_id_name);
			}
			parameters.setCount(count);
			result = FaceBase.getHttpRequests().recognitionSearch(parameters);

			JSONArray candidates = result.getJSONArray("candidate");
			for (int i = 0; i < candidates.length(); i++) {
				SearchCandidate candidate = new SearchCandidate();
				JSONObject candidateObj = candidates.getJSONObject(i);
				candidate.face_id = candidateObj.getString("face_id");
				candidate.similarity = (float) candidateObj.getDouble("similarity");
				candidate.tag = candidateObj.getString("tag");
				searchRst.candidates.add(candidate);
			}

		} catch (FaceppParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result != null;
	}

	/**
	 * 各种识别结果类
	 * 
	 * @author Administrator
	 * 
	 */
	public class CompareResult {
		public float similarity; // 总体相似度
		public float similarity_eye; // 眼镜相似度
		public float similarity_mouth; // 嘴巴相似度
		public float similarity_nose; // 鼻子相似度
		public float similarity_eyebrow; // 眉毛相似度
	}

	public class VerifyResult {
		public boolean is_same_person; // 是否为同一人
		public float confidence; // 置信度
	}

	public class IdentifyResult {
		public List<IdentifyFace> faces = new ArrayList<IdentifyFace>();
	}

	public class IdentifyFace {
		// 识别结果。candidate包含不超过3个人，包含相应person信息与相应的置信度
		public List<IdentifyCandidate> identifyCandidate = new ArrayList<IdentifyCandidate>();
		public String face_id;
		public FacePosition position = new FacePosition();
	}

	public class IdentifyCandidate {
		public float confidence; // 置信度
		public String person_id; // personID
		public String person_name; // personName
		public String tag; // tag
	}

	public class SearchResult {
		public List<SearchCandidate> candidates = new ArrayList<SearchCandidate>();
	}

	public class SearchCandidate {
		public String face_id;
		public float similarity;
		public String tag;
	}
}
