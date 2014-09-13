package our.memo.editor;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static our.memo.data.NoteDataContract.NoteEntry;

/**
 * Created by yifan on 14-9-3.
 * E-mail: zhuyifan@xiaomi.com
 */
public class EditeNoteFragment extends Fragment {
    private Context mContext;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("memo","fragment onCreate ");
        mContext = getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("memo","fragment onPause");
        EditText eText = (EditText)view.findViewById(R.id.edit_text_note);
        String content = eText.getText().toString();
        if(!"".equals(content))
            saveData(content);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("memo", "fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("memo","fragment onDestroyview");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("memo","fragment onDestroy");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.v("memo","fragment onCreateView");
        view = inflater.inflate(R.layout.edit_note_fragment,container,false);
        init(view);
        return view;

    }

    public void init(View view) {

        TextView text_date = (TextView)view.findViewById(R.id.current_date);
        text_date.setText(currentTime());
    }

    private void saveData(String content) {
        NoteDbHelper mDbHelper = new NoteDbHelper(mContext);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_CONTENT, content);
        values.put(NoteEntry.COLUMN_NAME_UPDATE_DATE,(new Date()).getTime());
        mDb.insert(NoteEntry.TABLE_NAME, null, values);
        mDb.close();
    }

    //get current time, string type
    private String currentTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
