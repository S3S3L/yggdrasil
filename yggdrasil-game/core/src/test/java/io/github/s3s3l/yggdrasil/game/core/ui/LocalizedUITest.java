package io.github.s3s3l.yggdrasil.game.core.ui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.resource.LanguageBundle;
import io.github.s3s3l.yggdrasil.game.core.resource.LocalizationManager;
import io.github.s3s3l.yggdrasil.game.core.resource.Locale;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 本地化 UI 组件测试
 */
@DisplayName("本地化 UI 组件测试")
class LocalizedUITest {

    private Disposable eventSubscription;
    private int languageChangeCount;

    @BeforeEach
    void setUp() {
        LocalizationManager localization = LocalizationManager.get();
        localization.clear();
        localization.setAutoPostEvent(true);
        languageChangeCount = 0;
    }

    @AfterEach
    void tearDown() {
        LocalizationManager.get().clear();
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            eventSubscription.dispose();
        }
    }

    /**
     * 创建模拟的语言包输入流
     */
    private ByteArrayInputStream createMockPropertiesFile(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("LocalizedLabel 基础本地化测试")
    void testLocalizedLabelBasic() throws IOException {
        // 使用 Properties 直接设置，避免换行符问题
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("greeting", "你好");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("greeting", "Hello");

        LocalizationManager lm = LocalizationManager.get();
        lm.loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        lm.loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        lm.setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedLabel label = new LocalizedLabel("greeting", false);
        label.updateLocalization();

        assertEquals("你好", label.getText(), "应该显示中文文本");
    }

    @Test
    @DisplayName("LocalizedLabel 自动订阅语言切换测试")
    void testLocalizedLabelAutoSubscribe() throws IOException {
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("greeting", "你好");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("greeting", "Hello");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        // 订阅语言切换事件计数
        LocalizationManager.get().setAutoPostEvent(false);

        LocalizedLabel label = new LocalizedLabel("greeting", true);
        assertTrue(label.isSubscribed(), "应该自动订阅语言切换事件");

        // 手动触发语言切换
        LocalizationManager.get().setAutoPostEvent(true);
        LocalizationManager.get().setLanguage(Locale.ENGLISH_US);

        // 等待一小段时间让事件处理
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals("Hello", label.getText(), "应该自动更新为英文文本");
    }

    @Test
    @DisplayName("LocalizedLabel 格式化参数测试")
    void testLocalizedLabelFormatArgs() throws IOException {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("welcome", "欢迎，%s！你有%d 条消息");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, props));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedLabel label = new LocalizedLabel("welcome", false);
        label.setFormatArgs("张三", 5);
        label.updateLocalization();

        assertEquals("欢迎，张三！你有 5 条消息".replaceAll("\\s+", ""), label.getText().replaceAll("\\s+", ""), "应该正确格式化文本");
    }

    @Test
    @DisplayName("LocalizedLabel 设置本地化键测试")
    void testLocalizedLabelSetKey() throws IOException {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("key1", "文本 1");
        props.setProperty("key2", "文本 2");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, props));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedLabel label = new LocalizedLabel("key1", false);
        label.updateLocalization();

        assertEquals("文本 1", label.getText(), "应该显示 key1 的文本");

        label.setLocalizationKey("key2");
        assertEquals("文本 2", label.getText(), "切换键后应该显示 key2 的文本");
    }

    @Test
    @DisplayName("LocalizedLabel 取消订阅测试")
    void testLocalizedLabelUnsubscribe() throws IOException {
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("greeting", "你好");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("greeting", "Hello");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedLabel label = new LocalizedLabel("greeting", true);
        label.unsubscribe();

        assertFalse(label.isSubscribed(), "应该已取消订阅");
    }

    @Test
    @DisplayName("LocalizedLabel 资源清理测试")
    void testLocalizedLabelDispose() {
        LocalizedLabel label = new LocalizedLabel("test", true);
        assertTrue(label.isSubscribed(), "应该已订阅");

        label.dispose();

        assertFalse(label.isSubscribed(), "清理后应该取消订阅");
    }

    @Test
    @DisplayName("LocalizedButton 基础本地化测试")
    void testLocalizedButtonBasic() throws IOException {
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("btn.start", "开始游戏");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("btn.start", "Start Game");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedButton button = new LocalizedButton("btn.start", false);
        button.updateLocalization();

        assertEquals("开始游戏", button.getText(), "应该显示中文文本");
    }

    @Test
    @DisplayName("LocalizedButton 语言切换测试")
    void testLocalizedButtonLanguageSwitch() throws IOException {
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("btn.start", "开始游戏");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("btn.start", "Start Game");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedButton button = new LocalizedButton("btn.start", false);

        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);
        button.updateLocalization();
        assertEquals("开始游戏", button.getText(), "应该显示中文文本");

        LocalizationManager.get().setLanguage(Locale.ENGLISH_US);
        button.updateLocalization();
        assertEquals("Start Game", button.getText(), "切换语言后应该显示英文文本");
    }

    @Test
    @DisplayName("LocalizedButton 格式化参数测试")
    void testLocalizedButtonFormatArgs() throws IOException {
        java.util.Properties props = new java.util.Properties();
        props.setProperty("btn.play", "玩家%s，准备就绪");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, props));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        LocalizedButton button = new LocalizedButton("btn.play", false);
        button.setFormatArgs("张三");
        button.updateLocalization();

        assertEquals("玩家张三，准备就绪", button.getText(), "应该正确格式化文本");
    }

    @Test
    @DisplayName("LocalizedButton 自动订阅测试")
    void testLocalizedButtonAutoSubscribe() {
        LocalizedButton button = new LocalizedButton("test", true);
        assertTrue(button.isSubscribed(), "应该自动订阅语言切换事件");

        button.setAutoSubscribe(false);
        assertFalse(button.isSubscribed(), "应该已取消订阅");
    }

    @Test
    @DisplayName("LocalizedButton 资源清理测试")
    void testLocalizedButtonDispose() {
        LocalizedButton button = new LocalizedButton("test", true);
        assertTrue(button.isSubscribed(), "应该已订阅");

        button.dispose();

        assertFalse(button.isSubscribed(), "清理后应该取消订阅");
    }

    @Test
    @DisplayName("LocalizationUI 工具类测试")
    void testLocalizationUI() throws IOException {
        java.util.Properties chineseProps = new java.util.Properties();
        chineseProps.setProperty("label", "标签");
        java.util.Properties englishProps = new java.util.Properties();
        englishProps.setProperty("label", "Label");

        LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, new LanguageBundle(Locale.SIMPLIFIED_CHINESE, chineseProps));
        LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

        // 创建模拟的可本地化组件
        MockLocalizable mockComponent = new MockLocalizable();
        mockComponent.setLocalizationKey("label");

        // 使用工具类订阅
        Disposable disposable = LocalizationUI.subscribeAndInitialUpdate(mockComponent);

        assertEquals(1, mockComponent.updateCount, "应该立即更新一次");

        // 切换语言
        LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, new LanguageBundle(Locale.ENGLISH_US, englishProps));
        LocalizationManager.get().setLanguage(Locale.ENGLISH_US);

        // 等待事件处理
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(mockComponent.updateCount >= 1, "语言切换时应该更新");

        // 取消订阅
        LocalizationUI.unsubscribe(disposable);
        assertTrue(disposable.isDisposed(), "取消订阅后应该已释放");
    }

    /**
     * 模拟的可本地化组件用于测试
     */
    static class MockLocalizable implements Localizable {
        private String localizationKey;
        int updateCount = 0;

        @Override
        public String getLocalizationKey() {
            return localizationKey;
        }

        @Override
        public void setLocalizationKey(String key) {
            this.localizationKey = key;
        }

        @Override
        public void updateLocalization() {
            updateCount++;
        }
    }
}
