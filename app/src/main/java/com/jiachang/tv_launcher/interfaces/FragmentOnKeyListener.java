package com.jiachang.tv_launcher.interfaces;

import android.view.KeyEvent;

/**
 * @author Mickey.Ma
 * @date 2020-05-20
 * @description
 */
public interface FragmentOnKeyListener {

    public boolean onKeyDowns(int keyCode, KeyEvent event);
    public void onWindowFocusChanged(boolean hasFocus);
}
