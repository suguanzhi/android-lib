package com.sgz.androidlib.dialog.sample;

import android.content.Context;

import com.android.sgzcommon.dialog.BaseTextListDialog;
import com.sgz.androidlib.entity.TextListEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class TestBaseTextListDialog extends BaseTextListDialog<TextListEntity> {

    private List<TextListEntity> mEntityList;

    @Override
    protected void loadList(Map<String, String> data, OnLoadListResponse response) {
        response.onStart();
        response.onSuccess(mEntityList);
    }

    public TestBaseTextListDialog(Context context) {
        super(context);
        mEntityList = new ArrayList<>();
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
//        mEntityList.add(new TextListEntity("深刻的反思了的"));
    }
}
