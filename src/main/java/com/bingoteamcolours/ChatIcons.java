package com.bingoteamcolours;


public enum ChatIcons {
    SELECT_A_COLOUR(""),
    RED("/red.png"),
    BLACK("/black.png"),
    BLUE("/blue.png"),
    CYAN("/cyan.png"),
    DARK_GREEN("/darkGreen.png"),
    LIGHT_GREEN("/lightGreen.png"),
    ORANGE("/orange.png"),
    PINK("/pink.png"),
    PURPLE("/purple.png"),
    WHITE("/white.png"),
    YELLOW("/yellow.png");

    private final String path;
    ChatIcons(String path) { this.path = path; }
    String getIconPath() {return path;}
}
