package info.staticfree.healthtracker.data;

import edu.mit.mobile.android.content.ContentItem;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.DatetimeColumn;
import edu.mit.mobile.android.content.column.TextColumn;

public abstract class Event implements ContentItem {
    @DBColumn(type = DatetimeColumn.class, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
    public static final String CREATION_DATE = "created";

    @DBColumn(type = DatetimeColumn.class, defaultValue = DatetimeColumn.NOW_IN_MILLISECONDS)
    public static final String EVENT_DATE = "when";

    @DBColumn(type = TextColumn.class)
    public static final String TYPE = "type";

    @DBColumn(type = TextColumn.class)
    public static final String VALUE = "value";
}
