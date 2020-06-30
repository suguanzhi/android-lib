package com.sgz.androidlib;

import android.content.Context;

import com.android.sgzcommon.dialog.BaseLoadListDialog;
import com.sgz.androidlib.entity.LoadListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class TestBaseLoadListDialog extends BaseLoadListDialog<LoadListEntity> {

    private List<LoadListEntity> mEntityList;

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

    @Override
    protected void loadList(BaseLoadListDialog.OnLoadListListener listener) {
        listener.onStart();
        listener.onSuccess(mEntityList);
    }
}
