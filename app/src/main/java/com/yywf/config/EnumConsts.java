package com.yywf.config;

import com.tool.utils.utils.StringUtils;
import com.yywf.R;


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
		MENU_1(1, R.drawable.mywallet,"我的钱包"),
		MENU_2(2, R.drawable.mygroup,"我的团队"),
		MENU_3(3, R.drawable.myqrcode,"推广二维码"),
//		MENU_4(4, R.drawable.myvouchar,"抵用券"),
		MENU_4(4, R.drawable.icon_scanqr,"扫码收钱"),

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
			if (StringUtils.isBlank(name)){
				return -1;
			}
			MineMenuType[] timeZoneTypes = MineMenuType.values();
			for (MineMenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getName() == name) {
					return timeZoneType.getCode();
				}
			}
			return -1;
		}
	}


	public enum BankMenuType {
		MENU_1(1, R.drawable.icon_managecard_bank_pf,"浦发银行"),
		MENU_2(2, R.drawable.icon_managecard_bank_ms,"民生银行"),
		MENU_3(3, R.drawable.icon_managecard_bank_xy,"兴业银行"),
		MENU_4(4, R.drawable.icon_managecard_bank_pa,"平安银行"),
		MENU_5(5, R.drawable.icon_managecard_bank_zs,"招商银行"),
		MENU_6(6, R.drawable.icon_managecard_bank_jt,"交通银行"),
		MENU_7(7, R.drawable.icon_managecard_bank_gs,"工商银行"),
		MENU_8(8, R.drawable.icon_managecard_bank_js,"建设银行"),
		MENU_9(9, R.drawable.icon_managecard_bank_ny,"农业银行"),
		MENU_10(10, R.drawable.icon_managecard_bank_zg,"中国银行"),
		MENU_11(11, R.drawable.icon_managecard_bank_gf,"广发银行"),
		MENU_12(12, R.drawable.icon_managecard_bank_zx,"中信银行"),
		MENU_13(13, R.drawable.icon_managecard_bank_gd,"光大银行"),
		MENU_14(14, R.drawable.icon_managecard_bank_hx,"华夏银行"),

		;

		private int code;
		private int bg;
		private String name;

		BankMenuType(int code, int bg, String name) {
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

		public static BankMenuType getByCode(int code) {
			BankMenuType[] timeZoneTypes = BankMenuType.values();
			for (BankMenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getCode() == code) {
					return timeZoneType;
				}
			}
			return null;
		}

		public static int getCodeByName(String name) {
			if (StringUtils.isBlank(name)){
				return -1;
			}
			BankMenuType[] timeZoneTypes = BankMenuType.values();
			for (BankMenuType timeZoneType : timeZoneTypes) {
				if (timeZoneType.getName() == name) {
					return timeZoneType.getCode();
				}
			}
			return -1;
		}
	}


	public enum BankUi {
		BANK_UNKNOW(0, "未知", R.drawable.ic_launcher),
		BANK_GS(1, "工商银行", R.drawable.icon_managecard_bank_gs),
		BANK_GD(2,"光大银行", R.drawable.icon_managecard_bank_gd),
		BANK_GF(3, "广发银行",R.drawable.icon_managecard_bank_gf),
		BANK_HX(4, "华夏银行",R.drawable.icon_managecard_bank_hx),
		BANK_JS(5, "建设银行",R.drawable.icon_managecard_bank_js),
		BANK_JT(6, "交通银行",R.drawable.icon_managecard_bank_jt),
		BANK_MS(7, "民生银行", R.drawable.icon_managecard_bank_ms),
		BANK_NY(8, "农业银行",R.drawable.icon_managecard_bank_ny),
		BANK_PA(9, "平安银行", R.drawable.icon_managecard_bank_pa),
		BANK_PF(10, "浦发银行", R.drawable.icon_managecard_bank_pf),
		BANK_XY(
				11,
				"兴业银行",
				R.drawable.icon_managecard_bank_xy),

		BANK_ZS(
				12,
				"招商银行",
				R.drawable.icon_managecard_bank_zs),
		BANK_ZG(
				13,
				"中国银行",
				R.drawable.icon_managecard_bank_zg),
		BANK_ZX(
				14,
				"中信银行",
				R.drawable.icon_managecard_bank_zx),
		BANK_PDFZ(15, "浦东发展银行", R.drawable.icon_managecard_bank_pf),
		;

		private int type;
		private String showName;
		private int icon_id;

		BankUi(int type, String showName, int icon_id) {
			this.type = type;
			this.showName = showName;
			this.icon_id = icon_id;
		}

		public int getType() {
			return type;
		}

		public int getIcon_id() {
			return icon_id;
		}

		public String getShowName() {
			return showName;
		}


		public static BankUi getByType(int type) {
			BankUi[] bankUis = BankUi.values();
			for (BankUi bankUi : bankUis) {
				if (bankUi.getType() == type) {
					return bankUi;
				}
			}
			return null;
		}

		public static BankUi getTypeByName(String name) {
			if (StringUtils.isBlank(name)){
				return null;
			}
			BankUi[] bankUis = BankUi.values();
			for (BankUi bankUi : bankUis) {
				// if (bankUi.getShowName().equals(name)) {
				// return bankUi;
				// }
				if (name.contains(bankUi.getShowName())) {
					return bankUi;
				}
			}
			return null;
		}

	}

}
