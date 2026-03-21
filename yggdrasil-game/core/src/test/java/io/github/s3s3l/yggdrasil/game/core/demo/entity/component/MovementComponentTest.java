package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MovementComponent 单元测试
 */
class MovementComponentTest {

    private MovementComponent movementComponent;

    @BeforeEach
    void setUp() {
        movementComponent = new MovementComponent(0, 0, 100);
    }

    @Test
    void testConstructor() {
        assertEquals(0, movementComponent.getX(), 0.01f);
        assertEquals(0, movementComponent.getY(), 0.01f);
        assertEquals(100, movementComponent.getMoveSpeed(), 0.01f);
        assertFalse(movementComponent.isMoving());
    }

    @Test
    void testConstructorWithPosition() {
        MovementComponent mc = new MovementComponent(10, 20, 150);
        assertEquals(10, mc.getX(), 0.01f);
        assertEquals(20, mc.getY(), 0.01f);
        assertEquals(150, mc.getMoveSpeed(), 0.01f);
    }

    @Test
    void testSetPosition() {
        movementComponent.setPosition(50, 60);
        assertEquals(50, movementComponent.getX(), 0.01f);
        assertEquals(60, movementComponent.getY(), 0.01f);
    }

    @Test
    void testSetVelocity() {
        movementComponent.setVelocity(10, 20);
        assertEquals(10, movementComponent.getVelocityX(), 0.01f);
        assertEquals(20, movementComponent.getVelocityY(), 0.01f);
        assertTrue(movementComponent.isMoving());
    }

    @Test
    void testSetMoveSpeed() {
        movementComponent.setMoveSpeed(200);
        assertEquals(200, movementComponent.getMoveSpeed(), 0.01f);
    }

    @Test
    void testUpdate() {
        movementComponent.setVelocity(100, 0);
        movementComponent.update(0.5f);

        assertEquals(50, movementComponent.getX(), 0.01f);
        assertEquals(0, movementComponent.getY(), 0.01f);
    }

    @Test
    void testUpdateNotMoving() {
        movementComponent.update(0.5f);
        assertEquals(0, movementComponent.getX(), 0.01f);
    }

    @Test
    void testMove() {
        movementComponent.move(1, 0);
        assertEquals(100, movementComponent.getVelocityX(), 0.01f);
        assertEquals(0, movementComponent.getVelocityY(), 0.01f);
        assertTrue(movementComponent.isMoving());
    }

    @Test
    void testMoveDiagonal() {
        movementComponent.move(1, 1);
        // 归一化后速度应为 100 * (1/sqrt(2))
        float expectedVelocity = 100 / (float) Math.sqrt(2);
        assertEquals(expectedVelocity, movementComponent.getVelocityX(), 0.01f);
        assertEquals(expectedVelocity, movementComponent.getVelocityY(), 0.01f);
    }

    @Test
    void testMoveZero() {
        movementComponent.move(0, 0);
        assertFalse(movementComponent.isMoving());
    }

    @Test
    void testStop() {
        movementComponent.move(1, 0);
        movementComponent.stop();

        assertEquals(0, movementComponent.getVelocityX(), 0.01f);
        assertEquals(0, movementComponent.getVelocityY(), 0.01f);
        assertFalse(movementComponent.isMoving());
    }

    @Test
    void testDistanceTo() {
        movementComponent.setPosition(0, 0);
        assertEquals(10, movementComponent.distanceTo(10, 0), 0.01f);
        assertEquals(10, movementComponent.distanceTo(0, 10), 0.01f);
        assertEquals(Math.sqrt(200), movementComponent.distanceTo(10, 10), 0.01f);
    }
}
