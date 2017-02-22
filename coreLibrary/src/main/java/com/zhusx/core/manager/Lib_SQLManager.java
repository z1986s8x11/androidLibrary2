package com.zhusx.core.manager;

import android.database.Cursor;

/**
 * boolean 有三种状态, 0(false) 1(true) 和 null
 * <p>
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/12 9:27
 */
public class Lib_SQLManager {
    /**
     * 建表
     */
    public static final String CreateTableKey = "CREATE TABLE IF NOT EXISTS ";
    /**
     * 主键
     */
    public static final String PrimaryKeyAutoKey = " PRIMARY KEY AUTOINCREMENT ";
    /**
     * 字段外键关联表的某字段 级联删除
     */
    public static final String ForeignKeyOnDeleteKey = " foreign key (?) REFERENCES ?(?) ON DELETE CASCADE ";
    /**
     * 不重复
     */
    public static final String UniqueKey = " UNIQUE ";
    /**
     * 必须 DATETIME 类型 当前时间
     */
    public static final String CurrentTimeKey = " (datetime(CURRENT_TIMESTAMP,'localtime'))";
    /**
     * 字符串
     */
    public static final String TextKey = " TEXT ";
    /**
     * 整数 0 - 4294967295
     */
    public static final String IntegerKey = " INTEGER ";
    /**
     * 长整形 0 -9223372036854775807
     */
    public static final String LongKey = " BIGINT ";
    /**
     * 浮点数
     */
    public static final String FloatKey = " REAL ";
    /**
     * 日期类型
     */
    public static final String DateKey = " DATETIME ";
    /**
     * 联合主键
     */
    public static final String PrimaryKey = " PRIMARY KEY(?,?) ";

    public enum SQLKeyEnum {
        /**
         * 什么都不做
         */
        DEFAULT(""),
        /**
         * 不能等于Null
         */
        NO_NULL("NOT NULL"),
        /**
         * 主键
         */
        PRIMARY_KEY_AUTOINCREMENT("PRIMARY KEY AUTOINCREMENT"),
        /**
         * 不能重复
         */
        UNIQUE("UNIQUE");
        private String value;

        SQLKeyEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    public enum SQLDefaultEnum {
        /**
         * 什么都不做
         */
        DEFAULT(""),
        /**
         * 默认为空
         */
        NULL("NULL"),
        /** 默认当前日期 */
        // CURRENT_DATE("(datetime(CURRENT_TIMESTAMP,'localtime'))"),
        /**
         * 默认为true
         */
        TRUE("TRUE"),
        /**
         * 默认为false
         */
        FALSE("FALSE"),
        /**
         * -1
         */
        NegativeOne("-1");
        private String value;

        SQLDefaultEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    /**
     * 当前日期的前后多少天日期
     */
    public static String _getDayDateKey(int dayCount) {
        if (dayCount > 0) {
            return " date('now','start of day','+? days') ";
        } else {
            return " date('now','start of day','-? days') ";
        }
    }

    /**
     * @return 当前月份的第一天
     */
    public static String _getFirstDayFromMonth() {
        return "date('now','start of month')";
    }

    public static String debug(Cursor sqlCursor) {
        if (sqlCursor == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] titles = sqlCursor.getColumnNames();
        for (int i = 0; i < titles.length; i++) {
            sb.append(titles[i]);
            sb.append("\t");
        }
        sb.append("\n");
        while (sqlCursor.moveToNext()) {
            for (int i = 0; i < sqlCursor.getColumnCount(); i++) {
                sb.append(sqlCursor.getString(i));
                sb.append("\t");
            }
            sb.append("\n");
        }
        sqlCursor.close();
        return sb.toString();
    }
}
