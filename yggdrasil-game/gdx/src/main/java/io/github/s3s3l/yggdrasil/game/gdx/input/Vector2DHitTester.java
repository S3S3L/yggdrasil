package io.github.s3s3l.yggdrasil.game.gdx.input;

import java.util.Collection;
import java.util.function.Supplier;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.event.input.HitTester;
import io.github.s3s3l.yggdrasil.game.core.object.GameObject;
import io.github.s3s3l.yggdrasil.game.core.object.ShapedGameObject;
import io.github.s3s3l.yggdrasil.game.core.object.props.Visible;

/**
 * 基于 Vector2D 的默认命中测试。
 * 深度优先，优先返回最深层次的可见、启用对象。
 */
public class Vector2DHitTester implements HitTester {

    private final Supplier<Collection<GameObject>> rootsSupplier;

    public Vector2DHitTester(Supplier<Collection<GameObject>> rootsSupplier) {
        this.rootsSupplier = rootsSupplier;
    }

    @Override
    public GameObject hit(float x, float y) {
        Collection<GameObject> roots = rootsSupplier == null ? null : rootsSupplier.get();
        if (roots == null) {
            return null;
        }
        GameObject result = null;
        Vector2D point = Vector2D.builder().x(x).y(y).build();
        for (GameObject root : roots) {
            // 如果存在父节点，跳过，避免重复遍历非根节点
            if (root.parent() != null) {
                continue;
            }

            GameObject candidate = dfs(root, point);
            if (candidate != null) {
                result = candidate;
            }
        }
        return result;
    }

    private GameObject dfs(GameObject node, Vector2D point) {
        if (node == null || !node.isEnable() || node.getVisible() != Visible.SHOW) {
            return null;
        }

        GameObject deepestChild = null;
        for (GameObject child : node.children()) {
            GameObject hit = dfs(child, point);
            if (hit != null) {
                deepestChild = hit;
            }
        }
        if (deepestChild != null) {
            return deepestChild;
        }

        if (node instanceof ShapedGameObject<?, ?> raw) {
            @SuppressWarnings("unchecked")
            ShapedGameObject<Vector2D, ?> shaped = (ShapedGameObject<Vector2D, ?>) raw;
            if (shaped.hitTest(point)) {
                return node;
            }
        }

        return null;
    }
}
