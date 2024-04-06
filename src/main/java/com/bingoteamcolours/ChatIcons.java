package com.bingoteamcolours;


public enum ChatIcons {
    ONE("/one.png"),
    TWO("/two.png"),
    THREE("/three.png"),
    FOUR("/four.png"),
    FIVE("/five.png"),
    SIX("/six.png"),
    SEVEN("/seven.png");

    private final String path;
    ChatIcons(String path) { this.path = path; }
    String getIconPath() {return path;}
}
