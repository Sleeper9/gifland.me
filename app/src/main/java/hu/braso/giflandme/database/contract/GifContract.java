package hu.braso.giflandme.database.contract;

import android.provider.BaseColumns;

/**
 * Created by Illés László on 2015.12.13..
 */
public final class GifContract {

    public GifContract() {
    }

    public static abstract class GifEntry implements BaseColumns {
        public static final String TABLE_NAME = "gif";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_XXX = "xxx";
        public static final String COLUMN_CONTENT = "content";
    }
}


