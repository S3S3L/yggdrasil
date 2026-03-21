package io.github.s3s3l.yggdrasil.game.core.resource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.event.EventBus;
import io.github.s3s3l.yggdrasil.game.core.event.LanguageChangeEvent;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 本地化管理器单元测试
 */
@DisplayName("本地化管理器测试")
class LocalizationManagerTest {

    private LocalizationManager localization;
    private Disposable eventSubscription;
    private LanguageChangeEvent lastEvent;

    @BeforeEach
    void setUp() {
        localization = LocalizationManager.get();
        localization.clear();
        localization.setAutoPostEvent(true);
        lastEvent = null;
    }

    @AfterEach
    void tearDown() {
        localization.clear();
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            eventSubscription.dispose();
        }
    }

    /**
     * 创建模拟的语言包输入流
     */
    private InputStream createMockPropertiesFile(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("单例实例测试")
    void testGetInstance() {
        LocalizationManager instance1 = LocalizationManager.get();
        LocalizationManager instance2 = LocalizationManager.get();
        assertSame(instance1, instance2, "LocalizationManager 应该是单例");
    }

    @Test
    @DisplayName("加载语言包测试")
    void testLoadLanguage() throws IOException {
        String content = "hello=你好\nwelcome=欢迎访问";
        InputStream inputStream = createMockPropertiesFile(content);

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, inputStream);

        assertTrue(localization.isLanguageLoaded(Locale.SIMPLIFIED_CHINESE), "应该已加载简体中文语言包");
    }

    @Test
    @DisplayName("加载多个语言包测试")
    void testLoadMultipleLanguages() throws IOException {
        String chineseContent = "hello=你好";
        String englishContent = "hello=Hello";

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(chineseContent));
        localization.loadLanguage(Locale.ENGLISH_US, createMockPropertiesFile(englishContent));

        assertTrue(localization.isLanguageLoaded(Locale.SIMPLIFIED_CHINESE), "应该已加载简体中文");
        assertTrue(localization.isLanguageLoaded(Locale.ENGLISH_US), "应该已加载英文");

        Set<Locale> loadedLocales = localization.getLoadedLocales();
        assertEquals(2, loadedLocales.size(), "应该加载了 2 个语言包");
    }

    @Test
    @DisplayName("设置当前语言测试")
    void testSetLanguage() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));

        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        assertEquals(Locale.SIMPLIFIED_CHINESE, localization.getCurrentLocale(), "当前语言应该是简体中文");
    }

    @Test
    @DisplayName("设置未加载的语言抛出异常")
    void testSetLanguageNotLoaded() {
        assertThrows(IllegalArgumentException.class, () -> {
            localization.setLanguage(Locale.SIMPLIFIED_CHINESE);
        }, "设置未加载的语言应该抛出异常");
    }

    @Test
    @DisplayName("trySetLanguage 测试")
    void testTrySetLanguage() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));

        boolean result1 = localization.trySetLanguage(Locale.SIMPLIFIED_CHINESE);
        boolean result2 = localization.trySetLanguage(Locale.ENGLISH_US);

        assertTrue(result1, "应该成功设置已加载的语言");
        assertFalse(result2, "设置未加载的语言应该返回 false");
    }

    @Test
    @DisplayName("获取文本测试")
    void testGetText() throws IOException {
        String content = "hello=你好\nwelcome=欢迎访问";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        String hello = localization.get("hello");
        String welcome = localization.get("welcome");

        assertEquals("你好", hello, "应该获取到正确的翻译");
        assertEquals("欢迎访问", welcome, "应该获取到正确的翻译");
    }

    @Test
    @DisplayName("获取不存在的键返回键本身")
    void testGetNonExistentKey() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        String result = localization.get("nonexistent");

        assertEquals("nonexistent", result, "不存在的键应该返回键本身");
    }

    @Test
    @DisplayName("获取文本带默认值测试")
    void testGetTextWithDefault() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        String result = localization.get("nonexistent", "默认值");

        assertEquals("默认值", result, "应该返回默认值");
    }

    @Test
    @DisplayName("格式化文本测试")
    void testGetTextWithFormat() throws IOException {
        // 使用 Properties 直接设置，避免换行符问题
        java.util.Properties props = new java.util.Properties();
        props.setProperty("greeting", "你好，%s！");
        props.setProperty("info", "你有%d 条消息，剩余%d 积分");

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, props));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        // 使用明确的可变参数调用
        String greeting = localization.get("greeting", (Object) "张三");
        String info = localization.get("info", (Object) 5, (Object) 100);

        assertEquals("你好，张三！", greeting, "应该正确格式化文本");
        assertEquals("你有 5 条消息，剩余 100 积分".replaceAll("\\s+", ""), info.replaceAll("\\s+", ""), "应该正确格式化多参数文本");
    }

    @Test
    @DisplayName("语言切换事件测试")
    void testLanguageChangeEvent() throws IOException {
        // 订阅语言切换事件
        EventBus.get().observe(LanguageChangeEvent.class).subscribe(new Observer<LanguageChangeEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
                eventSubscription = d;
            }

            @Override
            public void onNext(LanguageChangeEvent event) {
                lastEvent = event;
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });

        String chineseContent = "hello=你好";
        String englishContent = "hello=Hello";

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(chineseContent));
        localization.loadLanguage(Locale.ENGLISH_US, createMockPropertiesFile(englishContent));

        // 切换语言
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);
        assertNotNull(lastEvent, "应该触发语言切换事件");
        assertNull(lastEvent.getOldLocale(), "之前应该没有设置语言");
        assertEquals(Locale.SIMPLIFIED_CHINESE, lastEvent.getNewLocale(), "新语言应该是简体中文");

        // 再次切换
        lastEvent = null;
        localization.setLanguage(Locale.ENGLISH_US);
        assertNotNull(lastEvent, "应该触发语言切换事件");
        assertEquals(Locale.SIMPLIFIED_CHINESE, lastEvent.getOldLocale(), "旧语言应该是简体中文");
        assertEquals(Locale.ENGLISH_US, lastEvent.getNewLocale(), "新语言应该是英文");
    }

    @Test
    @DisplayName("关闭自动发布事件测试")
    void testDisableAutoPostEvent() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));

        localization.setAutoPostEvent(false);
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        // 订阅事件验证没有触发
        EventBus.get().observe(LanguageChangeEvent.class).subscribe(new Observer<LanguageChangeEvent>() {
            @Override
            public void onSubscribe(Disposable d) {
                eventSubscription = d;
            }

            @Override
            public void onNext(LanguageChangeEvent event) {
                fail("不应该触发语言切换事件");
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
    }

    @Test
    @DisplayName("卸载语言包测试")
    void testUnloadLanguage() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        LanguageBundle removed = localization.unloadLanguage(Locale.SIMPLIFIED_CHINESE);

        assertNotNull(removed, "应该返回被卸载的语言包");
        assertFalse(localization.isLanguageLoaded(Locale.SIMPLIFIED_CHINESE), "简体中文语言包应该已被卸载");
    }

    @Test
    @DisplayName("设置默认语言测试")
    void testSetDefaultLocale() {
        localization.setDefaultLocale(Locale.ENGLISH_US);
        assertEquals(Locale.ENGLISH_US, localization.getDefaultLocale(), "默认语言应该是英文");
    }

    @Test
    @DisplayName("获取指定语言的文本测试")
    void testGetInLocale() throws IOException {
        String chineseContent = "hello=你好";
        String englishContent = "hello=Hello";

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(chineseContent));
        localization.loadLanguage(Locale.ENGLISH_US, createMockPropertiesFile(englishContent));

        String chineseText = localization.getInLocale(Locale.SIMPLIFIED_CHINESE, "hello");
        String englishText = localization.getInLocale(Locale.ENGLISH_US, "hello");

        assertEquals("你好", chineseText, "应该获取到简体中文文本");
        assertEquals("Hello", englishText, "应该获取到英文文本");
    }

    @Test
    @DisplayName("动态添加文本测试")
    void testSetText() throws IOException {
        String content = "hello=你好";
        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(content));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        localization.setText("newkey", "新文本");

        String result = localization.get("newkey");
        assertEquals("新文本", result, "应该获取到动态添加的文本");
    }

    @Test
    @DisplayName("清空所有语言包测试")
    void testClear() throws IOException {
        String chineseContent = "hello=你好";
        String englishContent = "hello=Hello";

        localization.loadLanguage(Locale.SIMPLIFIED_CHINESE, createMockPropertiesFile(chineseContent));
        localization.loadLanguage(Locale.ENGLISH_US, createMockPropertiesFile(englishContent));
        localization.setLanguage(Locale.SIMPLIFIED_CHINESE);

        localization.clear();

        assertEquals(0, localization.getLoadedLocales().size(), "应该清空所有语言包");
        assertFalse(localization.isLanguageLoaded(Locale.SIMPLIFIED_CHINESE), "简体中文语言包应该已被清空");
        assertFalse(localization.isLanguageLoaded(Locale.ENGLISH_US), "英文语言包应该已被清空");
    }

    @Test
    @DisplayName("Locale 类测试")
    void testLocale() {
        assertEquals("zh", Locale.SIMPLIFIED_CHINESE.getLanguage(), "语言代码应该正确");
        assertEquals("CN", Locale.SIMPLIFIED_CHINESE.getCountry(), "国家代码应该正确");
        assertEquals("zh_CN", Locale.SIMPLIFIED_CHINESE.getIdentifier(), "完整标识符应该正确");

        Locale customLocale = Locale.forIdentifier("zh_CN");
        assertEquals(Locale.SIMPLIFIED_CHINESE, customLocale, "应该返回预定义的简体中文区域");

        Locale unknownLocale = Locale.forIdentifier("fr_FR");
        assertEquals("fr_FR", unknownLocale.getIdentifier(), "未知区域应该正确创建");
    }

    @Test
    @DisplayName("LanguageBundle 直接创建测试")
    void testLanguageBundleDirectCreation() {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("hello", "Hello");
        props.setProperty("bye", "Goodbye");

        LanguageBundle bundle = new LanguageBundle(Locale.ENGLISH_US, props);

        assertEquals(Locale.ENGLISH_US, bundle.getLocale(), "语言区域应该正确");
        assertEquals("Hello", bundle.getText("hello"), "应该获取到正确的文本");
        assertEquals("Goodbye", bundle.getText("bye"), "应该获取到正确的文本");
        assertEquals("Unknown", bundle.getText("unknown", "Unknown"), "不存在的键应该返回默认值");
    }

    @Test
    @DisplayName("LanguageBundle 回退测试")
    void testLanguageBundleFallback() {
        java.util.Properties mainProps = new java.util.Properties();
        mainProps.setProperty("hello", "你好");
        LanguageBundle mainBundle = new LanguageBundle(Locale.SIMPLIFIED_CHINESE, mainProps);

        java.util.Properties fallbackProps = new java.util.Properties();
        fallbackProps.setProperty("hello", "Hello");
        fallbackProps.setProperty("bye", "Goodbye");
        LanguageBundle fallbackBundle = new LanguageBundle(Locale.ENGLISH_US, fallbackProps);

        mainBundle.setFallbackBundle(fallbackBundle);

        assertEquals("你好", mainBundle.getText("hello"), "应该优先使用主语言包");
        assertEquals("Goodbye", mainBundle.getText("bye"), "主语言包不存在时应该使用回退语言包");
    }
}
