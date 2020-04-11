package com.iflysse.face;

public class FaceAttribute {
	
	public int age_value; // 年龄值
	public int age_range; // 年龄偏差
	public String gender_value; // 性别
	public float gender_confidence; // 性别可信度
	public String glass_value; // 眼镜
	public float glass_confidence; // 眼镜可信度
	public float pose_pitch_angle; // 抬头角度
	public float pose_roll_angle; // 旋转角度
	public float pose_yaw_angle; // 摇头角度
	public String race_value; // 种族
	public float race_confidence; // 种族可信度
	public float smiling; // 笑容
	@Override
	public String toString() {
		return "FaceAttribute [age_value=" + age_value + ", age_range=" + age_range + ", gender_value=" + gender_value
				+ ", gender_confidence=" + gender_confidence + ", glass_value=" + glass_value + ", glass_confidence="
				+ glass_confidence + ", pose_pitch_angle=" + pose_pitch_angle + ", pose_roll_angle=" + pose_roll_angle
				+ ", pose_yaw_angle=" + pose_yaw_angle + ", race_value=" + race_value + ", race_confidence="
				+ race_confidence + ", smiling=" + smiling + "]";
	}
	
	
}