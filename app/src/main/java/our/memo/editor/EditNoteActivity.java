package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import our.memo.R;

public class EditNoteActivity extends Activity {

    private EditeNoteFragment mEditeNoteFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);
        initActionbar();
        mEditeNoteFragment = EditeNoteFragment.newInstance();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mEditeNoteFragment)
                    .commit();
        }
    }

    private void initActionbar() {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#BDC0BA"));
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg));
        }
    }
}
