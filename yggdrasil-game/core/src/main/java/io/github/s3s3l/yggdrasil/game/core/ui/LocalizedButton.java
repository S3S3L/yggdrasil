package io.github.s3s3l.yggdrasil.game.core.ui;

import io.github.s3s3l.yggdrasil.game.core.resource.LocalizationManager;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 支持本地化的 UI 按钮
 * 自动响应语言切换事件并更新文本
 */
public class LocalizedButton extends UIButton implements Localizable {

    /**
     * 本地化键
     */
    private String localizationKey;

    /**
     * 格式化参数
     */
    private Object[] formatArgs;

    /**
     * 语言切换事件订阅
     */
    private Disposable eventSubscription;

    /**
     * 是否自动订阅语言切换事件
     */
    private boolean autoSubscribe;

    public LocalizedButton() {
        this("");
    }

    public LocalizedButton(String localizationKey) {
        this(localizationKey, true);
    }

    public LocalizedButton(String localizationKey, boolean autoSubscribe) {
        super(localizationKey);
        this.localizationKey = localizationKey;
        this.autoSubscribe = autoSubscribe;
        this.formatArgs = null;

        if (autoSubscribe) {
            subscribeToLanguageChanges();
        }
    }

    /**
     * 订阅语言切换事件
     */
    protected void subscribeToLanguageChanges() {
        if (eventSubscription != null && !eventSubscription.isDisposed()) {
            eventSubscription.dispose();
        }
        eventSubscription = LocalizationUI.subscribeAndInitialUpdate(this);
    }

    /**
     * 取消订阅
     */
    public void unsubscribe() {
        LocalizationUI.unsubscribe(eventSubscription);
        eventSubscription = null;
    }

    @Override
    public String getLocalizationKey() {
        return localizationKey;
    }

    @Override
    public void setLocalizationKey(String key) {
        this.localizationKey = key;
        updateLocalization();
    }

    /**
     * 设置格式化参数
     *
     * @param args 格式化参数
     */
    public void setFormatArgs(Object... args) {
        this.formatArgs = args;
        updateLocalization();
    }

    @Override
    public void updateLocalization() {
        if (localizationKey == null || localizationKey.isEmpty()) {
            return;
        }

        LocalizationManager localization = LocalizationManager.get();
        String localizedText;

        if (formatArgs != null && formatArgs.length > 0) {
            localizedText = localization.get(localizationKey, formatArgs);
        } else {
            localizedText = localization.get(localizationKey);
        }

        // 直接调用父类方法设置文本，绕过保护机制
        super.setText(localizedText);
    }

    @Override
    public void setText(String text) {
        // 如果设置了本地化键，阻止直接设置文本
        if (localizationKey != null && !localizationKey.isEmpty()) {
            return;
        }
        super.setText(text);
    }

    /**
     * 设置是否自动订阅语言切换事件
     *
     * @param autoSubscribe true 为自动订阅
     */
    public void setAutoSubscribe(boolean autoSubscribe) {
        if (this.autoSubscribe != autoSubscribe) {
            this.autoSubscribe = autoSubscribe;
            if (autoSubscribe) {
                subscribeToLanguageChanges();
            } else {
                unsubscribe();
            }
        }
    }

    /**
     * 检查是否正在订阅语言切换事件
     *
     * @return true 如果正在订阅
     */
    public boolean isSubscribed() {
        return eventSubscription != null && !eventSubscription.isDisposed();
    }

    /**
     * 清理资源
     * 在组件销毁时调用
     */
    public void dispose() {
        unsubscribe();
    }
}
