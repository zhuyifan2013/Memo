package our.memo.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteDatabase {

    public static final String TABLE_NOTE = "note";
    public static final String[] PROJECTION_CONTENT_AND_DATE = new String[]{
            NoteTable._ID, NoteTable.CONTENT, NoteTable.UPDATE_DATE
    };
    public static final Uri CONTENT_URI_NOTE = Uri.parse(NoteDbProvider.CONTENT_URI + "/note");

    public static abstract class NoteTable implements BaseColumns {

        public static final String CONTENT = "content";
        public static final String UPDATE_DATE = "update_time";
    }
}
