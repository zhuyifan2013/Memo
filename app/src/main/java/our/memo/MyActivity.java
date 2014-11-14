package our.memo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import our.memo.editor.EditNoteActivity;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hello", "MyActivity OnCreate");
        setContentView(R.layout.homepage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("hello", "Myactivity intent = " + (data == null ? "null" : data.toString()));
    }
}
