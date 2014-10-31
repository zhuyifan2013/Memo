package our.memo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteDbProvider extends ContentProvider {

    private static final String AUTHORITY = "our.memo.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://our.memo.provider");

    private NoteDbHelper mNoteDbHelper;

    public static final int URI_NOTE = 0;
    public static final int URI_NOTE_ID = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "note", URI_NOTE);
        sUriMatcher.addURI(AUTHORITY, "note/#", URI_NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        mNoteDbHelper = new NoteDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mNoteDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)) {
            case URI_NOTE: {
                /**
                 * SELECT _id, content, update_time FROM note
                 */
                return db.query(NoteDatabase.TABLE_NOTE,
                        NoteDatabase.PROJECTION_CONTENT_AND_DATE, null, null, null, null,
                        sortOrder);
            }
            case URI_NOTE_ID: {
                /**
                 * SELECT _id, content, update_time FROM note
                 * WHERE _id = ?
                 */
                long noteId = ContentUris.parseId(uri);
                selection = NoteDatabase.NoteTable._ID + "=?";
                return db.query(NoteDatabase.TABLE_NOTE,
                        NoteDatabase.PROJECTION_CONTENT_AND_DATE, selection,
                        new String[]{String.valueOf(noteId)}, null,
                        null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (!values.containsKey(NoteDatabase.NoteTable.CONTENT) || !values.containsKey
                (NoteDatabase.NoteTable.UPDATE_DATE)) {
            return null;
        }

        SQLiteDatabase db = mNoteDbHelper.getWritableDatabase();
        long rowId;
        switch (sUriMatcher.match(uri)) {
            case URI_NOTE: {
                rowId = db.insert(NoteDatabase.TABLE_NOTE, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            }
            default:
                return null;
        }

        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mNoteDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case URI_NOTE_ID: {
                long noteId = ContentUris.parseId(uri);
                selection = NoteDatabase.NoteTable._ID + "=?";
                count = db.delete(NoteDatabase.TABLE_NOTE, selection,
                        new String[]{String.valueOf(noteId)});
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
            }
            default:
                return count;
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        SQLiteDatabase db = mNoteDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case URI_NOTE_ID: {
                long noteId = ContentUris.parseId(uri);
                count = db.update(NoteDatabase.TABLE_NOTE, values, NoteDatabase.NoteTable._ID +
                        "=?", new String[]{String.valueOf(noteId)});
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }
            default:
                return count;
        }
    }
}
