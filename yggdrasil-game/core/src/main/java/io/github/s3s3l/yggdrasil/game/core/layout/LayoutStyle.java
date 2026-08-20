package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 布局样式主类
 * 整合了所有布局相关的属性，包括显示类型、定位、盒模型、Flexbox、Grid等
 */
public class LayoutStyle {
    // 显示和定位相关
    /** 显示类型 */
    private DisplayType display = DisplayType.BLOCK;
    /** 定位类型 */
    private PositionType positionType = PositionType.STATIC;
    /** 定位偏移 */
    private Position position = new Position();
    /** 浮动类型 */
    private FloatType floatType = FloatType.NONE;
    /** 清除浮动类型 */
    private ClearType clearType = ClearType.NONE;

    // 盒模型
    /** 盒模型属性 */
    private BoxModel boxModel = new BoxModel();

    // 溢出处理
    /** 溢出处理方式 */
    private OverflowType overflow = OverflowType.VISIBLE;
    /** 水平方向溢出处理 */
    private OverflowType overflowX = OverflowType.VISIBLE;
    /** 垂直方向溢出处理 */
    private OverflowType overflowY = OverflowType.VISIBLE;

    // Flexbox布局
    /** Flexbox容器属性 */
    private FlexContainer flexContainer;
    /** Flexbox项目属性 */
    private FlexItem flexItem;

    // Grid布局
    /** Grid容器属性 */
    private GridContainer gridContainer;
    /** Grid项目属性 */
    private GridItem gridItem;

    // 其他属性
    /** 垂直对齐方式 */
    private VerticalAlign verticalAlign = VerticalAlign.BASELINE;
    /** 可见性 */
    private Visibility visibility = Visibility.VISIBLE;
    /** 对象适配方式 */
    private ObjectFit objectFit = ObjectFit.FILL;
    /** 宽高比，0表示无约束 */
    private float aspectRatio = 0;

    /**
     * 获取显示类型
     * @return 显示类型
     */
    public DisplayType getDisplay() {
        return display;
    }

    /**
     * 设置显示类型，会自动初始化对应的容器
     * @param display 显示类型
     */
    public void setDisplay(DisplayType display) {
        this.display = display;

        // 根据显示类型初始化对应的容器
        if (display == DisplayType.FLEX || display == DisplayType.INLINE_FLEX) {
            if (flexContainer == null) {
                flexContainer = new FlexContainer();
            }
        }

        if (display == DisplayType.GRID || display == DisplayType.INLINE_GRID) {
            if (gridContainer == null) {
                gridContainer = new GridContainer();
            }
        }
    }

    /**
     * 获取定位类型
     * @return 定位类型
     */
    public PositionType getPositionType() {
        return positionType;
    }

    /**
     * 设置定位类型
     * @param positionType 定位类型
     */
    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    /**
     * 获取定位偏移
     * @return 定位偏移
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 设置定位偏移
     * @param position 定位偏移
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 获取浮动类型
     * @return 浮动类型
     */
    public FloatType getFloatType() {
        return floatType;
    }

    /**
     * 设置浮动类型
     * @param floatType 浮动类型
     */
    public void setFloatType(FloatType floatType) {
        this.floatType = floatType;
    }

    /**
     * 获取清除浮动类型
     * @return 清除浮动类型
     */
    public ClearType getClearType() {
        return clearType;
    }

    /**
     * 设置清除浮动类型
     * @param clearType 清除浮动类型
     */
    public void setClearType(ClearType clearType) {
        this.clearType = clearType;
    }

    /**
     * 获取盒模型属性
     * @return 盒模型属性
     */
    public BoxModel getBoxModel() {
        return boxModel;
    }

    /**
     * 设置盒模型属性
     * @param boxModel 盒模型属性
     */
    public void setBoxModel(BoxModel boxModel) {
        this.boxModel = boxModel;
    }

    /**
     * 获取溢出处理方式
     * @return 溢出处理方式
     */
    public OverflowType getOverflow() {
        return overflow;
    }

    /**
     * 设置溢出处理方式（同时设置水平和垂直方向）
     * @param overflow 溢出处理方式
     */
    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
        this.overflowX = overflow;
        this.overflowY = overflow;
    }

    /**
     * 获取水平方向溢出处理
     * @return 水平方向溢出处理
     */
    public OverflowType getOverflowX() {
        return overflowX;
    }

    /**
     * 设置水平方向溢出处理
     * @param overflowX 水平方向溢出处理
     */
    public void setOverflowX(OverflowType overflowX) {
        this.overflowX = overflowX;
    }

    /**
     * 获取垂直方向溢出处理
     * @return 垂直方向溢出处理
     */
    public OverflowType getOverflowY() {
        return overflowY;
    }

    /**
     * 设置垂直方向溢出处理
     * @param overflowY 垂直方向溢出处理
     */
    public void setOverflowY(OverflowType overflowY) {
        this.overflowY = overflowY;
    }

    /**
     * 获取Flexbox容器属性
     * @return Flexbox容器属性
     */
    public FlexContainer getFlexContainer() {
        return flexContainer;
    }

    /**
     * 设置Flexbox容器属性
     * @param flexContainer Flexbox容器属性
     */
    public void setFlexContainer(FlexContainer flexContainer) {
        this.flexContainer = flexContainer;
    }

    /**
     * 获取Flexbox项目属性（如不存在则自动创建）
     * @return Flexbox项目属性
     */
    public FlexItem getFlexItem() {
        if (flexItem == null) {
            flexItem = new FlexItem();
        }
        return flexItem;
    }

    /**
     * 设置Flexbox项目属性
     * @param flexItem Flexbox项目属性
     */
    public void setFlexItem(FlexItem flexItem) {
        this.flexItem = flexItem;
    }

    /**
     * 获取Grid容器属性
     * @return Grid容器属性
     */
    public GridContainer getGridContainer() {
        return gridContainer;
    }

    /**
     * 设置Grid容器属性
     * @param gridContainer Grid容器属性
     */
    public void setGridContainer(GridContainer gridContainer) {
        this.gridContainer = gridContainer;
    }

    /**
     * 获取Grid项目属性（如不存在则自动创建）
     * @return Grid项目属性
     */
    public GridItem getGridItem() {
        if (gridItem == null) {
            gridItem = new GridItem();
        }
        return gridItem;
    }

    /**
     * 设置Grid项目属性
     * @param gridItem Grid项目属性
     */
    public void setGridItem(GridItem gridItem) {
        this.gridItem = gridItem;
    }

    /**
     * 获取垂直对齐方式
     * @return 垂直对齐方式
     */
    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    /**
     * 设置垂直对齐方式
     * @param verticalAlign 垂直对齐方式
     */
    public void setVerticalAlign(VerticalAlign verticalAlign) {
        this.verticalAlign = verticalAlign;
    }

    /**
     * 获取可见性
     * @return 可见性
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * 设置可见性
     * @param visibility 可见性
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * 获取对象适配方式
     * @return 对象适配方式
     */
    public ObjectFit getObjectFit() {
        return objectFit;
    }

    /**
     * 设置对象适配方式
     * @param objectFit 对象适配方式
     */
    public void setObjectFit(ObjectFit objectFit) {
        this.objectFit = objectFit;
    }

    /**
     * 获取宽高比
     * @return 宽高比，0表示无约束
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    /**
     * 设置宽高比
     * @param aspectRatio 宽高比，0表示无约束
     */
    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * Returns an allocation-free fingerprint of every value the layout engine
     * reads. It lets a retained UI scene detect mutations made through the
     * existing mutable style API, including nested box, flex, and grid values.
     */
    public long layoutFingerprint() {
        long hash = 0xcbf29ce484222325L;
        hash = mixEnum(hash, display);
        hash = mixEnum(hash, positionType);
        hash = mixPosition(hash, position);
        hash = mixEnum(hash, floatType);
        hash = mixEnum(hash, clearType);
        hash = mixBoxModel(hash, boxModel);
        hash = mixEnum(hash, overflow);
        hash = mixEnum(hash, overflowX);
        hash = mixEnum(hash, overflowY);
        hash = mixFlexContainer(hash, flexContainer);
        hash = mixFlexItem(hash, flexItem);
        hash = mixGridContainer(hash, gridContainer);
        hash = mixGridItem(hash, gridItem);
        hash = mixEnum(hash, verticalAlign);
        hash = mixEnum(hash, visibility);
        hash = mixEnum(hash, objectFit);
        return mix(hash, Float.floatToIntBits(aspectRatio));
    }

    private static long mixBoxModel(long hash, BoxModel value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixSize(hash, value.getSize());
        hash = mixSpacing(hash, value.getMargin());
        hash = mixSpacing(hash, value.getPadding());
        return mixEnum(hash, value.getBoxSizing());
    }

    private static long mixPosition(long hash, Position value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixLength(hash, value.getTop());
        hash = mixLength(hash, value.getRight());
        hash = mixLength(hash, value.getBottom());
        hash = mixLength(hash, value.getLeft());
        return mix(hash, value.getZIndex());
    }

    private static long mixSize(long hash, Size value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixLength(hash, value.getWidth());
        hash = mixLength(hash, value.getHeight());
        hash = mixLength(hash, value.getMinWidth());
        hash = mixLength(hash, value.getMinHeight());
        hash = mixLength(hash, value.getMaxWidth());
        return mixLength(hash, value.getMaxHeight());
    }

    private static long mixSpacing(long hash, Spacing value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixLength(hash, value.getTop());
        hash = mixLength(hash, value.getRight());
        hash = mixLength(hash, value.getBottom());
        return mixLength(hash, value.getLeft());
    }

    private static long mixFlexContainer(long hash, FlexContainer value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixEnum(hash, value.getFlexDirection());
        hash = mixEnum(hash, value.getFlexWrap());
        hash = mixEnum(hash, value.getJustifyContent());
        hash = mixEnum(hash, value.getAlignItems());
        hash = mixEnum(hash, value.getAlignContent());
        hash = mixLength(hash, value.getGap());
        hash = mixLength(hash, value.getRowGap());
        return mixLength(hash, value.getColumnGap());
    }

    private static long mixFlexItem(long hash, FlexItem value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mix(hash, Float.floatToIntBits(value.getFlexGrow()));
        hash = mix(hash, Float.floatToIntBits(value.getFlexShrink()));
        hash = mixLength(hash, value.getFlexBasis());
        hash = mixEnum(hash, value.getAlignSelf());
        return mix(hash, value.getOrder());
    }

    private static long mixGridContainer(long hash, GridContainer value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixGridTrack(hash, value.getTemplateColumns());
        hash = mixGridTrack(hash, value.getTemplateRows());
        for (var row : value.getTemplateAreas()) {
            hash = mix(hash, row == null ? -1 : row.size());
            if (row != null) {
                for (GridArea area : row) {
                    hash = mixString(hash, area == null ? null : area.getName());
                }
            }
        }
        hash = mixLength(hash, value.getAutoColumns());
        hash = mixLength(hash, value.getAutoRows());
        hash = mixEnum(hash, value.getAutoFlow());
        hash = mixLength(hash, value.getGap());
        hash = mixLength(hash, value.getRowGap());
        hash = mixLength(hash, value.getColumnGap());
        hash = mixEnum(hash, value.getJustifyContent());
        hash = mixEnum(hash, value.getAlignContent());
        hash = mixEnum(hash, value.getJustifyItems());
        return mixEnum(hash, value.getAlignItems());
    }

    private static long mixGridItem(long hash, GridItem value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mixGridLine(hash, value.getColumnStart());
        hash = mixGridLine(hash, value.getColumnEnd());
        hash = mixGridLine(hash, value.getRowStart());
        hash = mixGridLine(hash, value.getRowEnd());
        hash = mixString(hash, value.getArea() == null ? null : value.getArea().getName());
        hash = mixEnum(hash, value.getJustifySelf());
        return mixEnum(hash, value.getAlignSelf());
    }

    private static long mixGridTrack(long hash, GridTrack value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mix(hash, value.getTrackCount());
        for (LengthUnit track : value.getTracks()) {
            hash = mixLength(hash, track);
        }
        return hash;
    }

    private static long mixGridLine(long hash, GridLine value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mix(hash, value.getLine());
        hash = mixString(hash, value.getName());
        return mix(hash, value.isSpan() ? 1 : 0);
    }

    private static long mixLength(long hash, LengthUnit value) {
        if (value == null) {
            return mix(hash, 0);
        }
        hash = mix(hash, Float.floatToIntBits(value.getValue()));
        return mixEnum(hash, value.getUnit());
    }

    private static long mixEnum(long hash, Enum<?> value) {
        return mix(hash, value == null ? -1 : value.ordinal());
    }

    private static long mixString(long hash, String value) {
        return mix(hash, value == null ? 0 : value.hashCode());
    }

    private static long mix(long hash, long value) {
        return (hash ^ value) * 0x100000001b3L;
    }
}
