package com.haruka.ability.area;

import java.util.ArrayList;
import java.util.List;

import com.haruka.ability.area.utils.Alarm;
import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.RegexTools;

public class UtilManager implements BrainArea{

	private ArtificialBrain brain;

	private List<Alarm> alarmGroup = new ArrayList<>();

	public UtilManager(ArtificialBrain brain) {
		this.brain = brain;
	}

	public void handleUtilOrder(String order) {
		System.out.println("工具模式：" + order);
		if (order.matches("提醒(我)?.*?")) {
			order = order.replaceFirst("提醒(我)?", "");
			String time = RegexTools.getFirstConform(order, "[0-9][0-9][点\\.:：](半|[0-9][0-9])");
			String times[] = time.split("[点\\.:：]");
			Alarm alarm = new Alarm(brain);
			BrainAreaTools.displayAsList(this, "闹钟已定下");
			alarm.excute(times[0], times[1], "0", new Alarm.Task(alarm, order) {
				@Override
				public void run() {
					this.getAlarm().getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.ALARM, "你定下的计划：" + this));
					this.cancel();
				}
			});
		}
	}

	@Override
	public ArtificialBrain getBrain() {
		return this.brain ;
	}

}
