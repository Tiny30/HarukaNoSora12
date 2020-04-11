package com.haruka.ability.area;

import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.sora.ArtificialIntelligence;

public class MoveArea {
	private ArtificialBrain brain ;
	
	public MoveArea(ArtificialBrain brain){
		this.brain = brain ;
	}
	
	public void handleMoveOrder(String order){
		int dir = ArtificialIntelligence.DIR_UP;
		if(order.matches(".*?往左边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_LEFT;
		}else if(order.matches(".*?往右边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_RIGHT;
		}else if(order.matches(".*?往上边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_UP;
		}else if(order.matches(".*?往下边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_DOWN;
		}else if(order.matches(".*?往左上边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_UPPER_LEFT;
		}else if(order.matches(".*?往右上边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_UPPER_RIGHT;
		}else if(order.matches(".*?往左下边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_LOWER_LEFT;
		}else if(order.matches(".*?往右下边[移区去].*?")){
			dir = ArtificialIntelligence.DIR_LOWER_RIGHT;
		}
		this.brain.getOwner().move(dir, 10);
	}
}	
