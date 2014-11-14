package our.memo;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import our.memo.data.Constants;
import our.memo.data.NoteDatabase;
import our.memo.data.NoteItem;
import our.memo.editor.EditNoteActivity;

public class NotesListFragment extends Fragment {

    private Context mContext;

    private ListView mNoteList;
    private ListAdapter mListAdapter;
    //request code
    private final static int ITEM_NOTE = 1;
    private final static int ADD_NOTE = 2;

    private ArrayList<NoteItem> mNoteArray = new ArrayList<NoteItem>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        mContext = getActivity();
    }

    private void initActionBar() {
        setHasOptionsMenu(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor(getString(R.string.actionbar_color)));
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), EditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    noteItem.setUpdateTime(time);

                    noteItem.setID(cursor.getInt(cursor.getColumnIndex(NoteDatabase.NoteTable
                            ._ID)));
                    mNoteArray.add(0, noteItem);
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
            Bundle bundle = new Bundle();
            bundle.putParcelable(NoteItem.NOTE_ITEM_TAG, noteItem);
            bundle.putInt(NoteItem.NOTE_ITEM_POSITION, position);
            intent.putExtras(bundle);
            startActivityForResult(intent, ITEM_NOTE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        switch (requestCode) {
            case ADD_NOTE: {
                if (resultCode == Constants.DATA_CHANGED) {
                    NoteItem noteItem = bundle.getParcelable(NoteItem.NOTE_ITEM_TAG);
                    mNoteArray.add(0, noteItem);
                    mListAdapter.notifyDataSetChanged();
                }
                break;
            }
            case ITEM_NOTE: {
                if (resultCode == Constants.DATA_CHANGED) {
                    NoteItem noteItem = bundle.getParcelable(NoteItem.NOTE_ITEM_TAG);
                    int position = bundle.getInt(NoteItem.NOTE_ITEM_POSITION);
                    mNoteArray.remove(position);
                    mNoteArray.add(0, noteItem);
                    mListAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }
}
