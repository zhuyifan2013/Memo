package our.memo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Memo.db";

    public NoteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +
                NoteDatabase.TABLE_NOTE + " (" +
                NoteDatabase.NoteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteDatabase.NoteTable.UPDATE_DATE + " LONG, " +
                NoteDatabase.NoteTable.CONTENT + " TEXT " + " )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}