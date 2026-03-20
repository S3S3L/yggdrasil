package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 布局参数基类
 * 用于传递布局相关的参数
 */
public class LayoutParams {

    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    private int width;
    private int height;
    private float margin;
    private float marginLeft;
    private float marginRight;
    private float marginTop;
    private float marginBottom;
    private float padding;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    private float paddingBottom;
    private Object tag;

    public LayoutParams() {
        this(WRAP_CONTENT, WRAP_CONTENT);
    }

    public LayoutParams(int width, int height) {
        this.width = width;
        this.height = height;
        this.margin = 0;
        this.marginLeft = 0;
        this.marginRight = 0;
        this.marginTop = 0;
        this.marginBottom = 0;
        this.padding = 0;
        this.paddingLeft = 0;
        this.paddingRight = 0;
        this.paddingTop = 0;
        this.paddingBottom = 0;
    }

    public LayoutParams(LayoutParams source) {
        this.width = source.width;
        this.height = source.height;
        this.margin = source.margin;
        this.marginLeft = source.marginLeft;
        this.marginRight = source.marginRight;
        this.marginTop = source.marginTop;
        this.marginBottom = source.marginBottom;
        this.padding = source.padding;
        this.paddingLeft = source.paddingLeft;
        this.paddingRight = source.paddingRight;
        this.paddingTop = source.paddingTop;
        this.paddingBottom = source.paddingBottom;
        this.tag = source.tag;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getMargin() {
        return margin;
    }

    public void setMargin(float margin) {
        this.margin = margin;
        this.marginLeft = margin;
        this.marginRight = margin;
        this.marginTop = margin;
        this.marginBottom = margin;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
        this.paddingLeft = padding;
        this.paddingRight = padding;
        this.paddingTop = padding;
        this.paddingBottom = padding;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 设置四边边距
     */
    public void setMargins(float left, float top, float right, float bottom) {
        this.marginLeft = left;
        this.marginTop = top;
        this.marginRight = right;
        this.marginBottom = bottom;
    }

    /**
     * 设置四边内边距
     */
    public void setPaddings(float left, float top, float right, float bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
    }

    /**
     * 获取总左边距
     */
    public float getTotalMarginLeft() {
        return marginLeft;
    }

    /**
     * 获取总右边距
     */
    public float getTotalMarginRight() {
        return marginRight;
    }

    /**
     * 获取总上边距
     */
    public float getTotalMarginTop() {
        return marginTop;
    }

    /**
     * 获取总下边距
     */
    public float getTotalMarginBottom() {
        return marginBottom;
    }

    /**
     * 复制布局参数
     */
    public LayoutParams copy() {
        return new LayoutParams(this);
    }
}
