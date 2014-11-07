package our.memo;

import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import our.memo.data.NoteDatabase;
import our.memo.data.NoteItem;
import our.memo.editor.EditNoteActivity;

public class NotesListFragment extends Fragment {

    private Context mContext;

    private ListView mNoteList;
    private ListAdapter mListAdapter;

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
        mListAdapter = new ListAdapter(mNoteArray, mContext);
        mListAdapter.setItemClickListener(mItemClickListener);
        mNoteList.setAdapter(mListAdapter);
        mListAdapter.notifyDataSetChanged();
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

    private ListAdapter.ItemClickListener mItemClickListener = new ListAdapter.ItemClickListener() {
        @Override
        public void itemListener(int position) {
            NoteItem noteItem = (NoteItem) mListAdapter.getItem(position);
            Intent intent = new Intent(mContext, EditNoteActivity.class);
            intent.putExtra(NoteDatabase.NoteTable._ID, noteItem.getID());
            startActivity(intent);
        }

        @Override
        public void deleteListener(int position) {
            NoteItem noteItem = (NoteItem) mListAdapter.getItem(position);
            Uri uri = ContentUris.withAppendedId(NoteDatabase.CONTENT_URI_NOTE, noteItem.getID());
            mContext.getContentResolver().delete(uri, null, null);
            mNoteArray.remove(position);
            mListAdapter.notifyDataSetChanged();
        }
    };
}
