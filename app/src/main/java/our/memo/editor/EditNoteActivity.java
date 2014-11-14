package our.memo.editor;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import our.memo.R;

public class EditNoteActivity extends Activity {

    private FinishListener mFinishListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);
    }

    @Override
    public void finish() {
        Log.i("hello", "ready finish");
        mFinishListener.beforeFinish();
        Log.i("hello", "after before finish");
        super.finish();
    }

    public void setFinishListener(FinishListener finishListener) {
        this.mFinishListener = finishListener;
    }

    public interface FinishListener{
        public void beforeFinish();
    }
}
