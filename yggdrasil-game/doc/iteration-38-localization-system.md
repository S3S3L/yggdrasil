# Iteration 38 - 本地化系统 (2026-03-21)

## 完成状态
✅ 已完成 - 32 个测试全部通过

## 实现内容

### 核心架构

#### Locale - 语言区域类
- 预定义常用语言区域（简体中文、繁体中文、英语、日语、韩语）
- 支持自定义语言区域
- 通过标识符快速查找（如 "zh_CN"）

```java
// 预定义语言区域
Locale.SIMPLIFIED_CHINESE  // zh_CN
Locale.TRADITIONAL_CHINESE // zh_TW
Locale.ENGLISH_US          // en_US
Locale.ENGLISH_UK          // en_GB
Locale.JAPANESE            // ja_JP
Locale.KOREAN              // ko_KR

// 从标识符创建
Locale locale = Locale.forIdentifier("zh_CN");
```

#### LanguageBundle - 语言包类
- 存储键值对形式的文本资源
- 支持回退语言包（当某个键不存在时使用回退语言包）
- 支持文本格式化（使用 `String.format` 语法）

```java
// 从 Properties 创建语言包
Properties props = new Properties();
props.setProperty("greeting", "你好，%s！");
LanguageBundle bundle = new LanguageBundle(Locale.SIMPLIFIED_CHINESE, props);

// 获取文本
String text = bundle.getText("greeting", "张三");  // 返回：你好，张三！
String textWithDefault = bundle.getText("unknown", "默认值");
```

#### LocalizationManager - 本地化管理器（单例）
- 管理多语言文本资源
- 支持运行时语言切换
- 自动发布语言切换事件（通过 EventBus）

```java
// 加载语言包
LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, inputStream);

// 设置当前语言
LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

// 获取文本
String text = LocalizationManager.get().get("greeting");
String formatted = LocalizationManager.get().get("greeting", (Object) "张三");
```

#### LanguageChangeEvent - 语言切换事件
- 当语言改变时通过 EventBus 发布
- 包含旧语言区域和新语言区域

```java
EventBus.get().observe(LanguageChangeEvent.class).subscribe(event -> {
    System.out.println("语言从 " + event.getOldLocale() + " 切换到 " + event.getNewLocale());
});
```

### UI 本地化支持

#### Localizable - 可本地化接口
```java
public interface Localizable {
    String getLocalizationKey();
    void setLocalizationKey(String key);
    void updateLocalization();
}
```

#### LocalizationUI - UI 本地化工具类
- 帮助 UI 组件订阅语言切换事件
- 支持订阅时立即更新

```java
// 订阅并立即更新
Disposable disposable = LocalizationUI.subscribeAndInitialUpdate(component);

// 取消订阅
LocalizationUI.unsubscribe(disposable);
```

#### LocalizedLabel - 支持本地化的 UILabel
- 自动响应语言切换事件
- 支持文本格式化参数
- 可配置是否自动订阅语言切换

```java
// 创建时自动订阅
LocalizedLabel label = new LocalizedLabel("ui.welcome");

// 创建时不自动订阅
LocalizedLabel label = new LocalizedLabel("ui.welcome", false);
label.updateLocalization();

// 设置格式化参数
label.setFormatArgs("张三");

// 手动取消订阅
label.unsubscribe();
```

#### LocalizedButton - 支持本地化的 UIButton
- 功能与 LocalizedLabel 类似
- 适用于按钮文本

### 使用示例

#### 基础使用
```java
// 1. 加载语言包
LocalizationManager.get().loadLanguage(Locale.SIMPLIFIED_CHINESE, chineseInputStream);
LocalizationManager.get().loadLanguage(Locale.ENGLISH_US, englishInputStream);

// 2. 设置当前语言
LocalizationManager.get().setLanguage(Locale.SIMPLIFIED_CHINESE);

// 3. 获取文本
String title = LocalizationManager.get().get("game.title");

// 4. 切换语言（会自动触发 LanguageChangeEvent 事件）
LocalizationManager.get().setLanguage(Locale.ENGLISH_US);
```

#### UI 使用
```java
// 使用本地化标签
LocalizedLabel titleLabel = new LocalizedLabel("ui.title");
titleLabel.setPosition(100, 100);

// 使用本地化按钮
LocalizedButton startButton = new LocalizedButton("ui.start");
startButton.setOnClickListener(() -> {
    // 开始游戏
});
```

#### 语言包文件格式
```properties
# 基础文本
game.title=游戏标题
ui.start=开始游戏
ui.quit=退出游戏

# 格式化文本（使用 String.format 语法）
ui.welcome=欢迎，%s！
ui.score=得分：%d
ui.message=你有%d 条消息，剩余%d 积分
```

## 测试结果

| 测试类 | 测试数 | 状态 |
|--------|--------|------|
| LocalizationManagerTest | 20 | ✅ |
| LocalizedUITest | 12 | ✅ |

### 测试覆盖

**LocalizationManagerTest:**
- testGetInstance - 单例测试
- testLoadLanguage - 加载语言包
- testLoadMultipleLanguages - 加载多个语言包
- testSetLanguage - 设置当前语言
- testSetLanguageNotLoaded - 设置未加载的语言抛出异常
- testTrySetLanguage - 尝试设置语言
- testGetText - 获取文本
- testGetNonExistentKey - 获取不存在的键
- testGetTextWithDefault - 获取文本带默认值
- testGetTextWithFormat - 格式化文本
- testLanguageChangeEvent - 语言切换事件
- testDisableAutoPostEvent - 关闭自动发布事件
- testUnloadLanguage - 卸载语言包
- testSetDefaultLocale - 设置默认语言
- testGetInLocale - 获取指定语言的文本
- testSetText - 动态添加文本
- testClear - 清空所有语言包
- testLocale - Locale 类测试
- testLanguageBundleDirectCreation - 直接创建语言包
- testLanguageBundleFallback - 语言包回退

**LocalizedUITest:**
- testLocalizedLabelBasic - 基础本地化测试
- testLocalizedLabelAutoSubscribe - 自动订阅语言切换
- testLocalizedLabelFormatArgs - 格式化参数测试
- testLocalizedLabelSetKey - 设置本地化键
- testLocalizedLabelUnsubscribe - 取消订阅
- testLocalizedLabelDispose - 资源清理
- testLocalizedButtonBasic - 按钮基础本地化
- testLocalizedButtonLanguageSwitch - 按钮语言切换
- testLocalizedButtonFormatArgs - 按钮格式化参数
- testLocalizedButtonAutoSubscribe - 按钮自动订阅
- testLocalizedButtonDispose - 按钮资源清理
- testLocalizationUI - 工具类测试

## 代码变更

### 新增文件
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/Locale.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/LanguageBundle.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/LocalizationManager.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/event/LanguageChangeEvent.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/ui/Localizable.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/ui/LocalizationUI.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/ui/LocalizedLabel.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/ui/LocalizedButton.java`
- `src/test/java/io/github/s3s3l/yggdrasil/game/core/resource/LocalizationManagerTest.java`
- `src/test/java/io/github/s3s3l/yggdrasil/game/core/ui/LocalizedUITest.java`

## 下一步：Iteration 39 - 对象池和缓存优化
- 通用对象池实现
- 碰撞体对象池
- 事件对象池
- 缓存清理策略优化
