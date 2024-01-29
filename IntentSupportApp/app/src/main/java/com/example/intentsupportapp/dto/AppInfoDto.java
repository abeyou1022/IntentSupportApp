package com.example.intentsupportapp.dto;

import android.graphics.drawable.Drawable;

public class AppInfoDto {
    private String label;
    private Drawable drawable;
    private String packageName;

    public AppInfoDto(String label, Drawable drawable, String packageName) {
        this.label = label;
        this.drawable = drawable;
        this.packageName = packageName;
    }

    public String getLabel() {
        return this.label;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public String getPackageName() {
        return this.packageName;
    }

}