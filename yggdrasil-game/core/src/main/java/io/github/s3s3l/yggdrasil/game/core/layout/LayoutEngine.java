package io.github.s3s3l.yggdrasil.game.core.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 简化版的布局引擎，能够在给定视口下计算每个节点的最终样式与布局结果。
 */
public final class LayoutEngine {
    private LayoutEngine() {
    }

    /**
     * 从根节点开始计算布局。
     *
     * @param root           根布局节点
     * @param viewportWidth  视口宽度
     * @param viewportHeight 视口高度
     * @return 根节点的布局结果
     */
    public static LayoutResult layout(LayoutNode root, float viewportWidth, float viewportHeight) {
        if (root == null) {
            return null;
        }
        LayoutStyle rootStyle = root.getStyle();
        if (rootStyle == null) {
            rootStyle = new LayoutStyle();
            root.setStyle(rootStyle);
        }
        // 根节点可用尺寸就是视口
        ComputedStyle computed = ComputedStyle.from(rootStyle, viewportWidth, viewportHeight, viewportWidth, viewportHeight);
        root.setComputedStyle(computed);
        if (computed.getDisplay() == DisplayType.NONE) {
            LayoutResult hidden = emptyResult();
            root.setLayoutResult(hidden);
            return hidden;
        }

        float rootWidth = dimensionOrFallback(computed.getWidth(), viewportWidth);
        float rootHeight = dimensionOrFallback(computed.getHeight(), viewportHeight);
        LayoutResult result = new LayoutResult(0, 0, rootWidth, rootHeight,
                Math.max(0, rootWidth - computed.getPaddingLeft() - computed.getPaddingRight()),
                Math.max(0, rootHeight - computed.getPaddingTop() - computed.getPaddingBottom()));
        root.setLayoutResult(result);

        layoutByDisplay(root, viewportWidth, viewportHeight,
                rootWidth - computed.getPaddingLeft() - computed.getPaddingRight(),
                rootHeight - computed.getPaddingTop() - computed.getPaddingBottom(),
                computed.getPaddingLeft(), computed.getPaddingTop(),
                0, 0);

        // 如果根高度为auto则根据子节点撑开
        if (Float.isNaN(computed.getHeight())) {
            float maxY = 0;
            for (LayoutNode child : root.childNodes()) {
                LayoutResult cr = child.getLayoutResult();
                if (cr != null) {
                    maxY = Math.max(maxY, cr.getY() + cr.getHeight());
                }
            }
            float finalHeight = maxY + computed.getPaddingBottom();
            result.setHeight(finalHeight);
            result.setContentHeight(finalHeight - computed.getPaddingTop() - computed.getPaddingBottom());
        }

        return result;
    }

    private static void layoutByDisplay(LayoutNode node, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float offsetX, float offsetY,
            float anchorX, float anchorY) {
        ComputedStyle style = node.getComputedStyle();
        if (style == null) {
            return;
        }
        if (style.getDisplay() == DisplayType.NONE) {
            node.setLayoutResult(new LayoutResult(0, 0, 0, 0, 0, 0));
            return;
        }
        switch (style.getDisplay()) {
            case FLEX:
            case INLINE_FLEX:
                layoutFlex(node, viewportWidth, viewportHeight, availableWidth, availableHeight, offsetX, offsetY,
                        anchorX, anchorY);
                break;
            case GRID:
            case INLINE_GRID:
                layoutGrid(node, viewportWidth, viewportHeight, availableWidth, availableHeight, offsetX, offsetY,
                        anchorX, anchorY);
                break;
            default:
                layoutBlock(node, viewportWidth, viewportHeight, availableWidth, availableHeight, offsetX, offsetY,
                        anchorX, anchorY);
                break;
        }
    }

    private static void layoutBlock(LayoutNode node, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float offsetX, float offsetY,
            float anchorX, float anchorY) {
        ComputedStyle style = node.getComputedStyle();
        LayoutStyle raw = node.getStyle();
        BoxSizingType boxSizing = raw.getBoxModel().getBoxSizing();

        float contentWidth = computeWidth(style, availableWidth, boxSizing);
        float x = offsetX + style.getMarginLeft();
        float y = offsetY + style.getMarginTop();

        float cursorY = y + style.getPaddingTop();
        float maxChildWidth = 0;
        float childrenHeight = 0;

        for (LayoutNode child : node.childNodes()) {
            ComputedStyle childStyle = ComputedStyle.from(child.getStyle(),
                    Math.max(0, contentWidth - style.getPaddingLeft() - style.getPaddingRight()), availableHeight,
                    viewportWidth, viewportHeight);
            child.setComputedStyle(childStyle);
            if (childStyle.getDisplay() == DisplayType.NONE) {
                child.setLayoutResult(emptyResult());
                continue;
            }

            if (childStyle.getPositionType() == PositionType.ABSOLUTE
                    || childStyle.getPositionType() == PositionType.FIXED) {
                LayoutResult childResult = layoutAbsolute(child, viewportWidth, viewportHeight,
                        contentWidth, availableHeight,
                        anchorX + style.getPaddingLeft(), anchorY + style.getPaddingTop());
                child.setLayoutResult(childResult);
                continue;
            }

            float childAvailableWidth = Math.max(0,
                    contentWidth - childStyle.getMarginLeft() - childStyle.getMarginRight());
            LayoutResult childResult = layoutFlowChild(child, viewportWidth, viewportHeight,
                    childAvailableWidth, availableHeight,
                    x + style.getPaddingLeft(), cursorY,
                    anchorX + style.getPaddingLeft(), anchorY + style.getPaddingTop());
            child.setLayoutResult(childResult);
            cursorY = childResult.getY() + childResult.getHeight() + childStyle.getMarginBottom();
            childrenHeight = cursorY - y - style.getPaddingTop();
            maxChildWidth = Math.max(maxChildWidth,
                    childStyle.getMarginLeft() + childResult.getWidth() + childStyle.getMarginRight());
        }

        float resolvedHeight = computeHeight(style, availableHeight, boxSizing, childrenHeight);
        float resolvedWidth = !Float.isNaN(style.getWidth()) ? computeWidth(style, availableWidth, boxSizing)
                : Math.max(contentWidth, maxChildWidth + style.getPaddingLeft() + style.getPaddingRight());

        float contentHeight = Math.max(0, resolvedHeight - style.getPaddingTop() - style.getPaddingBottom());
        LayoutResult self = node.getLayoutResult();
        if (self == null) {
            self = new LayoutResult();
        }
        self.setX(x);
        self.setY(y);
        self.setWidth(resolvedWidth);
        self.setHeight(resolvedHeight);
        self.setContentWidth(Math.max(0, resolvedWidth - style.getPaddingLeft() - style.getPaddingRight()));
        self.setContentHeight(contentHeight);
        node.setLayoutResult(self);
    }

    private static void layoutFlex(LayoutNode node, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float offsetX, float offsetY,
            float anchorX, float anchorY) {
        ComputedStyle style = node.getComputedStyle();
        LayoutStyle raw = node.getStyle();
        BoxSizingType boxSizing = raw.getBoxModel().getBoxSizing();

        boolean isRow = style.getFlexContainer().getFlexDirection() == FlexDirection.ROW
                || style.getFlexContainer().getFlexDirection() == FlexDirection.ROW_REVERSE;

        float resolvedWidth = computeWidth(style, availableWidth, boxSizing);
        float resolvedHeight = computeHeight(style, availableHeight, boxSizing, Float.NaN);
        float contentWidth = Math.max(0, resolvedWidth - style.getPaddingLeft() - style.getPaddingRight());
        float contentHeight = Float.isNaN(resolvedHeight) ? availableHeight
                : Math.max(0, resolvedHeight - style.getPaddingTop() - style.getPaddingBottom());

        List<LayoutNode> items = new ArrayList<>(node.childNodes());
        items.sort(Comparator.comparingInt(o -> {
            FlexItem fi = o.getStyle().getFlexItem();
            return fi == null ? 0 : fi.getOrder();
        }));

        float totalMain = 0;
        float maxCross = 0;
        float rowGap = style.getRowGap();
        float columnGap = style.getColumnGap();
        float gap = isRow ? columnGap : rowGap;
        int visibleCount = 0;

        List<Float> baseMainSizes = new ArrayList<>();
        List<Float> baseCrossSizes = new ArrayList<>();

        for (LayoutNode child : items) {
            ComputedStyle childStyle = ComputedStyle.from(child.getStyle(),
                    contentWidth, contentHeight, viewportWidth, viewportHeight);
            child.setComputedStyle(childStyle);
            if (childStyle.getDisplay() == DisplayType.NONE) {
                baseMainSizes.add(0f);
                baseCrossSizes.add(0f);
                child.setLayoutResult(emptyResult());
                continue;
            }
            visibleCount++;
            float mainBase = resolveFlexBasis(childStyle, isRow ? contentWidth : contentHeight,
                    viewportWidth, viewportHeight, child.getStyle().getFlexItem());
            if (Float.isNaN(mainBase)) {
                mainBase = isRow ? computeWidth(childStyle, contentWidth, child.getStyle().getBoxModel().getBoxSizing())
                        : computeHeight(childStyle, contentHeight, child.getStyle().getBoxModel().getBoxSizing(), Float.NaN);
            }
            float crossBase = isRow ? computeHeight(childStyle, contentHeight, child.getStyle().getBoxModel().getBoxSizing(), Float.NaN)
                    : computeWidth(childStyle, contentWidth, child.getStyle().getBoxModel().getBoxSizing());
            totalMain += mainBase + (isRow ? childStyle.getMarginLeft() + childStyle.getMarginRight()
                    : childStyle.getMarginTop() + childStyle.getMarginBottom());
            maxCross = Math.max(maxCross, crossBase + (isRow ? childStyle.getMarginTop() + childStyle.getMarginBottom()
                    : childStyle.getMarginLeft() + childStyle.getMarginRight()));
            baseMainSizes.add(mainBase);
            baseCrossSizes.add(crossBase);
        }

        if (visibleCount > 1) {
            totalMain += gap * (visibleCount - 1);
        }

        float mainSize = isRow ? contentWidth : contentHeight;
        if (Float.isNaN(mainSize)) {
            mainSize = totalMain;
        }
        float freeSpace = mainSize - totalMain;

        float totalGrow = 0;
        float totalShrink = 0;
        for (LayoutNode child : items) {
            FlexItem fi = child.getStyle().getFlexItem();
            if (fi == null) {
                fi = new FlexItem();
            }
            totalGrow += fi.getFlexGrow();
            totalShrink += fi.getFlexShrink();
        }

        List<Float> finalMainSizes = new ArrayList<>(baseMainSizes.size());
        for (int i = 0; i < baseMainSizes.size(); i++) {
            FlexItem fi = items.get(i).getStyle().getFlexItem();
            if (fi == null) {
                fi = new FlexItem();
            }
            float target = baseMainSizes.get(i);
            if (freeSpace > 0 && totalGrow > 0) {
                target += freeSpace * (fi.getFlexGrow() / totalGrow);
            } else if (freeSpace < 0 && totalShrink > 0) {
                target += freeSpace * (fi.getFlexShrink() / totalShrink);
                target = Math.max(0, target);
            }
            finalMainSizes.add(target);
        }

        float usedSpace = 0;
        int usedCount = 0;
        for (int i = 0; i < finalMainSizes.size(); i++) {
            LayoutNode child = items.get(i);
            ComputedStyle childStyle = child.getComputedStyle();
            if (childStyle.getDisplay() == DisplayType.NONE) {
                continue;
            }
            usedCount++;
            usedSpace += finalMainSizes.get(i) + (isRow ? childStyle.getMarginLeft() + childStyle.getMarginRight()
                    : childStyle.getMarginTop() + childStyle.getMarginBottom());
        }
        if (usedCount > 1) {
            usedSpace += gap * (usedCount - 1);
        }

        float startMain = isRow ? style.getPaddingLeft() : style.getPaddingTop();
        float remaining = mainSize - usedSpace;
        if (!Float.isNaN(remaining) && remaining > 0) {
            switch (style.getFlexContainer().getJustifyContent()) {
                case CENTER:
                    startMain += remaining / 2f;
                    break;
                case FLEX_END:
                    startMain += remaining;
                    break;
                case SPACE_BETWEEN:
                    gap = usedCount > 1 ? remaining / (usedCount - 1) : 0;
                    break;
                case SPACE_AROUND:
                    gap = usedCount > 0 ? remaining / usedCount : 0;
                    startMain += gap / 2f;
                    break;
                case SPACE_EVENLY:
                    gap = usedCount >= 0 ? remaining / (usedCount + 1) : 0;
                    startMain += gap;
                    break;
                case FLEX_START:
                default:
                    break;
            }
        }

        float usedMain = 0;
        int placed = 0;
        for (int i = 0; i < finalMainSizes.size(); i++) {
            LayoutNode child = items.get(i);
            ComputedStyle childStyle = child.getComputedStyle();
            if (childStyle.getDisplay() == DisplayType.NONE) {
                continue;
            }
            float mainSizeForChild = finalMainSizes.get(i);
            float crossSizeForChild = baseCrossSizes.get(i);

            float posMain = startMain + usedMain + (placed > 0 ? gap : 0) + (isRow ? childStyle.getMarginLeft()
                    : childStyle.getMarginTop());
            float crossSpace = isRow ? (Float.isNaN(contentHeight) ? availableHeight : contentHeight)
                    : (Float.isNaN(contentWidth) ? availableWidth : contentWidth);
            float crossWithMargin = crossSizeForChild + (isRow ? childStyle.getMarginTop() + childStyle.getMarginBottom()
                    : childStyle.getMarginLeft() + childStyle.getMarginRight());
            AlignItems align = resolveAlignItems(childStyle, style.getFlexContainer());
            float posCross = isRow ? style.getPaddingTop() + childStyle.getMarginTop()
                    : style.getPaddingLeft() + childStyle.getMarginLeft();
            switch (align) {
                case CENTER:
                    posCross += Math.max(0, (crossSpace - crossWithMargin) / 2f);
                    break;
                case FLEX_END:
                    posCross += Math.max(0, crossSpace - crossWithMargin);
                    break;
                case STRETCH:
                    if (isRow) {
                        crossSizeForChild = Math.max(0, crossSpace - childStyle.getMarginTop() - childStyle.getMarginBottom());
                    } else {
                        crossSizeForChild = Math.max(0, crossSpace - childStyle.getMarginLeft() - childStyle.getMarginRight());
                    }
                    break;
                case BASELINE:
                case FLEX_START:
                default:
                    break;
            }

            float childAvailableWidth = isRow ? Math.max(0, mainSizeForChild - childStyle.getMarginLeft() - childStyle.getMarginRight())
                    : Math.max(0, (Float.isNaN(contentWidth) ? availableWidth : contentWidth)
                            - childStyle.getMarginLeft() - childStyle.getMarginRight());
            float childAvailableHeight = isRow ? Math.max(0, (Float.isNaN(contentHeight) ? availableHeight : contentHeight)
                    - childStyle.getMarginTop() - childStyle.getMarginBottom())
                    : Math.max(0, mainSizeForChild - childStyle.getMarginTop() - childStyle.getMarginBottom());
            if (isRow) {
                childAvailableHeight = Math.max(0, crossSizeForChild);
            } else {
                childAvailableWidth = Math.max(0, crossSizeForChild);
            }

            LayoutResult childResult = layoutFlowChild(child, viewportWidth, viewportHeight,
                    childAvailableWidth, childAvailableHeight,
                    offsetX + posMain, offsetY + posCross,
                    anchorX + style.getPaddingLeft(), anchorY + style.getPaddingTop());
            child.setLayoutResult(childResult);

            usedMain += mainSizeForChild + (isRow ? childStyle.getMarginLeft() + childStyle.getMarginRight()
                    : childStyle.getMarginTop() + childStyle.getMarginBottom());
            if (childStyle.getPositionType() != PositionType.ABSOLUTE
                    && childStyle.getPositionType() != PositionType.FIXED) {
                maxCross = Math.max(maxCross, crossSizeForChild + (isRow ? childStyle.getMarginTop()
                        + childStyle.getMarginBottom()
                        : childStyle.getMarginLeft() + childStyle.getMarginRight()));
            }
            placed++;
        }

        float finalCross = isRow ? computeHeight(style, availableHeight, boxSizing, maxCross)
                : computeWidth(style, availableWidth, boxSizing);
        float finalMain = isRow ? resolvedWidth : computeHeight(style, availableHeight, boxSizing, usedMain);

        LayoutResult self = node.getLayoutResult();
        if (self == null) {
            self = new LayoutResult();
        }
        float x = offsetX + style.getMarginLeft();
        float y = offsetY + style.getMarginTop();
        self.setX(x);
        self.setY(y);
        self.setWidth(isRow ? finalMain : finalCross);
        self.setHeight(isRow ? finalCross : finalMain);
        self.setContentWidth(Math.max(0, self.getWidth() - style.getPaddingLeft() - style.getPaddingRight()));
        self.setContentHeight(Math.max(0, self.getHeight() - style.getPaddingTop() - style.getPaddingBottom()));
        node.setLayoutResult(self);
    }

    private static void layoutGrid(LayoutNode node, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float offsetX, float offsetY,
            float anchorX, float anchorY) {
        ComputedStyle style = node.getComputedStyle();
        LayoutStyle raw = node.getStyle();
        BoxSizingType boxSizing = raw.getBoxModel().getBoxSizing();

        float resolvedWidth = computeWidth(style, availableWidth, boxSizing);
        float resolvedHeight = computeHeight(style, availableHeight, boxSizing, Float.NaN);
        float contentWidth = Math.max(0, resolvedWidth - style.getPaddingLeft() - style.getPaddingRight());
        float contentHeight = Float.isNaN(resolvedHeight) ? availableHeight
                : Math.max(0, resolvedHeight - style.getPaddingTop() - style.getPaddingBottom());

        List<LengthUnit> colTracks = style.getGridContainer().getTemplateColumns().getTracks();
        List<LengthUnit> rowTracks = style.getGridContainer().getTemplateRows().getTracks();
        int columnCount = Math.max(1, colTracks.isEmpty() ? 1 : colTracks.size());
        int neededRows = (int) Math.ceil(node.childNodes().size() / (float) columnCount);
        int rowCount = Math.max(1, rowTracks.isEmpty() ? neededRows : Math.max(rowTracks.size(), neededRows));

        float[] columnSizes = resolveTracks(colTracks, columnCount, contentWidth, style.getGridContainer().getAutoColumns(),
                style.getColumnGap(), viewportWidth, viewportHeight);
        float[] rowSizes = resolveTracks(rowTracks, rowCount, contentHeight, style.getGridContainer().getAutoRows(),
                style.getRowGap(), viewportWidth, viewportHeight);

        float[] columnStarts = new float[columnCount];
        float running = offsetX + style.getMarginLeft() + style.getPaddingLeft();
        for (int i = 0; i < columnCount; i++) {
            columnStarts[i] = running;
            running += columnSizes[i];
            if (i < columnCount - 1) {
                running += style.getColumnGap();
            }
        }

        float[] rowStarts = new float[rowCount];
        running = offsetY + style.getMarginTop() + style.getPaddingTop();
        for (int i = 0; i < rowCount; i++) {
            rowStarts[i] = running;
            running += rowSizes[i];
            if (i < rowCount - 1) {
                running += style.getRowGap();
            }
        }

        int index = 0;
        for (LayoutNode child : node.childNodes()) {
            ComputedStyle childStyle = ComputedStyle.from(child.getStyle(),
                    columnSizes[index % columnCount], rowSizes[index / columnCount], viewportWidth, viewportHeight);
            child.setComputedStyle(childStyle);

            if (childStyle.getDisplay() == DisplayType.NONE) {
                child.setLayoutResult(emptyResult());
                index++;
                continue;
            }

            int row = index / columnCount;
            int col = index % columnCount;
            float availableCellWidth = Math.max(0, columnSizes[col] - childStyle.getMarginLeft() - childStyle.getMarginRight());
            float availableCellHeight = Math.max(0, rowSizes[row] - childStyle.getMarginTop() - childStyle.getMarginBottom());

            LayoutResult childResult = layoutFlowChild(child, viewportWidth, viewportHeight,
                    availableCellWidth, availableCellHeight,
                    columnStarts[col] + childStyle.getMarginLeft(), rowStarts[row] + childStyle.getMarginTop(),
                    anchorX + style.getPaddingLeft(), anchorY + style.getPaddingTop());
            child.setLayoutResult(childResult);
            index++;
        }

        float usedHeight = rowStarts[Math.min(rowCount - 1, rowCount - 1)] + rowSizes[Math.min(rowCount - 1, rowCount - 1)]
                - (offsetY + style.getMarginTop() + style.getPaddingTop());
        float finalHeight = Float.isNaN(resolvedHeight)
                ? usedHeight + style.getPaddingTop() + style.getPaddingBottom()
                : resolvedHeight;

        LayoutResult self = node.getLayoutResult();
        if (self == null) {
            self = new LayoutResult();
        }
        self.setX(offsetX + style.getMarginLeft());
        self.setY(offsetY + style.getMarginTop());
        self.setWidth(resolvedWidth);
        self.setHeight(finalHeight);
        self.setContentWidth(Math.max(0, resolvedWidth - style.getPaddingLeft() - style.getPaddingRight()));
        self.setContentHeight(Math.max(0, finalHeight - style.getPaddingTop() - style.getPaddingBottom()));
        node.setLayoutResult(self);
    }

    private static LayoutResult layoutFlowChild(LayoutNode child, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float offsetX, float offsetY,
            float anchorX, float anchorY) {
        ComputedStyle childStyle = child.getComputedStyle();
        if (childStyle.getDisplay() == DisplayType.NONE) {
            return emptyResult();
        }
        BoxSizingType boxSizing = child.getStyle().getBoxModel().getBoxSizing();
        float width = computeWidth(childStyle, availableWidth, boxSizing);
        float height = computeHeight(childStyle, availableHeight, boxSizing, Float.NaN);

        float x = offsetX + childStyle.getMarginLeft();
        float y = offsetY + childStyle.getMarginTop();

        layoutByDisplay(child, viewportWidth, viewportHeight,
                Math.max(0, width - childStyle.getPaddingLeft() - childStyle.getPaddingRight()),
                Math.max(0, Float.isNaN(height) ? availableHeight : height - childStyle.getPaddingTop()
                        - childStyle.getPaddingBottom()),
                x + childStyle.getPaddingLeft(), y + childStyle.getPaddingTop(), anchorX, anchorY);

        LayoutResult result = child.getLayoutResult();
        if (result == null) {
            result = new LayoutResult();
        }
        float finalHeight = Float.isNaN(height) ? (result.getHeight() + childStyle.getPaddingTop()
                + childStyle.getPaddingBottom()) : height;
        result.setX(x);
        result.setY(y);
        result.setWidth(width);
        result.setHeight(finalHeight);
        result.setContentWidth(Math.max(0, width - childStyle.getPaddingLeft() - childStyle.getPaddingRight()));
        result.setContentHeight(Math.max(0, finalHeight - childStyle.getPaddingTop() - childStyle.getPaddingBottom()));
        return result;
    }

    private static LayoutResult layoutAbsolute(LayoutNode child, float viewportWidth, float viewportHeight,
            float availableWidth, float availableHeight, float anchorX, float anchorY) {
        ComputedStyle style = child.getComputedStyle();
        if (style.getDisplay() == DisplayType.NONE) {
            return emptyResult();
        }
        LengthResolver resolver = new LengthResolver(availableWidth, availableHeight, viewportWidth, viewportHeight);
        Position pos = child.getStyle().getPosition();

        float left = resolver.resolve(pos.getLeft(), availableWidth);
        float right = resolver.resolve(pos.getRight(), availableWidth);
        float top = resolver.resolve(pos.getTop(), availableHeight);
        float bottom = resolver.resolve(pos.getBottom(), availableHeight);

        float x = anchorX + style.getMarginLeft();
        float y = anchorY + style.getMarginTop();

        float width = computeWidth(style, availableWidth, child.getStyle().getBoxModel().getBoxSizing());
        float height = computeHeight(style, availableHeight, child.getStyle().getBoxModel().getBoxSizing(), Float.NaN);

        if (!Float.isNaN(left)) {
            x = anchorX + left + style.getMarginLeft();
        } else if (!Float.isNaN(right) && !Float.isNaN(width)) {
            x = anchorX + availableWidth - right - width - style.getMarginRight();
        }

        if (!Float.isNaN(top)) {
            y = anchorY + top + style.getMarginTop();
        } else if (!Float.isNaN(bottom) && !Float.isNaN(height)) {
            y = anchorY + availableHeight - bottom - height - style.getMarginBottom();
        }

        layoutByDisplay(child, viewportWidth, viewportHeight,
                Math.max(0, width - style.getPaddingLeft() - style.getPaddingRight()),
                Math.max(0, Float.isNaN(height) ? availableHeight : height - style.getPaddingTop()
                        - style.getPaddingBottom()),
                x + style.getPaddingLeft(), y + style.getPaddingTop(), x, y);

        LayoutResult result = child.getLayoutResult();
        if (result == null) {
            result = new LayoutResult();
        }
        float finalHeight = Float.isNaN(height) ? (result.getHeight() + style.getPaddingTop() + style.getPaddingBottom())
                : height;
        result.setX(x);
        result.setY(y);
        result.setWidth(width);
        result.setHeight(finalHeight);
        result.setContentWidth(Math.max(0, width - style.getPaddingLeft() - style.getPaddingRight()));
        result.setContentHeight(Math.max(0, finalHeight - style.getPaddingTop() - style.getPaddingBottom()));
        return result;
    }

    private static float[] resolveTracks(List<LengthUnit> tracks, int count, float available,
            LengthUnit autoUnit, float gap, float viewportWidth, float viewportHeight) {
        float[] result = new float[count];
        LengthResolver resolver = new LengthResolver(available, available, viewportWidth, viewportHeight);
        float fixedSum = 0;
        float frTotal = 0;
        for (int i = 0; i < count; i++) {
            LengthUnit unit = tracks.isEmpty() ? autoUnit : (i < tracks.size() ? tracks.get(i) : autoUnit);
            if (unit == null) {
                unit = LengthUnit.fr(1);
            }
            switch (unit.getUnit()) {
                case PX:
                case PERCENT:
                case VH:
                case VW:
                    float value = resolver.resolve(unit, available);
                    result[i] = value;
                    fixedSum += value;
                    break;
                case AUTO:
                    result[i] = available / count;
                    fixedSum += result[i];
                    break;
                case FR:
                default:
                    frTotal += unit.getValue();
                    break;
            }
        }
        float totalGap = gap * Math.max(0, count - 1);
        float remaining = Math.max(0, available - fixedSum - totalGap);
        for (int i = 0; i < count; i++) {
            LengthUnit unit = tracks.isEmpty() ? autoUnit : (i < tracks.size() ? tracks.get(i) : autoUnit);
            if (unit == null || unit.getUnit() == LengthUnit.Unit.FR) {
                float ratio;
                if (frTotal == 0) {
                    ratio = 1f / count;
                } else {
                    float value = unit == null ? 1f : unit.getValue();
                    ratio = value / frTotal;
                }
                result[i] = remaining * ratio;
            }
        }
        return result;
    }

    private static float resolveFlexBasis(ComputedStyle style, float availableMain,
            float viewportWidth, float viewportHeight, FlexItem item) {
        if (item == null) {
            return Float.NaN;
        }
        LengthUnit basis = item.getFlexBasis();
        if (basis == null || basis.getUnit() == LengthUnit.Unit.AUTO) {
            return Float.NaN;
        }
        LengthResolver resolver = new LengthResolver(availableMain, availableMain, viewportWidth, viewportHeight);
        return resolver.resolve(basis, availableMain);
    }

    private static float computeWidth(ComputedStyle style, float availableWidth, BoxSizingType boxSizing) {
        float fallback = Float.isNaN(availableWidth) ? 0
                : Math.max(0, availableWidth - style.getMarginLeft() - style.getMarginRight());
        float width = dimensionOrFallback(style.getWidth(), fallback);
        width = clamp(width, style.getMinWidth(), style.getMaxWidth());
        width = Math.max(0, width);
        if (boxSizing == BoxSizingType.CONTENT_BOX) {
            return width;
        }
        return width; // BORDER_BOX 暂无border，直接返回
    }

    private static float computeHeight(ComputedStyle style, float availableHeight, BoxSizingType boxSizing,
            float autoContentHeight) {
        float fallback = Float.isNaN(autoContentHeight) ? 0 : autoContentHeight;
        float height = dimensionOrFallback(style.getHeight(), fallback);
        height = clamp(height, style.getMinHeight(), style.getMaxHeight());
        height = Math.max(0, height);
        if (boxSizing == BoxSizingType.CONTENT_BOX) {
            return height;
        }
        return height;
    }

    private static AlignItems resolveAlignItems(ComputedStyle childStyle, FlexContainer container) {
        AlignSelf self = container == null ? AlignSelf.AUTO : childStyle.getFlexItem().getAlignSelf();
        AlignItems containerAlign = container == null ? AlignItems.STRETCH : container.getAlignItems();
        if (self == null || self == AlignSelf.AUTO) {
            return containerAlign;
        }
        switch (self) {
            case FLEX_START:
                return AlignItems.FLEX_START;
            case FLEX_END:
                return AlignItems.FLEX_END;
            case CENTER:
                return AlignItems.CENTER;
            case STRETCH:
                return AlignItems.STRETCH;
            case BASELINE:
                return AlignItems.BASELINE;
            case AUTO:
            default:
                return containerAlign;
        }
    }

    private static float clamp(float value, float min, float max) {
        float result = value;
        if (!Float.isNaN(min)) {
            result = Math.max(min, result);
        }
        if (!Float.isNaN(max) && max > 0) {
            result = Math.min(max, result);
        }
        return result;
    }

    private static float dimensionOrFallback(float value, float fallback) {
        return Float.isNaN(value) ? fallback : value;
    }

    private static LayoutResult emptyResult() {
        return new LayoutResult(0, 0, 0, 0, 0, 0);
    }
}
