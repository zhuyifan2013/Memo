package our.memo.SwipeListview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by yifan on 14-9-18.
 * Email: zhuyifan@xiaomi.com
 */
public class ListviewSwipeAdapter extends BaseAdapter{
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
        SwipeLayout swipeLayout;
        int resourceId = getResourceId(position);
        if(convertView == null){
            convertView = generateView(position, parent);
            swipeLayout = (SwipeLayout)convertView.findViewById(R.id.)
        }
    }

    private int getResourceId(int position) {
        return
    }

    private View generateView(int position, ViewGroup parent) {


    }
}
