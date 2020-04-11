package com.haruka.tools;

import org.json.JSONException;

import com.facepp.error.FaceppParseException;
import com.iflysse.face.FaceBase;
import com.iflysse.face.FaceDetection;
import com.iflysse.face.FaceGroup;
import com.iflysse.face.FacePerson;
import com.iflysse.face.FaceRecognition;

public class FaceTools {

	static {
		FaceBase.init("c25e31bd42cf1014941b3b30b50cf315", "MRiJJO3crikWDaGIumD8g5RQdEW5US7D");
	}

	public static final FaceGroup group = new FaceGroup();

	public static final FacePerson person = new FacePerson();

	public static final FaceDetection detection = new FaceDetection(null);

	public static final FaceRecognition recognition = new FaceRecognition();

	public static final int FIAL = 0;

	public static final int SUCCESS = 1;

	public static final int ERROR = 2;

	public static final int NOT_FOUND = 3;
	
	public static final int HAS_EXITED = 4 ;

	public static int createGroup(String groupName) {
		try {
			if (group.create(groupName, null, false)) {
				return SUCCESS;
			} else {
				return FIAL;
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
			return HAS_EXITED;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public static int getGroupInfo(String groupName) {
		try {
			if (group.get_info(groupName, false)) {
				return SUCCESS;
			} else {
				return FIAL;
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
			return NOT_FOUND;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public static int createPerson(String personName, String faceId, String tag, String gruopName) {
		try {
			if (person.create(personName, faceId, tag, gruopName, false)) {
				return SUCCESS;
			} else {
				return FIAL;
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
			return HAS_EXITED;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public static int addFaceToPerson(String faceId, String personName) {
		try {
			if (person.add_face(personName, false, faceId)) {
				return SUCCESS;
			} else {
				return FIAL;
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
			return NOT_FOUND;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	public static int verifyPerson(String faceId, String personName) {

		try {
			if (recognition.verify(faceId, personName, false)) {
				return SUCCESS;
			} else {
				return FIAL;
			}
		} catch (FaceppParseException e) {
			e.printStackTrace();
			return NOT_FOUND;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public static String getFaceId(String imgPath)  {
	
		try {
			detection.detect(imgPath);
			return detection.faceInfos.get(0).faceId;
		} catch (FaceppParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (IndexOutOfBoundsException e){
			
			throw e;
		}
		return null ;
		
	}
}
