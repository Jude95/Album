package com.jude.album.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.PictureModel;
import com.jude.album.model.server.ErrorTransform;
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

    private ArrayList<Picture> mPictures;

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

        mPictures = getIntent().getParcelableArrayListExtra(KEY_PICTURES);
        if (mPictures == null) mPictures = new ArrayList<>();
        Picture picture =  getIntent().getParcelableExtra(KEY_PICTURE);
        if (picture!=null) mPictures.add(picture);
        int index = getIntent().getIntExtra(KEY_INDEX,0);
        updateWatchCount(index);

        mAdapter.setPictures(mPictures);
        mViewPager.setCurrentItem(index);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateWatchCount(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateWatchCount(int position){
        mPictures.get(position).setWatchCount(mPictures.get(position).getWatchCount()+1);
        PictureModel.getInstance().updateWatchCount(mPictures.get(position).getId())
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.NONE))
                .subscribe();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    static class PictureFragmentAdapter extends FragmentStatePagerAdapter{
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
