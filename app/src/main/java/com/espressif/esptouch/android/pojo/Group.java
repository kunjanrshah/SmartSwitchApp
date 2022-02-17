package com.espressif.esptouch.android.pojo;

public class Group {
    public String name = "";
    public int boardId;
    public int switchId;
    public int floorId;
    public boolean selected;

    public Group(String name, int floorId, int boardId, int switchId) {
        this.name = name;
        this.boardId = boardId;
        this.switchId = switchId;
        this.floorId = floorId;
    }
}
