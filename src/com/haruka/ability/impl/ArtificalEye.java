package com.haruka.ability.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import com.haruka.ability.AbilityWatch;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.FaceTools;
import com.haruka.view.panel.ArtificialCameraPanel;
import com.iflysse.camera.util.Camera;

public class ArtificalEye implements AbilityWatch {

	private Camera realEye;

	private ArtificialIntelligence owner;

	private ArtificialCameraPanel cameraPanel;

	public ArtificalEye(ArtificialIntelligence owner) {
		super();
		this.owner = owner;
		this.initWatch();
	}

	@Override
	public void initWatch() {
		this.realEye = new Camera(0);
		this.cameraPanel = new ArtificialCameraPanel(this.realEye, this.owner);
	}

	@Override
	public void startWatch() {
		this.owner.setDisplayPanel(cameraPanel);
		this.realEye.open();
		this.realEye.linkPanel(this.cameraPanel);
		this.realEye.startCapture();
	}

	@Override
	public void duringWatch() {

	}

	@Override
	public void stopWatch() {

		this.realEye.close();
		this.owner.watchCompleted();
	}

	@Override
	public void catureImage(String parentPath, boolean isDirectory) {
		try {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File storage = null;
			if (isDirectory) {
				storage = new File(parentPath + new Date().getTime() + ".jpg");
			} else {
				storage = new File(parentPath);
			}
			if (!storage.getParentFile().exists()) {
				storage.getParentFile().mkdirs();
			}
			ImageIO.write(this.cameraPanel.getImage(), "jpg", new FileOutputStream(storage));
			this.owner.speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, "已拍好"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerMasterFace(String masterName, String masterGroupName, String imgsRootPath) {
		this.owner.speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "开始识别脸部信息"));
		FaceTools.createGroup(masterGroupName);
		boolean first = true;
		for (File file : new File(imgsRootPath).listFiles()) {
			String faceId = FaceTools.getFaceId(file.getAbsolutePath());
			if (first) {
				FaceTools.createPerson(masterName, faceId, "master", masterGroupName);
				first = false;
			} else {
				FaceTools.addFaceToPerson(faceId, masterName);
			}
		}
		this.owner.speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "脸部信息注册完成"));
	}

}
