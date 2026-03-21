package io.github.s3s3l.yggdrasil.game.core.resource;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * TextureLoader 单元测试
 */
public class TextureLoaderTest {

    private TextureLoader textureLoader;

    @BeforeEach
    void setUp() {
        textureLoader = new TextureLoader();
    }

    @Test
    void testConstructor() {
        assertNotNull(textureLoader);
        assertEquals(TextureResource.class, textureLoader.getResourceType());
    }

    @Test
    void testTextureParams() {
        TextureLoader.TextureParams params = new TextureLoader.TextureParams();

        assertEquals("linear", params.filter);
        assertEquals("clamp", params.wrapS);
        assertEquals("clamp", params.wrapT);
        assertFalse(params.premultiplyAlpha);
    }

    @Test
    void testSetTextureParams() {
        TextureLoader.TextureParams params = new TextureLoader.TextureParams();
        params.filter = "nearest";
        params.wrapS = "repeat";
        params.wrapT = "repeat";

        textureLoader.setTextureParams("test/texture.png", params);
        // 设置成功，不抛出异常
    }

    @Test
    void testIsLoaded() {
        assertFalse(textureLoader.isLoaded("test/texture.png"));
    }

    @Test
    void testLoadParamsHandling() {
        LoadParams params = new LoadParams();
        params.set("filter", "nearest");
        params.set("wrapS", "repeat");
        params.set("wrapT", "repeat");
        params.set("premultiplyAlpha", true);

        // 验证参数能被正确读取
        assertEquals("nearest", params.getString("filter"));
        assertEquals("repeat", params.getString("wrapS"));
        assertTrue(params.getBoolean("premultiplyAlpha"));
    }
}
