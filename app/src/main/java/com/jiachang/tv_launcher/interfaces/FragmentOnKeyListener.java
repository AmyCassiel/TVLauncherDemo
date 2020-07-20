package com.jiachang.tv_launcher.interfaces;

import android.view.KeyEvent;

/**
 * @author Mickey.Ma
 * @date 2020-05-20
 * @description
 */
public interface FragmentOnKeyListener {

    boolean onKeyDowns(int keyCode, KeyEvent event);
    void onWindowFocusChanged(boolean hasFocus);
}
