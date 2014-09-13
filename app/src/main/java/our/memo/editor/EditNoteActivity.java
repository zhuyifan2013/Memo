package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import our.memo.R;

/**
 * Created by yifan on 14-9-3.
 */
public class EditNoteActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.edit_notes);
    }

}
