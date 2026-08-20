package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * Backend-neutral, premultiplied-agnostic RGBA color components in the [0, 1] range.
 */
public record UiColor(float red, float green, float blue, float alpha) {

    public UiColor {
        validate(red, "red");
        validate(green, "green");
        validate(blue, "blue");
        validate(alpha, "alpha");
    }

    private static void validate(float value, String name) {
        if (Float.isNaN(value) || value < 0 || value > 1) {
            throw new IllegalArgumentException(name + " must be between 0 and 1");
        }
    }
}
