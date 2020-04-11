package com.haruka.ability.area ;

import com.haruka.ability.impl.ArtificialBrain;

// ========================================逻辑区开始=================================

	public class LogicArea implements BrainArea{

		private ArtificialBrain brain ;
		
		/**
		 * 逻辑“是” 的key
		 */
		public final static String LOGIC_TRUE_KEY = "logic_true";

		/**
		 * 确认拍照
		 */
		public final static String LOGIC_TRUE_PHOTO_KEY = "logic_true_photo";
		/**
		 * 停止拍照
		 */
		public final static String LOGIC_FALSE_PHOTO_KEY = "logic_false_photo_key";

		/**
		 * 逻辑“否” 的key
		 */
		public final static String LOGIC_FALSE_KEY = "logic_false";
		
		public final static String MANNER_KEY = "manner_key";

		public LogicArea(ArtificialBrain brain ) {
			this.brain = brain ;

			// 注册逻辑是
			StringBuffer sbf = new StringBuffer("");
			sbf.append("好|").append("是|").append("是的|").append("好的|").append("很好|").append("可以|").append("恩|")
					.append("确定|").append("这样可以|").append("yes|").append("椰丝|").append("no|").append("ok|")
					.append("甚好|").append("如此甚好|").append("就这样|").append("干得不错|").append("这样去做吧|").append("就这样去做吧|");
			brain.set(LOGIC_TRUE_KEY, sbf.toString());

			// 注册逻辑非
			sbf = new StringBuffer("");
			sbf.append("可以了|").append("取消|").append("不需要|").append("不用了|");
			brain.set(LOGIC_FALSE_KEY, sbf.toString());

			// 注册确认拍照语句
			sbf = new StringBuffer("");

			sbf.append("椰丝|").append("耶|").append("茄子|").append("ok|").append("yes|").append("准备好了|").append("开始吧|")
					.append("开始拍照吧|");

			brain.set(LOGIC_TRUE_PHOTO_KEY, sbf.toString());

			// 注册停止拍照语句
			sbf = new StringBuffer("");

			sbf.append("就这样吧|").append("可以了|").append("不拍了|").append("这样够了|").append("这样就可以了|").append("辛苦了|")
					.append("停下吧|").append("我累了|").append("可以啦！|").append("在这停顿|");

			brain.set(LOGIC_FALSE_PHOTO_KEY, sbf.toString());
		}

		public boolean judgeOrderFalse(String order) {
			return this.baseJudge(order, brain.get(LOGIC_FALSE_KEY));
		}

		public boolean judgeOrderTrue(String order) {
			System.out.println("判断是" + order);
			return this.baseJudge(order, brain.get(LOGIC_TRUE_KEY));
		}

		public boolean judgeTakePohtoTrue(String order) {
			return this.baseJudge(order, brain.get(LOGIC_TRUE_PHOTO_KEY));
		}

		public boolean judgeTakePhotoFalse(String order) {
			return this.baseJudge(order, brain.get(LOGIC_FALSE_PHOTO_KEY));
		}

		private boolean baseJudge(String order, String logicTrue) {
			if (logicTrue.matches(".*?\\|?" + order + ".*?\\|.*?")) {
				System.out.println("成功");
				return true;
			}
			return false;
		}
		
		public boolean judgeManner(String order){
			if(order.matches(".*?(请|帮我|帮下忙|请求|请帮我).*?")){
				return true ;
			}
			return false ;
		}

		@Override
		public ArtificialBrain getBrain() {
			return this.brain ;
		}
	}
	// ========================================逻辑区结束=================================