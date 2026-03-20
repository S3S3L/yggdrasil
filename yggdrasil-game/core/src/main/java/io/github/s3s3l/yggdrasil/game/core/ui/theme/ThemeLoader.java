package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.patch.NinePatchLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 主题加载器
 * 从 JSON 文件加载主题配置
 */
public class ThemeLoader {

    private final NinePatchLoader ninePatchLoader;
    private final Map<String, ThemeLoaderListener> listeners;

    /**
     * 创建主题加载器
     */
    public ThemeLoader() {
        this.ninePatchLoader = new NinePatchLoader();
        this.listeners = new HashMap<>();
    }

    /**
     * 获取 NinePatch 加载器
     *
     * @return NinePatch 加载器
     */
    public NinePatchLoader getNinePatchLoader() {
        return ninePatchLoader;
    }

    /**
     * 添加主题加载器监听器
     *
     * @param name     监听器名称
     * @param listener 监听器
     */
    public void addListener(String name, ThemeLoaderListener listener) {
        if (name != null && listener != null) {
            listeners.put(name, listener);
        }
    }

    /**
     * 移除主题加载器监听器
     *
     * @param name 监听器名称
     */
    public void removeListener(String name) {
        listeners.remove(name);
    }

    /**
     * 通知主题加载完成
     *
     * @param theme 加载的主题
     */
    protected void notifyLoaded(UITheme theme) {
        for (ThemeLoaderListener listener : listeners.values()) {
            try {
                listener.onThemeLoaded(theme);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 解析主题 JSON
     *
     * @param name   主题名称
     * @param json   JSON 字符串
     * @param parent 父主题
     * @return 解析的主题
     */
    public UITheme parseTheme(String name, String json, UITheme parent) {
        UITheme theme = new UITheme(name, parent);

        // 简单的 JSON 解析（避免外部依赖）
        // 格式示例：
        // {
        //   "colors": {
        //     "primary": "#FF0000",
        //     "background": "#FFFFFF"
        //   },
        //   "styles": {
        //     "Button": {
        //       "backgroundColor": "#00FF00",
        //       "fontSize": 14,
        //       "border": { "width": 1, "color": "#000000", "radius": 4 }
        //     }
        //   }
        // }

        try {
            // 解析 colors
            int colorsStart = json.indexOf("\"colors\"");
            if (colorsStart != -1) {
                int braceStart = json.indexOf("{", colorsStart);
                int braceEnd = findMatchingBrace(json, braceStart);
                if (braceStart != -1 && braceEnd != -1) {
                    String colorsJson = json.substring(braceStart, braceEnd + 1);
                    parseColors(theme, colorsJson);
                }
            }

            // 解析 styles
            int stylesStart = json.indexOf("\"styles\"");
            if (stylesStart != -1) {
                int braceStart = json.indexOf("{", stylesStart);
                int braceEnd = findMatchingBrace(json, braceStart);
                if (braceStart != -1 && braceEnd != -1) {
                    String stylesJson = json.substring(braceStart, braceEnd + 1);
                    parseStyles(theme, stylesJson);
                }
            }

            // 解析 fonts
            int fontsStart = json.indexOf("\"fonts\"");
            if (fontsStart != -1) {
                int braceStart = json.indexOf("{", fontsStart);
                int braceEnd = findMatchingBrace(json, braceStart);
                if (braceStart != -1 && braceEnd != -1) {
                    String fontsJson = json.substring(braceStart, braceEnd + 1);
                    parseFonts(theme, fontsJson);
                }
            }

        } catch (Exception e) {
            // 解析失败时返回空主题
        }

        notifyLoaded(theme);
        return theme;
    }

    /**
     * 从流加载主题
     *
     * @param name   主题名称
     * @param stream 输入流
     * @param parent 父主题
     * @return 加载的主题
     * @throws IOException 读取失败
     */
    public UITheme loadTheme(String name, InputStream stream, UITheme parent) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        UITheme theme = parseTheme(name, sb.toString(), parent);
        notifyLoaded(theme);
        return theme;
    }

    /**
     * 解析颜色
     */
    private void parseColors(UITheme theme, String json) {
        // 简单解析：提取 "name": "#value" 对
        int i = 0;
        while ((i = json.indexOf("\"", i)) != -1) {
            int end = json.indexOf("\"", i + 1);
            if (end == -1) break;
            String key = json.substring(i + 1, end);

            int colon = json.indexOf(":", end);
            if (colon == -1) break;

            int hashStart = json.indexOf("#", colon);
            if (hashStart == -1) break;
            int hashEnd = json.indexOf("\"", hashStart);
            if (hashEnd == -1) break;

            String colorStr = json.substring(hashStart + 1, hashEnd);
            try {
                UIColor color = parseColor(colorStr);
                theme.setColor(key, color);
            } catch (Exception e) {
                // 忽略无效颜色
            }

            i = hashEnd + 1;
        }
    }

    /**
     * 解析字体
     */
    private void parseFonts(UITheme theme, String json) {
        int i = 0;
        while ((i = json.indexOf("\"", i)) != -1) {
            int end = json.indexOf("\"", i + 1);
            if (end == -1) break;
            String key = json.substring(i + 1, end);

            int colon = json.indexOf(":", end);
            if (colon == -1) break;

            int valueStart = json.indexOf("\"", colon + 1);
            if (valueStart == -1) break;
            int valueEnd = json.indexOf("\"", valueStart + 1);
            if (valueEnd == -1) break;

            String value = json.substring(valueStart + 1, valueEnd);
            theme.setFont(key, value);

            i = valueEnd + 1;
        }
    }

    /**
     * 解析样式
     */
    private void parseStyles(UITheme theme, String json) {
        // 简化解析，提取组件样式
        // 样式名在 "  " 中，后面跟着 { } 包含样式属性
        int i = 0;
        while ((i = json.indexOf("\"", i)) != -1) {
            int end = json.indexOf("\"", i + 1);
            if (end == -1) break;

            String componentName = json.substring(i + 1, end);

            int braceStart = json.indexOf("{", end);
            if (braceStart == -1) break;

            int braceEnd = findMatchingBrace(json, braceStart);
            if (braceEnd == -1) break;

            String styleJson = json.substring(braceStart, braceEnd + 1);
            UIStyle style = parseStyle(styleJson);
            theme.setStyle(componentName, style);

            i = braceEnd + 1;
        }
    }

    /**
     * 解析单个样式
     */
    private UIStyle parseStyle(String json) {
        UIStyle style = new UIStyle();

        // 解析 backgroundColor
        int bgStart = json.indexOf("\"backgroundColor\"");
        if (bgStart != -1) {
            int hashStart = json.indexOf("#", bgStart);
            if (hashStart != -1) {
                int hashEnd = json.indexOf("\"", hashStart);
                if (hashEnd != -1) {
                    String colorStr = json.substring(hashStart + 1, hashEnd);
                    style.setBackgroundColor(parseColor(colorStr));
                }
            }
        }

        // 解析 fontSize
        int fontSizeStart = json.indexOf("\"fontSize\"");
        if (fontSizeStart != -1) {
            int colon = json.indexOf(":", fontSizeStart);
            if (colon != -1) {
                int end = Math.min(json.indexOf(",", colon), json.indexOf("}", colon));
                if (end != -1) {
                    try {
                        String value = json.substring(colon + 1, end).trim();
                        style.setFontSize(Float.parseFloat(value));
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }

        // 解析 fontColor
        int fontColorStart = json.indexOf("\"fontColor\"");
        if (fontColorStart != -1) {
            int hashStart = json.indexOf("#", fontColorStart);
            if (hashStart != -1) {
                int hashEnd = json.indexOf("\"", hashStart);
                if (hashEnd != -1) {
                    String colorStr = json.substring(hashStart + 1, hashEnd);
                    style.setFontColor(parseColor(colorStr));
                }
            }
        }

        // 解析 opacity
        int opacityStart = json.indexOf("\"opacity\"");
        if (opacityStart != -1) {
            int colon = json.indexOf(":", opacityStart);
            if (colon != -1) {
                int end = Math.min(json.indexOf(",", colon), json.indexOf("}", colon));
                if (end != -1) {
                    try {
                        String value = json.substring(colon + 1, end).trim();
                        style.setOpacity(Float.parseFloat(value));
                    } catch (NumberFormatException e) {
                        // 忽略
                    }
                }
            }
        }

        return style;
    }

    /**
     * 解析颜色值
     */
    private UIColor parseColor(String colorStr) {
        if (colorStr.length() == 6) {
            // #RRGGBB
            int r = Integer.parseInt(colorStr.substring(0, 2), 16);
            int g = Integer.parseInt(colorStr.substring(2, 4), 16);
            int b = Integer.parseInt(colorStr.substring(4, 6), 16);
            return new UIColor(r / 255f, g / 255f, b / 255f, 1f);
        } else if (colorStr.length() == 8) {
            // #RRGGBBAA
            int r = Integer.parseInt(colorStr.substring(0, 2), 16);
            int g = Integer.parseInt(colorStr.substring(2, 4), 16);
            int b = Integer.parseInt(colorStr.substring(4, 6), 16);
            int a = Integer.parseInt(colorStr.substring(6, 8), 16);
            return new UIColor(r / 255f, g / 255f, b / 255f, a / 255f);
        }
        return UIColor.WHITE;
    }

    /**
     * 查找匹配的右花括号
     */
    private int findMatchingBrace(String json, int start) {
        int count = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escaped) {
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                continue;
            }

            if (c == '"') {
                inString = !inString;
                continue;
            }

            if (!inString) {
                if (c == '{') {
                    count++;
                } else if (c == '}') {
                    count--;
                    if (count == 0) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 主题加载器监听器接口
     */
    public interface ThemeLoaderListener {
        /**
         * 主题加载完成时调用
         *
         * @param theme 加载的主题
         */
        void onThemeLoaded(UITheme theme);
    }
}
