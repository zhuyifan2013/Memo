package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import our.memo.R;

/**
 * Created by yifan on 14-9-3.
 */
public class EditNoteActivity extends Activity {

    private EditeNoteFragment mEditeNoteFragment;
    private ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.v("test","调用了onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);

        mEditeNoteFragment = EditeNoteFragment.newInstance();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mEditeNoteFragment)
                    .commit();
        }

        mActionBar= getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        Log.v("test","调用了onStart");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.v("test","调用了onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.v("test","调用了onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("test","调用了onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.v("test","调用了onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v("test","调用了onDestroy");
        super.onDestroy();
    }
}
