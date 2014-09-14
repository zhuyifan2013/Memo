package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import our.memo.R;

/**
 * Created by yifan on 14-9-3.
 */
public class EditNoteActivity extends Activity {

    private EditeNoteFragment mEditeNoteFragment;
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);

        mEditeNoteFragment = EditeNoteFragment.newInstance();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mEditeNoteFragment)
                    .commit();
        }

        mActionBar= getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

}
