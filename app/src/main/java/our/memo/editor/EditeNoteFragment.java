package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Date;

import our.memo.R;
import our.memo.Util;
import our.memo.data.Constants;
import our.memo.data.NoteDatabase;
import our.memo.data.NoteItem;

public class EditeNoteFragment extends Fragment {

    private Activity mContext;
    private TextView mContentView;
    private TextView mDateView;

    private NoteItem mNote = new NoteItem();
    private NoteItem mReceiveNote = new NoteItem();

    private boolean mChanged = false;
    private boolean mNewNote = true;
    private int mPosition = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionbar();
        mContext = getActivity();
    }

    private void initActionbar() {
        setHasOptionsMenu(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor(getString(R.string.actionbar_color)));
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        }
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
        ((EditNoteActivity) getActivity()).setFinishListener(finishListener);
        Bundle bundle = mContext.getIntent().getExtras();
        if (bundle == null) {
            mNewNote = true;
            mDateView.setText(Util.getCurrentTime());
        } else {
            mNewNote = false;
            mPosition = bundle.getInt(NoteItem.NOTE_ITEM_POSITION);
            mReceiveNote = bundle.getParcelable(NoteItem.NOTE_ITEM_TAG);
            initView();
        }
    }

    private void initView() {
        mContentView.setText(mReceiveNote.getContent());
        mDateView.setText(Util.formatTime(Util.DATE_FORMAT_EDIT_PAGE,
                mReceiveNote.getUpdateTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveData() {
        ContentValues values = new ContentValues();

        String content = mContentView.getText().toString();
        if (mNewNote && content.isEmpty()) {
            return;
        }

        mNote.setContent(content);
        if (mNewNote) {
            mChanged = true;
            mNote.setUpdateTime((new Date()).getTime());
            values.put(NoteDatabase.NoteTable.CONTENT, mNote.getContent());
            values.put(NoteDatabase.NoteTable.UPDATE_DATE, mNote.getUpdateTime());
            Uri uri = mContext.getContentResolver().insert(NoteDatabase.CONTENT_URI_NOTE, values);
            mNote.setID((int) ContentUris.parseId(uri));
        } else {
            mNote.setID(mReceiveNote.getID());
            if (!NoteItem.equals(mNote, mReceiveNote)) {
                mChanged = true;
                mNote.setUpdateTime((new Date()).getTime());
                values.put(NoteDatabase.NoteTable.CONTENT, mNote.getContent());
                values.put(NoteDatabase.NoteTable.UPDATE_DATE, mNote.getUpdateTime());
                Uri uri = ContentUris.withAppendedId(NoteDatabase.CONTENT_URI_NOTE, mNote.getID());
                mContext.getContentResolver().delete(uri, null, null);
                uri = mContext.getContentResolver().insert(NoteDatabase.CONTENT_URI_NOTE, values);
                mNote.setID((int) ContentUris.parseId(uri));
            }
        }
    }

    EditNoteActivity.FinishListener finishListener = new EditNoteActivity.FinishListener() {
        @Override
        public void beforeFinish() {
            saveData();
            setResult();
        }
    };

    private void setResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (mChanged) {
            bundle.putParcelable(NoteItem.NOTE_ITEM_TAG, mNote);
            bundle.putInt(NoteItem.NOTE_ITEM_POSITION, mPosition);
            intent.putExtras(bundle);
            getActivity().setResult(Constants.DATA_CHANGED, intent);
        } else {
            getActivity().setResult(Constants.DATA_NOT_CHANGED, intent);
        }
    }
}