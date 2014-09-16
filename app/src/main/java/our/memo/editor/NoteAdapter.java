package our.memo.editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import our.memo.R;
import our.memo.data.NoteItem;

import java.util.ArrayList;

/**
 * Created by yifan on 14-9-11.
 * E-mail: zhuyifan@xiaomi.com
 */
public class NoteAdapter extends BaseAdapter {

    private ArrayList<NoteItem> note_array;
    private LayoutInflater inflater;

    public NoteAdapter(ArrayList<NoteItem> note_array,Context context){
        this.note_array = note_array;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return note_array.size();
    }

    @Override
    public Object getItem(int position) {
        return note_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.note_item,null);
            holder.content_view = (TextView)convertView.findViewById(R.id.item_content);
            holder.update_time_view = (TextView)convertView.findViewById(R.id.item_update_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.content_view.setText(note_array.get(position).getContent());
        holder.update_time_view.setText(note_array.get(position).getUpdate_time());
        return convertView;
    }

    public static class ViewHolder{
        public TextView content_view;
        public TextView update_time_view;
    }
}
