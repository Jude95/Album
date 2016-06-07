package com.jude.album.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;


/**
* Created by Mr.Jude on 2015/2/22.
 * 展示图片的Activity
*/
public class PictureActivity extends BeamBaseActivity {
    public static String KEY_PICTURES = "pictures";
    public static String KEY_PICTURE = "picture";
    public static String KEY_INDEX = "index";

    private ViewPager mViewPager;
    private PictureFragmentAdapter mAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewpager_inner);
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(mViewPager);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        mAdapter = new PictureFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        ArrayList<Picture> pictures = getIntent().getParcelableArrayListExtra(KEY_PICTURES);
        if (pictures == null)pictures = new ArrayList<>();
        Picture picture =  getIntent().getParcelableExtra(KEY_PICTURE);
        if (picture!=null) pictures.add(picture);
        int index = getIntent().getIntExtra(KEY_INDEX,0);

        mAdapter.setPictures(pictures);
        mViewPager.setCurrentItem(index);

        new Handler().post(()-> Log.i("Test","Hello1"));
        runOnUiThread(()-> Log.i("Test","Hello2"));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    class PictureFragmentAdapter extends FragmentStatePagerAdapter{
        ArrayList<Picture> mPictures;

        public PictureFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setPictures(ArrayList<Picture> pictures){
            mPictures = pictures;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new PictureFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("picture",mPictures.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mPictures==null?0:mPictures.size();
        }
    }



}
