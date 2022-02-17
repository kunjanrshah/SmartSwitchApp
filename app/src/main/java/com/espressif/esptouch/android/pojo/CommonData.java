package com.espressif.esptouch.android.pojo;

public class CommonData {
    private static CommonData INSTANCE = null;
    private Root root = null;

    private CommonData() {
    }

    public static CommonData getInstance() {
        if (INSTANCE == null) {
            synchronized (CommonData.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CommonData();
                }
            }
        }
        return INSTANCE;
    }

    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }
}
