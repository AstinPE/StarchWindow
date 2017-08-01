package me.astin.starch;

public class FLAG {
    private static int bit = 0;
    final static int FLAG_DECORATION_SYSTEM = 1 << bit++;
    final static int FLAG_DECORATION_MENU = FLAG_DECORATION_SYSTEM | 1 << bit++;
    final static int FLAG_DECORATION_CLOSE = FLAG_DECORATION_SYSTEM | 1 << bit++;
    final static int FLAG_DECORATION_TITLE = FLAG_DECORATION_SYSTEM | 1 << bit++;
    final static int FLAG_DECORATION_RESIZE = FLAG_DECORATION_SYSTEM | 1 << bit++;
    final static int FLAG_DECORATION_FRAME = FLAG_DECORATION_SYSTEM | 1 << bit++;
    final static int FLAG_WINDOW_RESIZEABLE = 1 << bit++;
    final static int FLAG_WINDOW_DRAGABLE = 1 << bit++;
    
    public static boolean equals(int a, int b) {
        return ((a&b) != 0);
    }
}
