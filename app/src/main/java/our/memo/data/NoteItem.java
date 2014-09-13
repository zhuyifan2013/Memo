package our.memo.data;


/**
 * Created by yifan on 14-9-11.
 * E-mail: zhuyifan@xiaomi.com
 */
public class NoteItem {
    private String _ID; //此条笔记的唯一标识符
    private String content; //笔记的内容
    private String update_time; //笔记更新的时间

    public NoteItem(){
        content = null;
        update_time = null;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }
}
