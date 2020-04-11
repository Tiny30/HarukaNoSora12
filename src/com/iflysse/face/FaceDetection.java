package com.iflysse.face;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;

public class FaceDetection {
	
	private PostParameters parameters;

	public String img_id;
	public int img_width;
	public int img_height;
	public List<DetectionResult> faceInfos = new ArrayList<DetectionResult>();

	public FaceDetection(PostParameters parameters) {
		super();
		setParameters(parameters);
	}

	public void setParameters(PostParameters parameters) {
		this.parameters = parameters;
		if (this.parameters == null) {
			this.parameters = new PostParameters().setMode("oneface");
		}
	}

	/**
	 * 面部检测
	 * 
	 * @param imgPath
	 *            图片文件路径
	 * @return 图片中包含的所有面部检测结果
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean detect(String imgPath) throws FaceppParseException, JSONException {
		JSONObject result = null;
		
			result = FaceBase.getHttpRequests().detectionDetect(parameters.setImg(
					new File(imgPath)).setMode("oneface"));
			checkFaceInfos(result);
			
		return result != null;
	}

	/**
	 * 面部检测
	 * 
	 * @param imgData
	 *            图片字节数组
	 * @return 图片中包含的所有面部检测结果
	 * @throws FaceppParseException 
	 * @throws JSONException 
	 */
	public boolean detect(byte[] imgData) throws FaceppParseException, JSONException {
		JSONObject result = null;
	
			result = FaceBase.getHttpRequests().detectionDetect(parameters
					.setImg(imgData));
			checkFaceInfos(result);
		return result != null;
	}

	/**
	 * 检测面部信息
	 * 
	 * @param result
	 *            检测总结果值
	 * @throws JSONException
	 */
	private void checkFaceInfos(JSONObject result) throws JSONException {
		img_width = result.getInt("img_width");
		img_height = result.getInt("img_height");
		img_id = result.getString("img_id");

		JSONArray faces = result.getJSONArray("face");
		for (int i = 0; i < faces.length(); ++i) {
			JSONObject face = faces.getJSONObject(i);
			
			faceInfos.add(checkFaceInfo(face));
		}
	}

	private DetectionResult checkFaceInfo(JSONObject face) throws JSONException {
		DetectionResult info = new DetectionResult();

		// 获取faceId
		info.faceId = face.getString("face_id");

		// 获取面部位置信息
		JSONObject facePosition = face.getJSONObject("position");

		JSONObject center = facePosition.getJSONObject("center");
		if (center != null) {
			info.position.center_x = Float.valueOf(center.get("x").toString());
			info.position.center_y = Float.valueOf(center.get("y").toString());
		}

		info.position.width = Float.valueOf(facePosition.get("width")
				.toString());
		info.position.height = Float.valueOf(facePosition.get("height")
				.toString());

		JSONObject eye_left = facePosition.getJSONObject("eye_left");
		if (eye_left != null) {
			info.position.eye_left_x = Float.valueOf(eye_left.get("x")
					.toString());
			info.position.eye_left_y = Float.valueOf(eye_left.get("y")
					.toString());
		}

		JSONObject eye_right = facePosition.getJSONObject("eye_right");
		if (eye_right != null) {
			info.position.eye_right_x = Float.valueOf(eye_right.get("x")
					.toString());
			info.position.eye_right_y = Float.valueOf(eye_right.get("y")
					.toString());
		}

		JSONObject mouth_left = facePosition.getJSONObject("mouth_left");
		if (mouth_left != null) {
			info.position.mouth_left_x = Float.valueOf(mouth_left.get("x")
					.toString());
			info.position.mouth_left_y = Float.valueOf(mouth_left.get("y")
					.toString());
		}

		JSONObject mouth_right = facePosition.getJSONObject("mouth_right");
		if (mouth_right != null) {
			info.position.mouth_right_x = Float.valueOf(mouth_right.get("x")
					.toString());
			info.position.mouth_right_y = Float.valueOf(mouth_right.get("y")
					.toString());
		}

		JSONObject nose = facePosition.getJSONObject("nose");
		if (nose != null) {
			info.position.nose_x = Float.valueOf(nose.get("x").toString());
			info.position.nose_y = Float.valueOf(nose.get("y").toString());
		}

		// 获取面部属性信息
		JSONObject faceAttribute = face.getJSONObject("attribute");

		if (faceAttribute.has("age")) {
			JSONObject age = faceAttribute.getJSONObject("age");
			if (age != null) {
				info.attribute.age_value = age.getInt("value");
				info.attribute.age_range = age.getInt("range");
			}
		}

		if (faceAttribute.has("gender")) {
			JSONObject gender = faceAttribute.getJSONObject("gender");
			if (gender != null) {
				info.attribute.gender_value = gender.getString("value");
				info.attribute.gender_confidence = Float.valueOf(gender.get(
						"confidence").toString());
			}
		}

		if (faceAttribute.has("glass")) {
			JSONObject glass = faceAttribute.getJSONObject("glass");
			if (glass != null) {
				info.attribute.glass_value = glass.getString("value");
				info.attribute.glass_confidence = Float.valueOf(glass.get(
						"confidence").toString());
			}
		}

		if (faceAttribute.has("pose")) {
			JSONObject pose = faceAttribute.getJSONObject("pose");
			if (pose != null) {
				info.attribute.pose_pitch_angle = Float.valueOf(pose
						.getJSONObject("pitch_angle").get("value").toString());
				info.attribute.pose_roll_angle = Float.valueOf(pose
						.getJSONObject("roll_angle").get("value").toString());
				info.attribute.pose_yaw_angle = Float.valueOf(pose
						.getJSONObject("yaw_angle").get("value").toString());
			}
		}

		if (faceAttribute.has("race")) {
			JSONObject race = faceAttribute.getJSONObject("race");
			if (race != null) {
				info.attribute.race_value = race.getString("value");
				info.attribute.race_confidence = Float.valueOf(race.get(
						"confidence").toString());
			}
		}

		if (faceAttribute.has("smiling")) {
			JSONObject smiling = faceAttribute.getJSONObject("smiling");
			if (smiling != null) {
				info.attribute.smiling = Float.valueOf(smiling.get("value")
						.toString());
			}
		}

		return info;
	}
	
	public class DetectionResult{
		public String faceId;
		public FaceAttribute attribute = new FaceAttribute();
		public FacePosition position = new FacePosition();
		@Override
		public String toString() {
			return "DetectionResult [faceId=" + faceId + ", attribute=" + attribute + ", position=" + position + "]";
		}
		
		
	}

}
