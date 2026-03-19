package io.github.s3s3l.yggdrasil.game.core.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源管理系统单元测试
 */
class ResourceManagerTest {

    private ResourceManager resourceManager;

    @BeforeEach
    void setUp() {
        Map<String, Resource> initialResources = new HashMap<>();
        initialResources.put("gold", Resource.builder()
                .id(1L)
                .name("Gold")
                .type(ResourceType.MONEY)
                .amount(100)
                .maxCapacity(1000)
                .incomeRate(10)
                .build());

        resourceManager = ResourceManager.builder()
                .id(1L)
                .name("Test ResourceManager")
                .resources(initialResources)
                .build();
    }

    @Test
    void testGetResourceAmount() {
        assertEquals(100, resourceManager.getResourceAmount("gold"));
        assertEquals(0, resourceManager.getResourceAmount("wood"));
    }

    @Test
    void testAddResource() {
        resourceManager.addResource("gold", 50);
        assertEquals(150, resourceManager.getResourceAmount("gold"));
    }

    @Test
    void testConsumeResource() {
        assertTrue(resourceManager.consumeResource("gold", 50));
        assertEquals(50, resourceManager.getResourceAmount("gold"));

        assertFalse(resourceManager.consumeResource("gold", 100));
    }

    @Test
    void testHasEnoughResource() {
        assertTrue(resourceManager.hasEnoughResource("gold", 50));
        assertFalse(resourceManager.hasEnoughResource("gold", 150));
    }

    @Test
    void testResourceCapacity() {
        resourceManager.addResource("gold", 950);
        assertEquals(1000, resourceManager.getResourceAmount("gold")); // Capped at maxCapacity
    }
}
