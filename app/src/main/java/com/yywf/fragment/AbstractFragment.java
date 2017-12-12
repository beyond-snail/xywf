package com.yywf.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.views.popwindow.bean.BaseFilter;
import com.views.popwindow.pop.CommonFilterPop;
import com.yywf.R;

import java.util.ArrayList;
import java.util.List;


public class AbstractFragment extends Fragment {

	public static final Handler handler = new Handler();

	public final static int DATA_LOAD_ING = 0x001;
	public final static int DATA_LOAD_COMPLETE = 0x002;
	public final static int DATA_LOAD_FAIL = 0x003;

	/**
	 * 主activity
	 */
	public Context mContext;

	public View fragment;

	/**
	 * 筛选pop
	 */
	private CommonFilterPop mPopupWindow;

	/**
	 * 加载等待效果
	 */
	public ProgressDialog progress;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContext = getActivity();
		this.fragment = getView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (progress != null) {
			progress.dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (progress != null) {
			progress.dismiss();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (progress != null) {
			progress.dismiss();
		}
	}

	/**
	 * 显示字符串消息
	 * 
	 * @param message
	 */
	public void showProgress(final String message) {
		AbstractFragment.handler.post(new Runnable() {

			@Override
			public void run() {
				if (progress != null) {
					progress.dismiss();
				}
				progress = new ProgressDialog(getActivity());
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.setMessage(message);
//				progress.setCancelable(false);
				progress.show();
				View layout = View.inflate(mContext, R.layout.layout_dialog, null);
				TextView tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
				if (!"".equals(message)) {
					tv_msg.setText("  " + message);
				}
				progress.getWindow().setContentView(layout);// show()代码后
			}
		});
	}
	
	public void setProgressText(String msg) {
		if (progress != null && progress.isShowing()) {
			progress.setMessage(msg);
		}
	}

	/**
	 * 隐藏字符串消息
	 */
	public void disShowProgress() {
		AbstractFragment.handler.post(new Runnable() {

			@Override
			public void run() {
				if (progress != null) {
					progress.dismiss();
				}
			}
		});

	}

	/**
	 * 提示信息
	 * 
	 * @param message
	 */
	public void showErrorMsg(String message) {
		final String str = message;
		AbstractFragment.handler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	/**
	 * 提示信息号或请求失败信息
	 * 
	 * showErrorRequestFair
	 *
	 */
	public void showE404() {
		AbstractFragment.handler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(mContext, "手机信号差或服务器维护，请稍候重试。谢谢！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	/**
	 * 显示数据加载状态
	 * 
	 * @param loading
	 * @param datas
	 * @param type
	 */
	public void viewSwitch(View loading, View datas, int type) {
		switch (type) {
		case DATA_LOAD_ING:
			datas.setVisibility(View.GONE);
			loading.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_COMPLETE:
			datas.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			break;
		case DATA_LOAD_FAIL:
			datas.setVisibility(View.GONE);
			loading.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 获取TextView
	 */
	public TextView findTextViewById(int id) {
		return (TextView) fragment.findViewById(id);
	}

	/**
	 * 获取Button
	 * 
	 * @param id
	 * @return
	 */
	public Button findButtonById(int id) {
		return (Button) fragment.findViewById(id);
	}

	/**
	 * 获取ImageButton
	 * 
	 * @param id
	 * @return
	 */
	public ImageButton findImageButtonById(int id) {
		return (ImageButton) fragment.findViewById(id);
	}

	/**
	 * 获取ImageView
	 * 
	 * @param id
	 * @return
	 */
	public ImageView findImageViewById(int id) {
		return (ImageView) fragment.findViewById(id);
	}

	/**
	 * 获取LinearLayout
	 * 
	 * @param id
	 * @return
	 */
	public LinearLayout findLayoutById(int id) {
		return (LinearLayout) fragment.findViewById(id);
	}

	/**
	 * 获取LinearLayout
	 * 
	 * @param id
	 * @return
	 */
	public EditText findEditTextById(int id) {
		return (EditText) fragment.findViewById(id);
	}

	/**
	 * 获取TableRow
	 * 
	 * @param id
	 * @return
	 */
	public TableRow findTableRowById(int id) {
		return (TableRow) fragment.findViewById(id);
	}

	/**
	 * 进入Activity
	 */
	public void goActivity(Class<?> inClass) {
		Intent intent = new Intent(mContext, inClass);
		startActivity(intent);
	}

	/**
	 * 进入Activity
	 */
	public void goActivityAddBundle(Class<?> inClass, Bundle bundle) {
		Intent intent = new Intent(mContext, inClass);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 文本View
	 */
	public TextView textView(int textview) {
		return (TextView) fragment.findViewById(textview);
	}

	/**
	 * 文本button
	 */
	public Button button(int id) {
		return (Button) fragment.findViewById(id);
	}

	/**
	 * 文本button
	 */
	public ImageView imageView(int id) {
		return (ImageView) fragment.findViewById(id);
	}

	/**
	 * 文本editText
	 */
	public EditText editText(int id) {
		return (EditText) fragment.findViewById(id);
	}

	public LinearLayout linearLayout(int id) {
		return (LinearLayout) fragment.findViewById(id);
	}



	/**
	 * 列表选择popupWindow
	 *
	 * @param parentView        父View
	 * @param itemTexts         列表项文本集合
	 * @param itemClickListener 列表项点击事件
	 */
	public void showFilterPopupWindow(View parentView,
									  List<String> itemTexts,
									  AdapterView.OnItemClickListener itemClickListener,
									  CustomerDismissListener dismissListener) {
		showFilterPopupWindow(parentView, itemTexts, itemClickListener, dismissListener, 0);
	}

	/**
	 * 列表选择popupWindow
	 *
	 * @param parentView        父View
	 * @param itemTexts         列表项文本集合
	 * @param itemClickListener 列表项点击事件
	 */
	public void showFilterPopupWindow(View parentView,
									  List<String> itemTexts,
									  AdapterView.OnItemClickListener itemClickListener,
									  CustomerDismissListener dismissListener, float alpha) {

		// 判断当前是否显示
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
		mPopupWindow = new CommonFilterPop(mContext, itemTexts);
		mPopupWindow.setOnDismissListener(dismissListener);
		// 绑定筛选点击事件
		mPopupWindow.setOnItemSelectedListener(itemClickListener);
		// 如果透明度设置为0的话,则默认设置为0.6f
		if (0 == alpha) {
			alpha = 0.6f;
		}
		// 设置背景透明度
		WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
		lp.alpha = alpha;
		((Activity)mContext).getWindow().setAttributes(lp);
		// 显示pop
		mPopupWindow.showAsDropDown(parentView);

	}

	/**
	 * Tab筛选栏切换
	 *
	 * @param isChecked         选中状态
	 * @param showView          展示pop的跟布局
	 * @param showMes           展示选择的数据
	 * @param itemClickListener 点击回调
	 * @param tabs              所有的cb(需要几个输入几个就可以,cb1,cb2....)
	 */
	public void filterTabToggle(boolean isChecked, View showView, List<String> showMes, AdapterView.OnItemClickListener itemClickListener, final CheckBox... tabs) {
		if (isChecked) {
			if (tabs.length <= 0) {
				return;
			}
			// 第一个checkBox为当前点击选中的cb,其他cb进行setChecked(false);
			for (int i = 1; i < tabs.length; i++) {
				tabs[i].setChecked(false);
			}

			showFilterPopupWindow(showView, showMes, itemClickListener, new CustomerDismissListener() {
				@Override
				public void onDismiss() {
					super.onDismiss();
					// 当pop消失时对第一个cb进行.setChecked(false)操作
					tabs[0].setChecked(false);
				}
			});
		} else {
			// 关闭checkBox时直接隐藏popuwindow
			hidePopListView();
		}
	}

	/**
	 * Tab筛选栏切换
	 *
	 * @param isChecked         选中状态
	 * @param showView          展示pop的跟布局
	 * @param showMes           展示选择的数据源
	 * @param itemClickListener 点击回调
	 * @param tabs              所有的cb(需要几个输入几个就可以,cb1,cb2....)
	 */
	public void filterTabToggleT(boolean isChecked, View showView, List<? extends BaseFilter> showMes, AdapterView.OnItemClickListener itemClickListener, final CheckBox... tabs) {
		if (isChecked) {
			if (tabs.length <= 0) {
				return;
			}
			// 第一个checkBox为当前点击选中的cb,其他cb进行setChecked(false);
			for (int i = 1; i < tabs.length; i++) {
				tabs[i].setChecked(false);
			}
			// 从数据源中提取出展示的筛选条件
			List<String> showStr = new ArrayList<String>();
			for (BaseFilter baseFilter : showMes) {
				showStr.add(baseFilter.getFilterStr());
			}
			showFilterPopupWindow(showView, showStr, itemClickListener, new CustomerDismissListener() {
				@Override
				public void onDismiss() {
					super.onDismiss();
					// 当pop消失时对第一个cb进行.setChecked(false)操作
					tabs[0].setChecked(false);
				}
			});
		} else {
			// 关闭checkBox时直接隐藏popuwindow
			hidePopListView();
		}
	}

	/**
	 * 自定义OnDismissListener
	 */
	public class CustomerDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			// 当pop消失的时候,重置背景色透明度
			WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
			lp.alpha = 1.0f;
			((Activity)mContext).getWindow().setAttributes(lp);
		}
	}

	/**
	 * 隐藏pop
	 */
	public void hidePopListView() {
		// 判断当前是否显示,如果显示则dismiss
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
	}
}
