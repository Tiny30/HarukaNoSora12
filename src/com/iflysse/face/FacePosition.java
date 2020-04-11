package com.iflysse.face;

public class FacePosition {

	public float center_x; // 面部区域中心点x百分比
	public float center_y; // 面部区域中心点y百分比
	public float width; // 面部区域宽度百分比
	public float height; // 面部区域高度百分比
	public float eye_left_x; // 左眼x百分比
	public float eye_left_y; // 左眼y百分比
	public float eye_right_x; // 右眼x百分比
	public float eye_right_y; // 右眼y百分比
	public float mouth_left_x; // 左侧嘴角x百分比
	public float mouth_left_y; // 左侧嘴角y百分比
	public float mouth_right_x; // 右侧嘴角x百分比
	public float mouth_right_y; // 右侧嘴角y百分比
	public float nose_x; // 鼻尖坐标x百分比
	public float nose_y; // 鼻尖坐标y百分比
	@Override
	public String toString() {
		return "FacePosition [center_x=" + center_x + ", center_y=" + center_y + ", width=" + width + ", height="
				+ height + ", eye_left_x=" + eye_left_x + ", eye_left_y=" + eye_left_y + ", eye_right_x=" + eye_right_x
				+ ", eye_right_y=" + eye_right_y + ", mouth_left_x=" + mouth_left_x + ", mouth_left_y=" + mouth_left_y
				+ ", mouth_right_x=" + mouth_right_x + ", mouth_right_y=" + mouth_right_y + ", nose_x=" + nose_x
				+ ", nose_y=" + nose_y + "]";
	}
}
