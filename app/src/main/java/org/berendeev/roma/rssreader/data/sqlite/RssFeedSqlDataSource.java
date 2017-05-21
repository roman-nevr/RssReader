package org.berendeev.roma.rssreader.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.berendeev.roma.rssreader.domain.entity.RssItem;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.AUTHOR;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.DATE;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.DESCRIPTION;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.ENCLOSURE;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.FEEDS_TABLE;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.LINK;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.THUMBNAIL;
import static org.berendeev.roma.rssreader.data.sqlite.DatabaseOpenHelper.TITLE;

public class RssFeedSqlDataSource {

    private SQLiteDatabase database;
    private ContentValues contentValues;

    public RssFeedSqlDataSource(DatabaseOpenHelper openHelper) {
        database = openHelper.getWritableDatabase();
        contentValues = new ContentValues();
    }

    public List<RssItem> getAllRssItems(){
        List<RssItem> rssItems = new ArrayList<>();

        Cursor cursor = database.query(FEEDS_TABLE, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            rssItems.add(getRssItemFromCursor(cursor));
        }
        cursor.close();
        return rssItems;
    }

    public void saveRssItem(RssItem rssItem) {
        fillContentValues(rssItem);
        database.insertWithOnConflict(FEEDS_TABLE, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void saveAllRssItems(List<RssItem> rssItems){
        for (RssItem rssItem : rssItems) {
            saveRssItem(rssItem);
        }
    }

    public RssItem getRssItem(String link){
        String selection = String.format("%1s = ?", LINK);
        String[] selectionArgs = {link};
        Cursor cursor = null;
        RssItem rssItem;
        cursor = database.query(FEEDS_TABLE, null, selection, selectionArgs, null, null, null, null);
        if(cursor.moveToFirst()){
            rssItem = getRssItemFromCursor(cursor);
        }else {
            rssItem = RssItem.EMPTY;
        }
        cursor.close();
        return rssItem;
    }

    private void fillContentValues(RssItem rssItem){
        contentValues.clear();
        contentValues.put(LINK, rssItem.link());
        contentValues.put(TITLE, rssItem.title());
        contentValues.put(DESCRIPTION, rssItem.title());
        contentValues.put(AUTHOR, rssItem.author());
        contentValues.put(DATE, rssItem.pubDate());
        contentValues.put(THUMBNAIL, rssItem.thumbnail());
        contentValues.put(ENCLOSURE, rssItem.enclosure());
    }

    private RssItem getRssItemFromCursor(Cursor cursor){

        int titleIndex = cursor.getColumnIndex(TITLE);
        int linkIndex = cursor.getColumnIndex(LINK);
        int descriptionIndex = cursor.getColumnIndex(DESCRIPTION);
        int authorIndex = cursor.getColumnIndex(AUTHOR);
        int dateIndex = cursor.getColumnIndex(DATE);
        int thumbnailIndex = cursor.getColumnIndex(THUMBNAIL);
        int enclosureIndex = cursor.getColumnIndex(ENCLOSURE);
        return RssItem.create(
                cursor.getString(titleIndex),
                cursor.getString(linkIndex),
                cursor.getString(descriptionIndex),
                cursor.getString(authorIndex),
                cursor.getLong(dateIndex),
                cursor.getString(thumbnailIndex),
                cursor.getString(enclosureIndex)
                );
    }


}
