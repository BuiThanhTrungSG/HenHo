package com.love.henho.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.love.henho.Fragment.Fragment_canhan;
import com.love.henho.Fragment.Fragment_tinnhan;
import com.love.henho.Fragment.Fragment_trangchu;
import com.love.henho.MainActivity;

public class Fragment_adapter extends FragmentStateAdapter {
    public Fragment_adapter(MainActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Fragment_trangchu();
            case 1:
                return new Fragment_tinnhan();
            case 2:
                return new Fragment_canhan();
            default:
                return new Fragment_trangchu();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        switch (position){
//            case 0:
//                return new Fragment_trangchu();
//            case 1:
//                return new Fragment_phongchat();
//            case 2:
//                return new Fragment_tinnhan();
//            case 3:
//                return new Fragment_canhan();
//            default:
//                return new Fragment_trangchu();
//        }
//    }

//    @Override
//    public int getCount() {
//        return 4;
//    }
}
