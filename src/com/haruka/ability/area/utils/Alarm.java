
package com.haruka.ability.area.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.haruka.ability.impl.ArtificialBrain;

public class Alarm extends Timer {
	
	public static final String ALARM_GRUP_KEY = "alarmGroupKey" ;
	private int hour;
	private int minute;
	private int second;
	private Task task;
	private String id ;
	private  ArtificialBrain brain ;
	

	public static abstract class Task extends TimerTask{

		private Alarm alarm ;
		
		private ArtificialBrain brain ;
		
		private String taskContent ;
		
		public Task( Alarm alarm, String taskContent){
			this.alarm = alarm ;
			this.taskContent = taskContent ;
		}

		public Alarm getAlarm() {
			return alarm;
		}

		public void setAlarm(Alarm alarm) {
			this.alarm = alarm;
		}
		
		public String toString(){
			return this.taskContent ;
		}
		
		@Override
		public boolean cancel() {
			this.alarm.cancel();
			return super.cancel();
		}
		
	}
	public Alarm(ArtificialBrain brain) {
		this.brain = brain ;
	}

	public void excute(int hour, int minute, int second, Task task) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.task = task ;
		this.id = hour + ":" + minute + ":" + second ;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);

		Date time = calendar.getTime();
		Date timeNow = new Date();
		if (time.after(timeNow)) {
			this.scheduleAtFixedRate(task, time, 1000 * 60 * 60 * 24);
		}

	}
	
	public void excute(String hour, String minute, String second, Task task) {
		this.excute(Integer.parseInt(hour), minute.equals("半")?30:Integer.parseInt(minute), Integer.parseInt(second), task);
	}

	public String getAlarm() {
		return this.id + " " + this.task;
	}

	
	public ArtificialBrain getBrain() {
		return brain;
	}

	public void setBrain(ArtificialBrain brain) {
		this.brain = brain;
	}

	@Override
	public void cancel() {
		this.brain.hdel( ALARM_GRUP_KEY, this.id) ;
		super.cancel();
	}
	
	
}
