package com.android.sgzcommon.view.adplayer;

/**
 * Created by sgz on 2018/3/22 0022.
 */

public class AdResource {

    protected TYPE type;
    protected int position;
    protected long duration;
    protected long duration2;
    protected long startTime;
    protected long endTime;
    protected boolean isDurationUsed;
    protected String path;
    protected String mark;

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration2() {
        return duration2;
    }

    public void setDuration2(long duration2) {
        this.duration2 = duration2;
    }

    public boolean isDurationUsed() {
        return isDurationUsed;
    }

    public void setDurationUsed(boolean durationUsed) {
        isDurationUsed = durationUsed;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public enum TYPE {
        IMAGE(1), AUDIO(2), VEDIO(3), WEB(4), LIVE(8);

        private int value;

        TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static TYPE getType(int value) {
            switch (value) {
                case 1:
                    return IMAGE;
                case 2:
                    return AUDIO;
                case 3:
                    return VEDIO;
                case 4:
                    return WEB;
                case 8:
                    return LIVE;
            }
            return null;
        }
    }
}
