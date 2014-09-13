package our.memo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yifan on 14-9-3.
 */
public class NoteDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Memo.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String LONG_TYPE = " LONG";
    private static final String COMMA_SEP = " ,";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +
                    NoteDataContract.NoteEntry.TABLE_NAME + " (" +
                    NoteDataContract.NoteEntry._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    NoteDataContract.NoteEntry.COLUMN_NAME_UPDATE_DATE + LONG_TYPE + COMMA_SEP +
                    NoteDataContract.NoteEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + " )";

    public NoteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}