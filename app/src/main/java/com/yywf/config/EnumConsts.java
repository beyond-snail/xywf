package com.yywf.config;

import com.yywf.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EnumConsts {





	public enum MenuType {
		MENU_1(1, R.drawable.kuaijieshouqian,"快捷收钱"),
		MENU_2(2, R.drawable.zhinenghuankuan,"智能还款"),
		MENU_3(3, R.drawable.huanxinyongka,"还信用卡"),
		MENU_4(4, R.drawable.yijiantie,"一键提额"),
		MENU_5(5, R.drawable.daibanshixiang,"待办事项"),
		MENU_6(6, R.drawable.yijianbanka,"一键办卡"),
		MENU_7(7, R.drawable.wodezhangdai,"我的账单"),
		MENU_8(8, R.drawable.saomashouqian,"购买等级"),

		;

		private int code;
		private int bg;
		private String name;

		MenuType(int code, int bg, String name) {
			this.code = code;
			this.bg = bg;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public int getBg() {
			return bg;
		}

		public String getName() {
			return name;
		}

		public static MenuType getByCode(int code) {
			MenuType[] timeZoneTypes = MenuType.values();
			for (MenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getCode() == code) {
					return timeZoneType;
				}
			}
			return null;
		}

		public static int getCodeByName(String name) {
			MenuType[] timeZoneTypes = MenuType.values();
			for (MenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getName() == name) {
					return timeZoneType.getCode();
				}
			}
			return -1;
		}
	}


	public enum MineMenuType {
		MENU_1(1, R.drawable.kuaijieshouqian,"我的钱包"),
		MENU_2(2, R.drawable.zhinenghuankuan,"我的团队"),
		MENU_3(3, R.drawable.huanxinyongka,"推广二维码"),
		MENU_4(4, R.drawable.yijiantie,"抵用券"),

		;

		private int code;
		private int bg;
		private String name;

		MineMenuType(int code, int bg, String name) {
			this.code = code;
			this.bg = bg;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public int getBg() {
			return bg;
		}

		public String getName() {
			return name;
		}

		public static MineMenuType getByCode(int code) {
			MineMenuType[] timeZoneTypes = MineMenuType.values();
			for (MineMenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getCode() == code) {
					return timeZoneType;
				}
			}
			return null;
		}

		public static int getCodeByName(String name) {
			MineMenuType[] timeZoneTypes = MineMenuType.values();
			for (MineMenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getName() == name) {
					return timeZoneType.getCode();
				}
			}
			return -1;
		}
	}

}
