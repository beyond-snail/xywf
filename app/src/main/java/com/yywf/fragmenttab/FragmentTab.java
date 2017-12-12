package com.yywf.fragmenttab;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.HashMap;

public class FragmentTab {

	FragmentManager mFragmentManager;
	@SuppressWarnings("rawtypes")
	HashMap<String, TabItem> mTabItems = new HashMap<String, TabItem>();
	@SuppressWarnings("rawtypes")
	TabItem mSelectedTabItem;

	public FragmentTab(FragmentManager mFragmentManager) {
		this.mFragmentManager = mFragmentManager;
	}

	@SuppressWarnings("rawtypes")
	public void addTabItem(TabItem item) {
		mTabItems.put(item.getTag(), item);
	}

	public void selectTab(String tag) {
		selectTab(mTabItems.get(tag));
	}
	
	@SuppressWarnings("rawtypes")
	public void selectTab(TabItem tabItem) {
		final FragmentTransaction trans = mFragmentManager.beginTransaction()
				.disallowAddToBackStack();

		if (mSelectedTabItem == tabItem) {
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabReselected(mSelectedTabItem, trans);
			}
		} else {
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabUnselected(mSelectedTabItem, trans);
			}
			mSelectedTabItem = tabItem;
			if (mSelectedTabItem != null) {
				mSelectedTabItem.onTabSelected(mSelectedTabItem, trans);
			}
		}

		if (!trans.isEmpty()) {
			trans.commit();
		}
	}

}
