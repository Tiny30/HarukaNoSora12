package com.haruka.view.panel;

import com.haruka.sora.ArtificialIntelligence;
import com.iflysse.camera.ui.VideoPanel;
import com.iflysse.camera.util.Camera;

public class ArtificialCameraPanel extends VideoPanel{
	
	private Camera eye ;
	
	private ArtificialIntelligence owner ;

	public ArtificialCameraPanel(Camera eye, ArtificialIntelligence owner) {
		super();
		this.eye = eye;
		this.owner = owner;
		this.setBounds(0, 0, 500, 350);
	}
}
