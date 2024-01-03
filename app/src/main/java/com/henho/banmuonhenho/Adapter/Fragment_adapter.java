package com.henho.banmuonhenho.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.henho.banmuonhenho.Fragment.Fragment_canhan;
import com.henho.banmuonhenho.Fragment.Fragment_phongchat;
import com.henho.banmuonhenho.Fragment.Fragment_tinnhan;
import com.henho.banmuonhenho.Fragment.Fragment_trangchu;

public class Fragment_adapter extends FragmentStatePagerAdapter {
    public Fragment_adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_trangchu();
            case 1:
                return new Fragment_phongchat();
            case 2:
                return new Fragment_tinnhan();
            case 3:
                return new Fragment_canhan();
            default:
                return new Fragment_trangchu();
        }}

    @Override
    public int getCount() {
        return 4;
    }
}
