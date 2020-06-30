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
        LoadListEntity entity = new LoadListEntity();
        entity.setItemName("四点零分");
        mEntityList.add(entity);
        LoadListEntity entity1 = new LoadListEntity();
        entity1.setItemName("四点零分");
        mEntityList.add(entity1);
        LoadListEntity entity2 = new LoadListEntity();
        entity2.setItemName("四点零分");
        mEntityList.add(entity2);
        LoadListEntity entity3 = new LoadListEntity();
        entity3.setItemName("四点零分");
        mEntityList.add(entity3);
        LoadListEntity entity4 = new LoadListEntity();
        entity4.setItemName("四点零分");
        mEntityList.add(entity4);
        LoadListEntity entity5 = new LoadListEntity();
        entity5.setItemName("四点零分");
        mEntityList.add(entity5);
    }

    @Override
    protected void loadList(BaseLoadListDialog.OnLoadListListener listener) {
        listener.onStart();
        listener.onSuccess(mEntityList);
    }
}
