package com.jiachang.tv_launcher;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @author Mickey.Ma
 * @date 2020-06-03
 * @description
 */
public class AdapterViewPager extends FragmentStatePagerAdapter {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;
    private int fragment;
    Fragment currentFragment;

    public AdapterViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        this.currentFragment = (Fragment) object;
    }
}
