package our.memo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import our.memo.SwipeListview.SwipeLayoutAdapter;
import our.memo.editor.EditNoteActivity;
import our.memo.data.NoteDbHelper;
import our.memo.data.NoteItem;

import java.util.ArrayList;
import java.util.Calendar;

import static our.memo.data.NoteDataContract.NoteEntry;

public class NotesListFragment extends Fragment {

    private Cursor mCursor;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list_fragment, container, false);
        getDataFromDatabase();
        ListView mListView = (ListView) view.findViewById(R.id.notes_list);
        ArrayList<NoteItem> note_array = new ArrayList<NoteItem>();
        process_raw_data(note_array);
        ListAdapter mAdapter = new ListAdapter(note_array, mContext);
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(SwipeLayoutAdapter.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteItem ni = (NoteItem) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, EditNoteActivity.class);
                intent.putExtra(NoteEntry._ID, ni.get_ID());
                startActivity(intent);
            }
        });
        mAdapter.notifyDataSetChanged();
        return view;
    }

    //处理从数据库中取得的原始数据，加工为在界面上展示的数据
    private void process_raw_data(ArrayList<NoteItem> note_array) {
        NoteItem ni;
        while (mCursor.moveToNext()) {
            ni = new NoteItem();
            ni.setContent(mCursor.getString(mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_CONTENT)));

            long time = mCursor.getLong(mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_UPDATE_DATE));
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(time);
            ni.setUpdate_time(Integer.toString(cl.get(Calendar.MONTH) + 1) + "/" + Integer
                    .toString(cl.get(Calendar.DAY_OF_MONTH)));

            ni.set_ID(mCursor.getString(mCursor.getColumnIndex(NoteEntry._ID)));
            note_array.add(ni);
        }
    }

    //从数据库中获取数据，并且返回cursor
    private void getDataFromDatabase() {
        NoteDbHelper mDbHelper = new NoteDbHelper(mContext);

        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();

        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_NAME_CONTENT,
                NoteEntry.COLUMN_NAME_UPDATE_DATE
        };
        mCursor = mDb.query(
                NoteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }
}
