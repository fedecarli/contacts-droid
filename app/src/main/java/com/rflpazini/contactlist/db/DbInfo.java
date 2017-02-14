package com.rflpazini.contactlist.db;

import android.provider.BaseColumns;

/**
 * Created by rflpazini on 4/19/16.
 */
public class DbInfo {
    public static final String DB_NAME = "com.rflpazini.contactlist.data";
    public static final int DB_VERSION = 2;

    public class DbEntry implements BaseColumns {
        public static final String TABLE = "contacts";
        public static final String CONT_NAME = "name";
        public static final String CONT_PHONE = "phone";
        public static final String CONT_EMAIL = "email";
    }
}
