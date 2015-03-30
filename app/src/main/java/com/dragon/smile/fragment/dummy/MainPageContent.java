package com.dragon.smile.fragment.dummy;

import com.dragon.smile.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MainPageContent {

    /**
     * important;
     */
    public static final int RAPID_BARBER = 0;


    /**
     * An array of sample (dummy) items.
     */
    public static List<MainPageItem> ITEMS = new ArrayList<MainPageItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, MainPageItem> ITEM_MAP = new HashMap<String, MainPageItem>();

    static {
        // Add 3 sample items.
        addItem(new MainPageItem(String.valueOf(RAPID_BARBER), R.mipmap.ic_launcher, R.string.title_rapid_barber, R.string.content_rapid_barber));
    }

    private static void addItem(MainPageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class MainPageItem {
        public String id;
        public int iconId;
        public int titleId;
        public int contentId;

        public MainPageItem(String id, int resId, int titleId, int contentId) {
            this.id = id;
            this.iconId = resId;
            this.titleId = titleId;
            this.contentId = contentId;
        }

        @Override
        public String toString() {
            return new StringBuilder().append(id).append(":").append(iconId).append(":").append(titleId).append(":").append(contentId).toString();
        }
    }
}
