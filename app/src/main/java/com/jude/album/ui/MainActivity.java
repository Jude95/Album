package com.jude.album.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.model.ImageModel;
import com.jude.album.presenter.MainPresenter;
import com.jude.album.ui.viewholder.ImageViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.List;
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BeamListActivity<MainPresenter,Picture>
        implements NavigationView.OnNavigationItemSelectedListener {

    PopularPagerAdapter mPagerAdapter;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    FloatingSearchView mSearchView;
    RollPagerView mVpBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);

        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }

            @Override
            public void onMenuClosed() {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mSearchView!=null){
                    mSearchView.setLeftMenuOpen(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mSearchView!=null){
                    mSearchView.setLeftMenuOpen(false);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        getListView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(parent);
    }

    public void refreshPopular(List<Picture> list) {
        getPresenter().getAdapter().removeAllHeader();
        getPresenter().getAdapter().addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.header_main,parent,false);
                mVpBanner = (RollPagerView) view.findViewById(R.id.vp_banner);
                mPagerAdapter = new PopularPagerAdapter(mVpBanner);
                mPagerAdapter.refresh(list);
                mVpBanner.setAdapter(mPagerAdapter);
                return view;
            }

            @Override
            public void onBindView(View headerView) {
            }
        });
        getPresenter().getAdapter().notifyDataSetChanged();
    }

    class PopularPagerAdapter extends LoopPagerAdapter {
        List<Picture> data;

        public PopularPagerAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        public void refresh(List<Picture> list) {
            data = list;
            notifyDataSetChanged();
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(MainActivity.this)
                    .load(ImageModel.getMiddleImage(data.get(position).getSrc()))
                    .into(imageView);
            return imageView;
        }

        @Override
        protected int getRealCount() {
            return data==null?0:data.size();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
