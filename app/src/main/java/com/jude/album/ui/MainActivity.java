package com.jude.album.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jude.album.R;
import com.jude.album.domain.entities.Picture;
import com.jude.album.domain.entities.User;
import com.jude.album.model.AccountModel;
import com.jude.album.model.ImageModel;
import com.jude.album.presenter.MainPresenter;
import com.jude.album.ui.viewholder.ImageViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.utils.JUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BeamListActivity<MainPresenter,Picture>
        implements NavigationView.OnNavigationItemSelectedListener {

    PopularPagerAdapter mPagerAdapter;
    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;
    FloatingSearchView mSearchView;
    RollPagerView mVpBanner;

    TextView tvName;
    ImageView imgAvatar;
    ImageView imgGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        AccountModel.getInstance().checkUpdate(this);

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
        tvName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_name);
        imgAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        imgGender = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.img_gender);

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
        getListView().addItemDecoration(new SpaceDecoration(JUtils.dip2px(4)));
        getListView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }


    public void setAccount(User user){
        if(user == null){
            tvName.setText("点击登录");
            imgAvatar.setImageResource(R.mipmap.ic_launcher);
            imgGender.setImageBitmap(null);
            RxView.clicks(mNavigationView.getHeaderView(0))
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(v-> startActivity(new Intent(this,LoginActivity.class)));
        }else{
            tvName.setText(user.getName());
            Glide.with(this).load(ImageModel.getSmallImage(user.getAvatar()))
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(imgAvatar);
            imgGender.setImageResource(user.getGender()==0?R.mipmap.male:R.mipmap.female);
            RxView.clicks(mNavigationView.getHeaderView(0))
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(i -> getPresenter().startActivity(UserEditActivity.class));
        }
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
        getListView().scrollToPosition(0);
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
            RxView.clicks(imageView)
                    .throttleFirst(500, TimeUnit.MILLISECONDS)
                    .subscribe(i -> {
                        Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                        intent.putParcelableArrayListExtra(PictureActivity.KEY_PICTURES, (ArrayList<? extends Parcelable>) data);
                        intent.putExtra(PictureActivity.KEY_INDEX,position);
                        startActivity(intent);
                    });
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

    @Override
    protected ListConfig getConfig() {
        return super.getConfig().setLoadmoreAble(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_add ||
                id == R.id.nav_album ||
                id == R.id.nav_collection ||
                id == R.id.nav_star ||
                id == R.id.nav_fans ){
            if (!AccountModel.getInstance().hasLogin()){
                getPresenter().startActivity(LoginActivity.class);
                return true;
            }
        }

        switch (id){
            case R.id.nav_logout:
                AccountModel.getInstance().logout();
                break;
            case R.id.nav_album:
                getPresenter().startActivityWithData(AccountModel.getInstance().getCurrentAccount().getId(),UserPictureListActivity.class);
                break;
            case R.id.nav_add:
                getPresenter().startActivity(AddPictureActivity.class);
                break;
            case R.id.nav_collection:
                getPresenter().startActivityWithData(AccountModel.getInstance().getCurrentAccount().getId(),CollectionPictureActivity.class);
                break;
            case R.id.nav_fans:
                getPresenter().startActivityWithData(AccountModel.getInstance().getCurrentAccount().getId(),UserFansActivity.class);
                break;
            case R.id.nav_star:
                getPresenter().startActivityWithData(AccountModel.getInstance().getCurrentAccount().getId(),UserStarActivity.class);
                break;
            case R.id.nav_about:
                getPresenter().startActivity(AboutActivity.class);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
