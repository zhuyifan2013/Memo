package our.memo.data;

import android.provider.BaseColumns;

/**
 * Created by yifan on 14-9-2.
 * Email: zhuyifan@xiaomi.com
 */
public final class NoteDataContract {
    public NoteDataContract(){}

    public static abstract class NoteEntry implements BaseColumns{
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_UPDATE_DATE = "update_time";
    }
}
