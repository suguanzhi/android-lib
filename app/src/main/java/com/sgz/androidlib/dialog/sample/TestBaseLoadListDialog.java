package com.sgz.androidlib.dialog.sample;

import android.content.Context;

import com.android.sgzcommon.dialog.BaseLoadListDialog;
import com.sgz.androidlib.entity.LoadListEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class TestBaseLoadListDialog extends BaseLoadListDialog<LoadListEntity> {

    private List<LoadListEntity> mEntityList;

    @Override
    protected void loadList(Map<String, String> data, OnLoadListListener listener) {
        listener.onStart();
        listener.onFailed();
    }

    public TestBaseLoadListDialog(Context context) {
        super(context);
        mEntityList = new ArrayList<>();
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
        mEntityList.add(new LoadListEntity("深刻的反思了的"));
    }
}
