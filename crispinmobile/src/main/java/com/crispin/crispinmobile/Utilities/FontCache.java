package com.crispin.crispinmobile.Utilities;

import com.crispin.crispinmobile.UserInterface.Font;

import java.util.HashMap;
import java.util.Map;

public class FontCache {
    private static final Map<Integer, Font> fontCache = new HashMap<>();

    // Pairing function
    private static int generateUniqueId(int a, int b) {
        return (((a + b) * (a + b + 1)) / 2) + b;
    }

    public static Font getFont(int resourceId, int size) {
        // Check if this exists in the cache
        int uniqueId = generateUniqueId(resourceId, size);

        if (fontCache.containsKey(uniqueId)) {
            return fontCache.get(uniqueId);
        }

        Font font = new Font(resourceId, size);
        fontCache.put(uniqueId, font);
        return font;
    }

    public static void removeAll() {
        for (Font font : fontCache.values()) {
            font.unload();
        }
    }
}
