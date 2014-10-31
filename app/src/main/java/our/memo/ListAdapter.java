package our.memo;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import our.memo.SwipeListview.SwipeLayout;
import our.memo.SwipeListview.SwipeLayoutAdapter;
import our.memo.data.NoteDatabase;
import our.memo.data.NoteItem;

/**
 * Created by yifan on 14-9-11.
 * E-mail: zhuyifan@xiaomi.com
 */
public class ListAdapter extends SwipeLayoutAdapter {

    private ArrayList<NoteItem> mNoteArray;
    private Context mContext;
    private SwipeLayout mSwipeLayout;

    public ListAdapter(ArrayList<NoteItem> noteArray, Context context) {
        this.mNoteArray = noteArray;
        mContext = context;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_item, null);
        mSwipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResId(position));
        return v;
    }

    @Override
    public int getCount() {
        return mNoteArray.size();
    }

    @Override
    public Object getItem(int position) {
        return mNoteArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResId(int position) {
        return R.id.swipe;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView contentView = (TextView) convertView.findViewById(R.id.item_content);
        TextView updateTimeView = (TextView) convertView.findViewById(R.id.item_update_time);
        //contentView.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/YueYuan.otf"));
        contentView.setText(mNoteArray.get(position).getContent());
        updateTimeView.setText(mNoteArray.get(position).getUpdateTime());
    }
}
