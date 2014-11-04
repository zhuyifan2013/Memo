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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import our.memo.SwipeListview.SwipeLayoutAdapter;
import our.memo.data.NoteDatabase;
import our.memo.editor.EditNoteActivity;
import our.memo.data.NoteDbHelper;
import our.memo.data.NoteItem;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesListFragment extends Fragment {

    private Context mContext;

    private ListView mNoteList;

    private ArrayList<NoteItem> mNoteArray = new ArrayList<NoteItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list_fragment, container, false);
        mNoteList = (ListView) view.findViewById(R.id.notes_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
        ListAdapter mAdapter = new ListAdapter(mNoteArray, mContext);
        mAdapter.setMode(SwipeLayoutAdapter.Mode.Single);
        mNoteList.setAdapter(mAdapter);
        mNoteList.setOnItemClickListener(mOnItemClickListener);
        mAdapter.notifyDataSetChanged();
    }

    private void initListView() {
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(NoteDatabase.CONTENT_URI_NOTE,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    NoteItem noteItem = new NoteItem();
                    noteItem.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase
                            .NoteTable
                            .CONTENT)));

                    long time = cursor.getLong(cursor.getColumnIndex(NoteDatabase.NoteTable
                            .UPDATE_DATE));
                    Calendar cl = Calendar.getInstance();
                    cl.setTimeInMillis(time);
                    noteItem.setUpdateTime(Integer.toString(cl.get(Calendar.MONTH) + 1) + "/" +
                            Integer
                                    .toString(cl.get(Calendar.DAY_OF_MONTH)));

                    noteItem.setID(cursor.getInt(cursor.getColumnIndex(NoteDatabase.NoteTable
                            ._ID)));
                    mNoteArray.add(noteItem);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView
            .OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NoteItem noteItem = (NoteItem) parent.getItemAtPosition(position);
            LinearLayout deleteLayout = (LinearLayout) view.findViewById(R.id.bottom);
            deleteLayout.setOnClickListener(mDeleteListener);

            Intent intent = new Intent(mContext, EditNoteActivity.class);
            intent.putExtra(NoteDatabase.NoteTable._ID, noteItem.getID());
            startActivity(intent);
        }
    };

    View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "删除笔记", Toast.LENGTH_SHORT).show();
        }
    };
}
