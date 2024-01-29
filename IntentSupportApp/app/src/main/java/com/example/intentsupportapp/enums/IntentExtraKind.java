package com.example.intentsupportapp.enums;

/**
 * 対応するインテントのパラメータ型
 */
public enum IntentExtraKind {
    INT("--ei"),    //int
    STRING("--es"); //String

    private String extraCommand; //ListViewに表示する際の文字列

    IntentExtraKind(String extraCommand) {
        this.extraCommand = extraCommand;
    }

    public String getExtraCommand() {
        return extraCommand;
    }
}
