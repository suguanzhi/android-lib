package com.sgz.androidlib.entity;

import com.android.sgzcommon.dialog.entity.LoadListItem;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class LoadListEntity extends LoadListItem {

    private String name;

    public LoadListEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getItemName() {
        return name;
    }
}
