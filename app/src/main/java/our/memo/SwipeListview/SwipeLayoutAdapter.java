package our.memo.SwipeListview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashSet;
import java.util.Set;

public abstract class SwipeLayoutAdapter extends BaseAdapter {

    public static enum Mode {
        Single, Multiple
    }

    private Mode mode = Mode.Single;
    public final int INVALID_POSITION = -1;

    private int mOpenPosition = INVALID_POSITION;
    private Set<Integer> mOpenPositions = new HashSet<Integer>();
    private Set<SwipeLayout> mShownLayouts = new HashSet<SwipeLayout>();

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        SwipeLayout swipeLayout;
        int swipeResId = getSwipeLayoutResId(position);
        if (v == null) {
            v = generateView(position, parent);
            swipeLayout = (SwipeLayout) v.findViewById(swipeResId);
            if (swipeLayout != null) {
                mShownLayouts.add(swipeLayout);
            }
        }
        fillValues(position, v);
        return v;
    }

    public void setMode(Mode mode) {
        mOpenPositions.clear();
        mOpenPosition = INVALID_POSITION;
        this.mode = mode;
    }

    public abstract int getSwipeLayoutResId(int position);

    public abstract View generateView(int position, ViewGroup parent);

    public abstract void fillValues(int position, View convertView);

}
