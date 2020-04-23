package com.android.sgzcommon.downloadtask;

public class ThreadInfo {

    private int id;
    private long start;
    private long end;
    private long finished;
    private String url;

    public ThreadInfo() {

    }

    public ThreadInfo(int id, long start, long end, long finished, String url) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.finished = finished;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
