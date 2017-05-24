package org.berendeev.roma.rssreader.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseOpenHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "rss.db";
    private static final int DATABASE_VERSION = 2;

    //ID | CARD | DATE
    public static final String FEEDS_TABLE = "feeds";
    public static final String LINK = "link";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String DATE = "time";
    public static final String THUMBNAIL = "thumbnail";
    public static final String ENCLOSURE = "enclosure";

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        String script = "create table " + FEEDS_TABLE + " (" +
                LINK + " text primary key, " +
                TITLE + " text not null, " +
                DESCRIPTION + " text not null, " +
                AUTHOR + " text not null, " +
                DATE + " text not null, " +
                THUMBNAIL + " text not null, " +
                ENCLOSURE + " text not null);";
        db.execSQL(script);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FEEDS_TABLE);
        onCreate(db);
    }

    @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + FEEDS_TABLE);
        onCreate(db);
    }
}
