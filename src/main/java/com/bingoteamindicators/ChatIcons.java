package com.bingoteamindicators;


public enum ChatIcons {
    ONE("/one.png"),
    TWO("/two.png"),
    THREE("/three.png"),
    FOUR("/four.png"),
    FIVE("/five.png"),
    SIX("/six.png"),
    SEVEN("/seven.png"),
    EIGHT("/eight.png"),
    NINE("/nine.png"),
    TEN("/ten.png"),
    ELEVEN("/eleven.png"),
    TWELVE("/twelve.png"),
    THIRTEEN("/thirteen.png"),
    FOURTEEN("/fourteen.png"),
    FIFTEEN("/fifteen.png");

    private final String path;
    ChatIcons(String path) { this.path = path; }
    String getIconPath() {return path;}
}
