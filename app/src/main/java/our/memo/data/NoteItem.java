package our.memo.data;

public class NoteItem {
    private int _ID; //此条笔记的唯一标识符
    private String content; //笔记的内容
    private String update_time; //笔记更新的时间

    public NoteItem(){
        content = null;
        update_time = null;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
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
}
