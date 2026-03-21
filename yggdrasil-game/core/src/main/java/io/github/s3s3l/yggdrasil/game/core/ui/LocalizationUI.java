package io.github.s3s3l.yggdrasil.game.core.ui;

import io.github.s3s3l.yggdrasil.game.core.event.EventBus;
import io.github.s3s3l.yggdrasil.game.core.event.LanguageChangeEvent;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 本地化 UI 工具类
 * 帮助 UI 组件响应语言切换事件
 */
public class LocalizationUI {

    /**
     * 订阅语言切换事件
     *
     * @param localizable 可本地化的 UI 组件
     * @return 订阅处理器，用于取消订阅
     */
    public static Disposable subscribeLocalization(Localizable localizable) {
        return EventBus.get()
                .observe(LanguageChangeEvent.class)
                .subscribe(event -> localizable.updateLocalization());
    }

    /**
     * 订阅语言切换事件，并立即更新一次
     *
     * @param localizable 可本地化的 UI 组件
     * @return 订阅处理器，用于取消订阅
     */
    public static Disposable subscribeAndInitialUpdate(Localizable localizable) {
        // 立即更新一次
        localizable.updateLocalization();
        // 订阅后续的语言切换事件
        return subscribeLocalization(localizable);
    }

    /**
     * 取消订阅
     *
     * @param disposable 订阅处理器
     */
    public static void unsubscribe(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
