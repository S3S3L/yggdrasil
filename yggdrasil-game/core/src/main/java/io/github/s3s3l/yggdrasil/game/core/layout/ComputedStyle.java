package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 经过长度计算后的样式数据，所有长度单位都转换为像素（float）。
 */
public class ComputedStyle {
    /** display，决定布局模式 */
    private final DisplayType display;
    /** position 类型 */
    private final PositionType positionType;
    /** float 类型 */
    private final FloatType floatType;
    /** clear 类型 */
    private final ClearType clearType;

    /** 解析后的宽度 */
    private final float width;
    /** 解析后的高度 */
    private final float height;
    /** 最小宽度 */
    private final float minWidth;
    /** 最小高度 */
    private final float minHeight;
    /** 最大宽度 */
    private final float maxWidth;
    /** 最大高度 */
    private final float maxHeight;

    /** 上外边距 */
    private final float marginTop;
    /** 右外边距 */
    private final float marginRight;
    /** 下外边距 */
    private final float marginBottom;
    /** 左外边距 */
    private final float marginLeft;

    /** 上内边距 */
    private final float paddingTop;
    /** 右内边距 */
    private final float paddingRight;
    /** 下内边距 */
    private final float paddingBottom;
    /** 左内边距 */
    private final float paddingLeft;

    /** 宽高比 */
    private final float aspectRatio;

    /** 溢出控制 */
    private final OverflowType overflow;
    /** 横向溢出控制 */
    private final OverflowType overflowX;
    /** 纵向溢出控制 */
    private final OverflowType overflowY;

    /** flex 容器属性（已默认化） */
    private final FlexContainer flexContainer;
    /** flex 子项属性（已默认化） */
    private final FlexItem flexItem;
    /** grid 容器属性（已默认化） */
    private final GridContainer gridContainer;
    /** grid 子项属性（已默认化） */
    private final GridItem gridItem;
    /** 行间距（flex/grid 解析后） */
    private final float rowGap;
    /** 列间距（flex/grid 解析后） */
    private final float columnGap;

    /**
     * 从原始样式和可用空间计算像素化后的样式。
     */
    public static ComputedStyle from(LayoutStyle style, float availableWidth, float availableHeight,
            float viewportWidth, float viewportHeight) {
        LengthResolver resolver = new LengthResolver(availableWidth, availableHeight, viewportWidth, viewportHeight);

        float width = resolver.resolve(style.getBoxModel().getSize().getWidth(), availableWidth);
        float height = resolver.resolve(style.getBoxModel().getSize().getHeight(), availableHeight);
        float minWidth = resolver.resolve(style.getBoxModel().getSize().getMinWidth(), availableWidth);
        float minHeight = resolver.resolve(style.getBoxModel().getSize().getMinHeight(), availableHeight);
        float maxWidth = resolver.resolve(style.getBoxModel().getSize().getMaxWidth(), availableWidth);
        float maxHeight = resolver.resolve(style.getBoxModel().getSize().getMaxHeight(), availableHeight);

        Spacing margin = style.getBoxModel().getMargin();
        float marginTop = resolver.resolve(margin.getTop(), availableWidth);
        float marginRight = resolver.resolve(margin.getRight(), availableWidth);
        float marginBottom = resolver.resolve(margin.getBottom(), availableWidth);
        float marginLeft = resolver.resolve(margin.getLeft(), availableWidth);

        Spacing padding = style.getBoxModel().getPadding();
        float paddingTop = resolver.resolve(padding.getTop(), availableWidth);
        float paddingRight = resolver.resolve(padding.getRight(), availableWidth);
        float paddingBottom = resolver.resolve(padding.getBottom(), availableWidth);
        float paddingLeft = resolver.resolve(padding.getLeft(), availableWidth);

        FlexContainer flexContainer = style.getFlexContainer();
        if (flexContainer == null) {
            flexContainer = new FlexContainer();
        }
        FlexItem flexItem = style.getFlexItem();
        if (flexItem == null) {
            flexItem = new FlexItem();
        }
        GridContainer gridContainer = style.getGridContainer();
        if (gridContainer == null) {
            gridContainer = new GridContainer();
        }
        GridItem gridItem = style.getGridItem();
        if (gridItem == null) {
            gridItem = new GridItem();
        }

        float rowGap = resolver.resolve(gridContainer.getRowGap(), availableWidth);
        float columnGap = resolver.resolve(gridContainer.getColumnGap(), availableWidth);
        // flex gap复用
        if (style.getDisplay() == DisplayType.FLEX || style.getDisplay() == DisplayType.INLINE_FLEX) {
            rowGap = resolver.resolve(flexContainer.getRowGap(), availableWidth);
            columnGap = resolver.resolve(flexContainer.getColumnGap(), availableWidth);
        }

        return new ComputedStyle(style.getDisplay(), style.getPositionType(), style.getFloatType(), style.getClearType(),
            width, height, minWidth, minHeight, maxWidth, maxHeight,
            marginTop, marginRight, marginBottom, marginLeft,
            paddingTop, paddingRight, paddingBottom, paddingLeft,
            style.getAspectRatio(), style.getOverflow(), style.getOverflowX(), style.getOverflowY(),
            flexContainer, flexItem, gridContainer, gridItem, rowGap, columnGap);
    }

        public ComputedStyle(DisplayType display, PositionType positionType, FloatType floatType, ClearType clearType,
            float width, float height, float minWidth, float minHeight, float maxWidth, float maxHeight,
            float marginTop, float marginRight, float marginBottom, float marginLeft,
            float paddingTop, float paddingRight, float paddingBottom, float paddingLeft,
            float aspectRatio, OverflowType overflow, OverflowType overflowX, OverflowType overflowY,
            FlexContainer flexContainer, FlexItem flexItem, GridContainer gridContainer, GridItem gridItem,
            float rowGap, float columnGap) {
        this.display = display;
        this.positionType = positionType;
        this.floatType = floatType;
        this.clearType = clearType;
        this.width = width;
        this.height = height;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
        this.paddingLeft = paddingLeft;
        this.aspectRatio = aspectRatio;
        this.overflow = overflow;
        this.overflowX = overflowX;
        this.overflowY = overflowY;
        this.flexContainer = flexContainer;
        this.flexItem = flexItem;
        this.gridContainer = gridContainer;
        this.gridItem = gridItem;
        this.rowGap = rowGap;
        this.columnGap = columnGap;
    }

    public DisplayType getDisplay() {
        return display;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public FloatType getFloatType() {
        return floatType;
    }

    public ClearType getClearType() {
        return clearType;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getMinWidth() {
        return minWidth;
    }

    public float getMinHeight() {
        return minHeight;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public float getMarginTop() {
        return marginTop;
    }

    public float getMarginRight() {
        return marginRight;
    }

    public float getMarginBottom() {
        return marginBottom;
    }

    public float getMarginLeft() {
        return marginLeft;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public OverflowType getOverflowX() {
        return overflowX;
    }

    public OverflowType getOverflowY() {
        return overflowY;
    }

    public FlexContainer getFlexContainer() {
        return flexContainer;
    }

    public FlexItem getFlexItem() {
        return flexItem;
    }

    public GridContainer getGridContainer() {
        return gridContainer;
    }

    public GridItem getGridItem() {
        return gridItem;
    }

    public float getRowGap() {
        return rowGap;
    }

    public float getColumnGap() {
        return columnGap;
    }
}

/**
 * 简单的长度解析器，用于将长度单位转换为像素值。
 */
class LengthResolver {
    private final float viewportWidth;
    private final float viewportHeight;

    LengthResolver(float availableWidth, float availableHeight, float viewportWidth, float viewportHeight) {
        this.viewportWidth = Math.max(1, viewportWidth);
        this.viewportHeight = Math.max(1, viewportHeight);
    }

    float resolve(LengthUnit unit, float fallbackAvailable) {
        if (unit == null) {
            return Float.NaN;
        }
        switch (unit.getUnit()) {
            case PX:
                return unit.getValue();
            case PERCENT:
                return unit.getValue() * fallbackAvailable / 100f;
            case VH:
                return unit.getValue() * viewportHeight / 100f;
            case VW:
                return unit.getValue() * viewportWidth / 100f;
            case FR:
                return unit.getValue();
            case AUTO:
            default:
                return Float.NaN;
        }
    }
}
