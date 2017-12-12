package com.yywf.fragmenttab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.yywf.R;


public class TabItemImpl<T> extends TabItem<T> implements TabListener {

	public TabItemImpl(Context ctx, String mTag, Class<T> mClass) {
		super(ctx, mTag, mClass);
	}

	public TabItemImpl(Context ctx, String mTag, Class<T> mClass, Bundle mArgs) {
		super(ctx, mTag, mClass, mArgs);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onTabSelected(TabItem tab, FragmentTransaction ft) {
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mContext, mClass.getName(), mArgs);
			ft.add(R.id.content, mFragment, mTag);
		} else {
			ft.attach(mFragment);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onTabUnselected(TabItem tab, FragmentTransaction ft) {
		if (mFragment != null) {
			ft.remove(mFragment);
			mFragment = null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onTabReselected(TabItem tab, FragmentTransaction ft) {
		// ToDo
	}

}
