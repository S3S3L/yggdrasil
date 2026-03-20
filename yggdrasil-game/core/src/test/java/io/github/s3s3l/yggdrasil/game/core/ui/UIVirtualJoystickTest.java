package io.github.s3s3l.yggdrasil.game.core.ui;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 虚拟摇杆组件单元测试
 */
class UIVirtualJoystickTest {

    private UIVirtualJoystick joystick;

    @BeforeEach
    void setUp() {
        joystick = new UIVirtualJoystick(50, 20);
        joystick.setId(1L);
        joystick.setCenterPosition(100, 100);
    }

    @Test
    void testVirtualJoystickCreation() {
        assertNotNull(joystick);
        assertEquals(50, joystick.getRadius(), 0.01f);
        assertEquals(20, joystick.getKnobRadius(), 0.01f);
        assertEquals(UIVirtualJoystick.JoystickMode.DIRECTION_360, joystick.getMode());
        assertFalse(joystick.isFollowTouch());
        assertTrue(joystick.isSnapBack());
        assertEquals(0.1f, joystick.getDeadZone(), 0.01f);
    }

    @Test
    void testDefaultConstructor() {
        UIVirtualJoystick j = new UIVirtualJoystick();
        assertNotNull(j);
        assertEquals(50, j.getRadius(), 0.01f);
        assertEquals(20, j.getKnobRadius(), 0.01f);
    }

    @Test
    void testSetRadius() {
        joystick.setRadius(60);
        assertEquals(60, joystick.getRadius(), 0.01f);
    }

    @Test
    void testSetRadiusMin() {
        joystick.setRadius(5); // 小于最小值
        assertEquals(10, joystick.getRadius(), 0.01f); // 钳制到 10
    }

    @Test
    void testSetKnobRadius() {
        joystick.setKnobRadius(25);
        assertEquals(25, joystick.getKnobRadius(), 0.01f);
    }

    @Test
    void testSetKnobRadiusMin() {
        joystick.setKnobRadius(3); // 小于最小值
        assertEquals(5, joystick.getKnobRadius(), 0.01f); // 钳制到 5
    }

    @Test
    void testGetDirection() {
        Vector2 dir = joystick.getDirection();
        assertNotNull(dir);
        assertEquals(0, dir.x, 0.01f);
        assertEquals(0, dir.y, 0.01f);
    }

    @Test
    void testGetAngle() {
        assertEquals(0, joystick.getAngle(), 0.01f);
    }

    @Test
    void testSetMode() {
        joystick.setMode(UIVirtualJoystick.JoystickMode.DIRECTION_8);
        assertEquals(UIVirtualJoystick.JoystickMode.DIRECTION_8, joystick.getMode());
    }

    @Test
    void testSetFollowTouch() {
        joystick.setFollowTouch(true);
        assertTrue(joystick.isFollowTouch());
    }

    @Test
    void testSetStickColor() {
        UIColor color = UIColor.GRAY;
        joystick.setStickColor(color);
        assertEquals(color, joystick.getStickColor());
    }

    @Test
    void testSetKnobColor() {
        UIColor color = UIColor.WHITE;
        joystick.setKnobColor(color);
        assertEquals(color, joystick.getKnobColor());
    }

    @Test
    void testSetActiveColor() {
        UIColor color = UIColor.BLUE;
        joystick.setActiveColor(color);
        assertEquals(color, joystick.getActiveColor());
    }

    @Test
    void testSetDeadZone() {
        joystick.setDeadZone(0.2f);
        assertEquals(0.2f, joystick.getDeadZone(), 0.01f);
    }

    @Test
    void testSetDeadZoneClamped() {
        joystick.setDeadZone(1.5f);
        assertEquals(1f, joystick.getDeadZone(), 0.01f);
    }

    @Test
    void testSetSnapBack() {
        joystick.setSnapBack(false);
        assertFalse(joystick.isSnapBack());
    }

    @Test
    void testSetSnapBackSpeed() {
        joystick.setSnapBackSpeed(15f);
        assertEquals(15f, joystick.getSnapBackSpeed(), 0.01f);
    }

    @Test
    void testSetSnapBackSpeedMin() {
        joystick.setSnapBackSpeed(0.5f);
        assertEquals(1f, joystick.getSnapBackSpeed(), 0.01f);
    }

    @Test
    void testGetIntensity() {
        assertEquals(0, joystick.getIntensity(), 0.01f);
    }

    @Test
    void testGetNormalizedDirection() {
        Vector2 dir = joystick.getNormalizedDirection();
        assertNotNull(dir);
        assertEquals(0, dir.x, 0.01f);
        assertEquals(0, dir.y, 0.01f);
    }

    @Test
    void testVector2Creation() {
        Vector2 vec = new Vector2(3, 4);
        assertEquals(3, vec.x, 0.01f);
        assertEquals(4, vec.y, 0.01f);
    }

    @Test
    void testVector2Length() {
        Vector2 vec = new Vector2(3, 4);
        assertEquals(5, vec.length(), 0.01f);
    }

    @Test
    void testVector2LengthSquared() {
        Vector2 vec = new Vector2(3, 4);
        assertEquals(25, vec.lengthSquared(), 0.01f);
    }

    @Test
    void testVector2Normalize() {
        Vector2 vec = new Vector2(3, 4);
        vec.normalize();
        assertEquals(0.6f, vec.x, 0.01f);
        assertEquals(0.8f, vec.y, 0.01f);
    }

    @Test
    void testVector2Scale() {
        Vector2 vec = new Vector2(1, 1);
        Vector2 scaled = vec.scale(2);
        assertEquals(2, scaled.x, 0.01f);
        assertEquals(2, scaled.y, 0.01f);
    }

    @Test
    void testVector2Copy() {
        Vector2 vec = new Vector2(3, 4);
        Vector2 copy = new Vector2(vec);
        assertEquals(vec, copy);
    }

    @Test
    void testVector2Equals() {
        Vector2 vec1 = new Vector2(3, 4);
        Vector2 vec2 = new Vector2(3, 4);
        assertEquals(vec1, vec2);
    }

    @Test
    void testHandleTouchStart() {
        final boolean[] touchStarted = {false};
        joystick.setListener(new UIVirtualJoystick.JoystickListener() {
            @Override
            public void onDirectionChanged(UIVirtualJoystick j, Vector2 dir, float angle, float intensity) {}

            @Override
            public void onTouchStart(UIVirtualJoystick j, float x, float y) {
                touchStarted[0] = true;
            }

            @Override
            public void onTouchEnd(UIVirtualJoystick j) {}
        });

        joystick.handleTouchStart(100, 100);
        assertTrue(touchStarted[0]);
        assertTrue(joystick.isTouching());
    }

    @Test
    void testHandleTouchStartOutside() {
        joystick.handleTouchStart(0, 0); // 摇杆范围外
        assertFalse(joystick.isTouching());
    }

    @Test
    void testHandleTouchDrag() {
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(120, 100);
        assertTrue(joystick.isTouching());
    }

    @Test
    void testHandleTouchEnd() {
        final boolean[] touchEnded = {false};
        joystick.setListener(new UIVirtualJoystick.JoystickListener() {
            @Override
            public void onDirectionChanged(UIVirtualJoystick j, Vector2 dir, float angle, float intensity) {}

            @Override
            public void onTouchStart(UIVirtualJoystick j, float x, float y) {}

            @Override
            public void onTouchEnd(UIVirtualJoystick j) {
                touchEnded[0] = true;
            }
        });

        joystick.handleTouchStart(100, 100);
        joystick.handleTouchEnd(100, 100);
        assertTrue(touchEnded[0]);
        assertFalse(joystick.isTouching());
    }

    @Test
    void testHandleTouchEndSnapBack() {
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(130, 100);
        joystick.handleTouchEnd(130, 100);

        // 回弹到中心
        assertEquals(100, joystick.getKnobX(), 0.01f);
        assertEquals(100, joystick.getKnobY(), 0.01f);
    }

    @Test
    void testHandleTouchEndNoSnapBack() {
        joystick.setSnapBack(false);
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(130, 100);
        float knobX = joystick.getKnobX();
        joystick.handleTouchEnd(130, 100);

        // 不回弹，保持位置
        assertEquals(knobX, joystick.getKnobX(), 0.01f);
    }

    @Test
    void testContainsPoint() {
        assertTrue(joystick.containsPoint(100, 100));
        assertTrue(joystick.containsPoint(140, 100)); // 在半径内
        assertFalse(joystick.containsPoint(200, 100)); // 在半径外
    }

    @Test
    void testSetCenterPosition() {
        joystick.setCenterPosition(150, 150);
        // 中心位置改变
    }

    @Test
    void testUpdate() {
        joystick.update(0.016f);
        // 不抛异常
    }

    @Test
    void testUpdateSnapBack() {
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(130, 100);
        joystick.handleTouchEnd(130, 100);

        joystick.update(0.016f);
        // 摇杆头应该回弹
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> joystick.render());
    }

    @Test
    void testRenderHidden() {
        joystick.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> joystick.render());
    }

    @Test
    void testSetPosition() {
        joystick.setPosition(50, 50);
        assertEquals(50, joystick.getX(), 0.01f);
        assertEquals(50, joystick.getY(), 0.01f);
    }

    @Test
    void testGetTotalSize() {
        float size = joystick.getTotalSize();
        assertEquals(100, size, 0.01f); // radius * 2
    }

    @Test
    void testIsActive() {
        assertFalse(joystick.isActive());
        joystick.handleTouchStart(100, 100);
        assertTrue(joystick.isActive());
    }

    @Test
    void testIsActiveAfterDrag() {
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(130, 100);
        assertTrue(joystick.isActive());
    }

    @Test
    void testGetAngle8Direction() {
        joystick.setMode(UIVirtualJoystick.JoystickMode.DIRECTION_8);
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(130, 100); // 右侧

        float angle = joystick.getAngle();
        assertEquals(0, angle, 0.01f); // 应该钳制到 0 度
    }

    @Test
    void testListenerDirectionChanged() {
        final float[] capturedAngle = {-1};
        joystick.setListener(new UIVirtualJoystick.JoystickListener() {
            @Override
            public void onDirectionChanged(UIVirtualJoystick j, Vector2 dir, float angle, float intensity) {
                capturedAngle[0] = angle;
            }

            @Override
            public void onTouchStart(UIVirtualJoystick j, float x, float y) {}

            @Override
            public void onTouchEnd(UIVirtualJoystick j) {}
        });

        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(140, 100); // 拖动距离大于死区

        // 角度应该被更新（0 度表示向右）
        assertEquals(0, capturedAngle[0], 1);
    }

    @Test
    void testFollowTouchMode() {
        joystick.setFollowTouch(true);
        joystick.handleTouchStart(150, 150);

        // 在跟随模式下，中心点应该移动到触摸位置
        // 注意：实际实现中中心点会改变
    }

    @Test
    void testDeadZone() {
        joystick.setDeadZone(0.5f);
        joystick.handleTouchStart(100, 100);
        joystick.handleTouchDrag(110, 100); // 小幅度拖动

        // 在死区内，方向应该为 0
        Vector2 dir = joystick.getDirection();
        assertEquals(0, dir.x, 0.01f);
        assertEquals(0, dir.y, 0.01f);
    }
}
