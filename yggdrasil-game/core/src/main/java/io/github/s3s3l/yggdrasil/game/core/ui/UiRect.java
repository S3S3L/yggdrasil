package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * An axis-aligned UI rectangle. Right and bottom edges are exclusive.
 */
public record UiRect(float x, float y, float width, float height) {

    public UiRect {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public boolean contains(float pointX, float pointY) {
        return pointX >= x && pointX < x + width && pointY >= y && pointY < y + height;
    }

    public UiRect intersection(UiRect other) {
        float left = Math.max(x, other.x);
        float top = Math.max(y, other.y);
        float right = Math.min(x + width, other.x + other.width);
        float bottom = Math.min(y + height, other.y + other.height);
        return new UiRect(left, top, Math.max(0, right - left), Math.max(0, bottom - top));
    }
}
