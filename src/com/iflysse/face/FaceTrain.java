package com.iflysse.face;

import com.facepp.error.FaceppParseException;
import com.facepp.http.PostParameters;

public class FaceTrain {

	/**
	 * 针对verify功能对一个person进行训练。请注意:
	 * 
	 * 在一个person内进行verify之前，必须先对该person进行Train
	 * 当一个person内的数据被修改后(例如增删Person相关的Face等)，为使这些修改生效，person应当被重新Train
	 * 
	 * @param person_id_name
	 * @param is_id
	 */
	public static void verify(String person_id_name, boolean is_id) {
		PostParameters parameters = new PostParameters();
		if (is_id) {
			parameters.setPersonId(person_id_name);
		} else {
			parameters.setPersonName(person_id_name);
		}
		try {
			FaceBase.getHttpRequests().trainVerify(parameters);
		} catch (FaceppParseException e) {
			e.printStackTrace();
		}
	}

	public static void verify(String person_id) {
		verify(person_id, true);
	}

	/**
	 * 针对search功能对一个faceset进行训练。请注意:
	 * 
	 * 在一个faceset内进行search之前，必须先对该faceset进行Train
	 * 当一个faceset内的数据被修改后(例如增删Face等)，为使这些修改生效，faceset应当被重新Train
	 * 
	 * @param face_set_id_name
	 * @param is_id
	 */
	public static void search(String face_set_id_name, boolean is_id) {
		PostParameters parameters = new PostParameters();
		if (is_id) {
			parameters.setFacesetId(face_set_id_name);
		} else {
			parameters.setFacesetName(face_set_id_name);
		}
		try {
			FaceBase.getHttpRequests().trainSearch(parameters);
		} catch (FaceppParseException e) {
			e.printStackTrace();
		}
	}

	public static void search(String face_set_id) {
		search(face_set_id, true);
	}

	/**
	 * 针对identify功能对一个Group进行训练。请注意:
	 * 
	 * 在一个Group内进行identify之前，必须先对该Group进行Train 当一个Group内的数据被修改后(例如增删Person,
	 * 增删Person相关的Face等)，为使这些修改生效，Group应当被重新Train
	 * 
	 * @param group_id_name
	 * @param is_id
	 */
	public static void identify(String group_id_name, boolean is_id) {
		PostParameters parameters = new PostParameters();
		if (is_id) {
			parameters.setGroupId(group_id_name);
		} else {
			parameters.setGroupName(group_id_name);
		}
		try {
			FaceBase.getHttpRequests().trainIdentify(parameters);
		} catch (FaceppParseException e) {
			e.printStackTrace();
		}
	}

	public static void identify(String group_id) {
		identify(group_id, true);
	}
}
