package com.jude.album.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import com.jude.album.domain.entities.User;
import com.jude.album.presenter.UserFansPresenter;
import com.jude.album.ui.viewholder.UserViewHolder;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

/**
 * Created by Mr.Jude on 2016/6/14.
 */
@RequiresPresenter(UserFansPresenter.class)
public class UserFansActivity extends BeamListActivity<UserFansPresenter,User> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DividerDecoration dividerDecoration = new DividerDecoration(Color.GRAY, Util.dip2px(this,0.5f), Util.dip2px(this,72),0);
        dividerDecoration.setDrawLastItem(false);
        getListView().addItemDecoration(dividerDecoration);
    }

    @Override
    protected BaseViewHolder<User> getViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(parent);
    }
}
