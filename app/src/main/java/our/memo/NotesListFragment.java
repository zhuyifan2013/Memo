package our.memo;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import our.memo.data.NoteAdapter;
import our.memo.data.NoteDbHelper;
import our.memo.data.NoteItem;

import java.util.ArrayList;
import java.util.Calendar;

import static our.memo.data.NoteDataContract.NoteEntry;

/**
 * Created by yifan on 14-9-3.
 * E-mail: zhuyifan@xiaomi.com
 */
public class NotesListFragment extends Fragment {

    private Cursor mCursor;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("memo", "onCreate");
        mContext = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("memo", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("memo", "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("memo", "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("memo", "onDestroyView");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v("memo", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("memo", "onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("memo", "onCreateView");
        View view = inflater.inflate(R.layout.notes_list_fragment,container,false);
        getDataFromDatabase();
        ListView mListView = (ListView)view.findViewById(R.id.notes_list);
        ArrayList<NoteItem> note_array = new ArrayList<NoteItem>();
        process_raw_data(note_array);
        NoteAdapter mAdapter = new NoteAdapter(note_array,mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteItem ni = (NoteItem)parent.getItemAtPosition(position);
                Toast.makeText(mContext,"这个item的内容为"+ni.getContent(),Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.notifyDataSetChanged();
        return view;
    }

    //处理从数据库中取得的原始数据，加工为在界面上展示的数据
    private void process_raw_data(ArrayList<NoteItem> note_array) {
        NoteItem ni;
        while(mCursor.moveToNext()){
            ni = new NoteItem();
            ni.setContent(mCursor.getString(mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_CONTENT)));

            long time = mCursor.getLong(mCursor.getColumnIndex(NoteEntry.COLUMN_NAME_UPDATE_DATE));
            Calendar cl = Calendar.getInstance();
            cl.setTimeInMillis(time);
            ni.setUpdate_time(Integer.toString(cl.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cl.get(Calendar.DAY_OF_MONTH)));

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
