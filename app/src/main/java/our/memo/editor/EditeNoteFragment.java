package our.memo.editor;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import our.memo.R;
import our.memo.data.NoteDbHelper;
import our.memo.data.NoteItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static our.memo.data.NoteDataContract.NoteEntry;

/**
 * Created by yifan on 14-9-3.
 * E-mail: zhuyifan@xiaomi.com
 */
public class EditeNoteFragment extends Fragment {
    private Activity mContext;
    private View view;
    private NoteItem note = null;

    public static EditeNoteFragment newInstance() {
        return new EditeNoteFragment();
    }

    public EditeNoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("memo", "fragment onCreate ");
        mContext = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("memo", "fragment onPause");
        EditText eText = (EditText) view.findViewById(R.id.edit_text_note);
        String content = eText.getText().toString();
        if (!"".equals(content))
            saveData(content);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("memo", "fragment onCreateView");
        view = inflater.inflate(R.layout.edit_note_fragment, container, false);
        init(view);
        return view;

    }

    public void init(View view) {
        Bundle bundle = mContext.getIntent().getExtras();
        TextView text_date = (TextView) view.findViewById(R.id.current_date);
        TextView text_content = (TextView) view.findViewById(R.id.edit_text_note);
        if (bundle == null) {
            text_date.setText(currentTime());
        } else {
            String id = bundle.getString(NoteEntry._ID);
            NoteItem note = getData(id);
            text_date.setText(note.getUpdate_time());
            text_content.setText(note.getContent());
        }
    }

    private NoteItem getData(String id) {
        Cursor mCursor;
        NoteItem note = new NoteItem();
        NoteDbHelper mDbHelper = new NoteDbHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_NAME_CONTENT,
                NoteEntry.COLUMN_NAME_UPDATE_DATE
        };
        String selection = NoteEntry._ID + "=?";
        String[] selectionArgs = {id};
        mCursor = mDb.query(
                NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        mCursor.moveToFirst();
        note.set_ID(id);
        note.setContent(mCursor.getString(mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_CONTENT)));
        long time = (mCursor.getLong((mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_UPDATE_DATE))));
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(time);
        note.setUpdate_time(Integer.toString(cl.get(Calendar.YEAR)) + "年" + Integer.toString(cl.get(Calendar.MONTH) + 1) + "月" + Integer.toString(cl.get(Calendar.DAY_OF_MONTH)) + "日" + " "
                + Integer.toString(cl.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cl.get(Calendar.MINUTE)) + ":" + Integer.toString(cl.get(Calendar.SECOND)));
        this.note = note;
        return note;
    }

    private void saveData(String content) {
        NoteDbHelper mDbHelper = new NoteDbHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_CONTENT, content);
        values.put(NoteEntry.COLUMN_NAME_UPDATE_DATE, (new Date()).getTime());
        if (note == null) {
            mDb.insert(NoteEntry.TABLE_NAME, null, values);
        } else {
            String whereClause = NoteEntry._ID + "=?";
            String[] whereArgs = {note.get_ID()};
            mDb.update(NoteEntry.TABLE_NAME, values, whereClause, whereArgs);
        }
        mDb.close();
    }


    //get current time, string type
    private String currentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
