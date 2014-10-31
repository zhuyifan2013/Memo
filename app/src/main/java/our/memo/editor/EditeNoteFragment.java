package our.memo.editor;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import our.memo.R;
import our.memo.data.NoteDatabase;
import our.memo.data.NoteDbHelper;
import our.memo.data.NoteItem;

public class EditeNoteFragment extends Fragment {

    private Activity mContext;

    private TextView mContentView;
    private TextView mDateView;

    private NoteItem mNote;

    public static EditeNoteFragment newInstance() {
        return new EditeNoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_note_fragment, container, false);
        mContentView = (TextView) view.findViewById(R.id.content);
        mDateView = (TextView) view.findViewById(R.id.date);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle == null) {
            mDateView.setText(currentTime());
        } else {
            QueryNoteTask queryNoteTask = new QueryNoteTask();
            int id = bundle.getInt(NoteDatabase.NoteTable._ID);
            queryNoteTask.execute(id);
        }
    }

    @Override
    public void onPause() {
        String content = mContentView.getText().toString();
        if (!"".equals(content)) {
            saveData(content);
        }
        super.onPause();
    }

    private class QueryNoteTask extends AsyncTask<Integer, Void, NoteItem> {

        @Override
        protected NoteItem doInBackground(Integer... params) {
            NoteItem noteItem = new NoteItem();
            Uri uri = ContentUris.withAppendedId(NoteDatabase.CONTENT_URI_NOTE, params[0]);
            long time = 0;
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(uri, null, null, null, null,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    noteItem.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase
                            .NoteTable
                            .CONTENT)));
                    time = cursor.getLong((cursor.getColumnIndex(NoteDatabase.NoteTable
                            .UPDATE_DATE)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(time);
            noteItem.setUpdateTime(Integer.toString(cl.get(Calendar.YEAR)) + "年" + Integer
                    .toString(cl
                            .get(Calendar.MONTH) + 1) + "月" + Integer.toString(cl.get(Calendar
                    .DAY_OF_MONTH))
                    + "日" + " "
                    + Integer.toString(cl.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cl.get
                    (Calendar.MINUTE)) + ":" + Integer.toString(cl.get(Calendar.SECOND)));
            return noteItem;
        }

        @Override
        protected void onPostExecute(NoteItem noteItem) {
            mNote = noteItem;
            mContentView.setText(noteItem.getContent());
            mDateView.setText(noteItem.getUpdateTime());
        }
    }

    private void saveData(String content) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabase.NoteTable.CONTENT, content);
        values.put(NoteDatabase.NoteTable.UPDATE_DATE, (new Date()).getTime());
        if (mNote == null) {
            mContext.getContentResolver().insert(NoteDatabase.CONTENT_URI_NOTE, values);
        } else {
            Uri uri = ContentUris.withAppendedId(NoteDatabase.CONTENT_URI_NOTE, mNote.getID());
            mContext.getContentResolver().update(uri, values, null, null);
        }
    }


    //get current time, string type
    private String currentTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
