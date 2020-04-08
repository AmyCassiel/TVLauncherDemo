package com.jiachang.tv_launcher.utils;

import android.util.ArrayMap;
import android.view.KeyEvent;

/**
 * @author Mickey.Ma
 * @date 2020-03-27
 * @description
 */
public class RemoteControlKeyEvent {
        /**
         * 直播
         */
        public static final int BTV = 1181;

        /**
         * 点播
         */
        public static final int VOD = 1182;

        /**
         * 回看
         */
        public static final int TVOD = 1184;

        /**
         * VOD快进
         */
        public static final int VOD_FAST_FORWARD = KeyEvent.KEYCODE_MEDIA_FAST_FORWARD;

        /**
         * VOD快退
         */
        public static final int VOD_FAST_REWIND = KeyEvent.KEYCODE_MEDIA_REWIND;

        /**
         * 媒体播放暂停
         */
        public static final int MEDIA_PAUSE_PLAY = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;

        /**
         * 频道+
         */
        public static final int CHANNEL_UP = KeyEvent.KEYCODE_CHANNEL_UP;

        /**
         * 频道-
         */
        public static final int CHANNEL_DOWN = KeyEvent.KEYCODE_CHANNEL_DOWN;

        /**
         * 在直播界面切换出Infoview菜单
         */
        public static final int CHANNEL_MENU = KeyEvent.KEYCODE_MENU;

        /**
         * 显示直播频道页
         */
        public static final int SHOW_EPGVIEW = KeyEvent.KEYCODE_DPAD_RIGHT;

        private static RemoteKeyEvent mKeyEvent;

        private ArrayMap<Integer, Integer> mBaseKeyCodeValue = new ArrayMap<>();

        private ArrayMap<Integer, Integer> mVODKeyCodeValue = new ArrayMap<>();

        private ArrayMap<Integer, Integer> mBTVKeyCodeValue = new ArrayMap<>();

        public int getKeyCodeValue(int code) {
            if (mBaseKeyCodeValue.containsKey(code)) {
                return mBaseKeyCodeValue.get(code);
            }
            return code;
        }

        public int getVODKeyCodeValue(int code) {
            if (mVODKeyCodeValue.containsKey(code)) {
                return mVODKeyCodeValue.get(code);
            }
            return code;
        }

        public int getBTVKeyCodeValue(int code) {
            if (mBTVKeyCodeValue.containsKey(code)) {
                return mBTVKeyCodeValue.get(code);
            }
            return code;
        }
        public static synchronized RemoteKeyEvent getInstance() {
            if (null == mKeyEvent) {
                mKeyEvent = new RemoteKeyEvent();
            }
            return mKeyEvent;
        }

    private static class RemoteKeyEvent {
    }

        /*private class RemoteKeyEvent {
            //BTV 直播
            mBaseKeyCodeValue.put(BTV, BTV);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_RED, BTV);

            //TVOD 回看
            mBaseKeyCodeValue.put(TVOD, TVOD);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_GREEN, TVOD);

            //点播
            mBaseKeyCodeValue.put(VOD, VOD);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_PROG_YELLOW, VOD);

            //0-9按键
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_0, 0);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_1, 1);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_2, 2);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_3, 3);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_4, 4);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_5, 5);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_6, 6);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_7, 7);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_8, 8);
            mBaseKeyCodeValue.put(KeyEvent.KEYCODE_9, 9);

            //VOD快进,快退
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_RIGHT, VOD_FAST_FORWARD);
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_PAGE_DOWN, VOD_FAST_FORWARD);
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_LEFT, VOD_FAST_REWIND);
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_PAGE_UP, VOD_FAST_REWIND);

            //VOD媒体播放暂停code
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_CENTER, MEDIA_PAUSE_PLAY);
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_ENTER, MEDIA_PAUSE_PLAY);
            mVODKeyCodeValue.put(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, MEDIA_PAUSE_PLAY);

            //频道+
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_CHANNEL_UP, CHANNEL_UP);
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_UP, CHANNEL_UP);

            //频道-
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_CHANNEL_DOWN, CHANNEL_DOWN);
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_DOWN, CHANNEL_DOWN);

            //channel menu，项目需求,直播页点击DPAD_LEFT可以呼出菜单键
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_LEFT, CHANNEL_MENU);
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_MENU, CHANNEL_MENU);

            //直播频道页
            mBTVKeyCodeValue.put(KeyEvent.KEYCODE_DPAD_RIGHT, SHOW_EPGVIEW);
        }*/
}
