package com.yywf.fragmenttab;

import android.support.v4.app.FragmentTransaction;

public interface TabListener {
	
	@SuppressWarnings("rawtypes")
	public void onTabSelected(TabItem tab, FragmentTransaction ft);

    @SuppressWarnings("rawtypes")
	public void onTabUnselected(TabItem tab, FragmentTransaction ft);

    @SuppressWarnings("rawtypes")
	public void onTabReselected(TabItem tab, FragmentTransaction ft);

}
