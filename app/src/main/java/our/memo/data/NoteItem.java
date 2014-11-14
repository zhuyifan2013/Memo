package our.memo.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteItem implements Parcelable {
    public final static String NOTE_ITEM_TAG = "noteitem";
    public final static String NOTE_ITEM_POSITION = "position";

    private int _ID; //此条笔记的唯一标识符
    private String content; //笔记的内容
    private long update_time; //笔记更新的时间

    public NoteItem() {}

    private NoteItem(Parcel source) {
        _ID = source.readInt();
        content = source.readString();
        update_time = source.readLong();
    }

    public long getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(long update_time) {
        this.update_time = update_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getID() {
        return _ID;
    }

    public void setID(int _ID) {
        this._ID = _ID;
    }

    public static boolean equals(NoteItem a, NoteItem b) {
        return a._ID == b._ID && a.content.equals(b.content);
    }

    public static final Creator<NoteItem> CREATOR = new Creator<NoteItem>() {

        @Override
        public NoteItem createFromParcel(Parcel source) {
            return new NoteItem(source);
        }

        @Override
        public NoteItem[] newArray(int size) {
            return new NoteItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_ID);
        dest.writeString(content);
        dest.writeLong(update_time);
    }
}
