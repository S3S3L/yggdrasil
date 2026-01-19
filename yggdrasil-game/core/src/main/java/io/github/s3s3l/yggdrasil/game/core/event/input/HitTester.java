package io.github.s3s3l.yggdrasil.game.core.event.input;

import io.github.s3s3l.yggdrasil.game.core.object.GameObject;

/**
 * 基于坐标的命中测试接口。
 */
@FunctionalInterface
public interface HitTester {
    GameObject hit(float x, float y);
}
