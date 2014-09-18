package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import our.memo.R;

public class EditNoteActivity extends Activity {

    private EditeNoteFragment mEditeNoteFragment;
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v("test", "调用了onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);

        mEditeNoteFragment = EditeNoteFragment.newInstance();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mEditeNoteFragment)
                    .commit();
        }

        mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
