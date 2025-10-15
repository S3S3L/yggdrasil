package io.github.s3s3l.yggdrasil.game.gdx.consts;

public enum Align {
    CENTER, LEFT, RIGHT, TOP, BOTTOM;
    
    public static Align fromString(String str) {
        for (Align align : values()) {
            if (align.name().equalsIgnoreCase(str)) {
                return align;
            }
        }
        return LEFT;
    }
}
