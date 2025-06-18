package configuration;

import java.awt.Color;

public class Config {
    
    // UI颜色配置
    public static class UIColors {
        // 主色调
        public static final Color PRIMARY_BLUE = new Color(0, 123, 255);
        public static final Color PRIMARY_BLUE_HOVER = new Color(0, 97, 195);
        public static final Color SECONDARY_BLUE = new Color(40, 120, 230);
        public static final Color SECONDARY_BLUE_HOVER = new Color(30, 100, 200);
        public static final Color TITLE_BLUE = new Color(30, 80, 150);
        
        // 背景色
        public static final Color BACKGROUND_WHITE = Color.WHITE;
        public static final Color BACKGROUND_LIGHT = new Color(240, 245, 250);
        
        // 文本色
        public static final Color TEXT_WHITE = Color.WHITE;
        public static final Color TEXT_GRAY = new Color(100, 100, 100);
        public static final Color TEXT_DARK_GRAY = new Color(100, 100, 100);
        
        // 从环境变量加载颜色配置
        public static Color getPrimaryColor() {
            String colorHex = Env.getInstance().getProperty("ui.primary.color");
            return colorHex != null ? Color.decode(colorHex) : PRIMARY_BLUE;
        }
        
        public static Color getPrimaryHoverColor() {
            String colorHex = Env.getInstance().getProperty("ui.primary.hover.color");
            return colorHex != null ? Color.decode(colorHex) : PRIMARY_BLUE_HOVER;
        }
        
        public static Color getSecondaryColor() {
            String colorHex = Env.getInstance().getProperty("ui.secondary.color");
            return colorHex != null ? Color.decode(colorHex) : SECONDARY_BLUE;
        }
        
        public static Color getSecondaryHoverColor() {
            String colorHex = Env.getInstance().getProperty("ui.secondary.hover.color");
            return colorHex != null ? Color.decode(colorHex) : SECONDARY_BLUE_HOVER;
        }
        
        public static Color getTitleColor() {
            String colorHex = Env.getInstance().getProperty("ui.title.color");
            return colorHex != null ? Color.decode(colorHex) : TITLE_BLUE;
        }
        
        public static Color getBackgroundColor() {
            String colorHex = Env.getInstance().getProperty("ui.background.color");
            return colorHex != null ? Color.decode(colorHex) : BACKGROUND_LIGHT;
        }
    }
    
    // 字体配置
    public static class UIFonts {
        public static final String DEFAULT_FONT_NAME = "微软雅黑";
        public static final String ENGLISH_FONT_NAME = "Arial";
        
        public static String getFontName() {
            String fontName = Env.getInstance().getProperty("ui.font.name");
            return fontName != null ? fontName : DEFAULT_FONT_NAME;
        }
        
        public static String getEnglishFontName() {
            String fontName = Env.getInstance().getProperty("ui.font.english.name");
            return fontName != null ? fontName : ENGLISH_FONT_NAME;
        }
    }
}
