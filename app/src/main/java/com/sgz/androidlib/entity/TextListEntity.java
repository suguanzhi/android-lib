package com.sgz.androidlib.entity;

import com.android.sgzcommon.dialog.entity.TextListItem;

/**
 * @author sgz
 * @date 2020/6/30
 */
public class TextListEntity extends TextListItem {

    private String name;

    public TextListEntity(String name) {
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
