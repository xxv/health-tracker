package info.staticfree.healthtracker.data;

import android.net.Uri;

import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;
import edu.mit.mobile.android.content.column.TextColumn;

public class MeasurementEvent extends Event {

    @DBColumn(type = IntegerColumn.class)
    public static final String VALUE_INT = "value_int";

    @DBColumn(type = TextColumn.class)
    public static final String MEASUREMENT_TYPE = "measurement_type";

    public static final String PATH = "measurements";
    public static final Uri CONTENT_URI =
            ProviderUtils.toContentUri(HealthTrackerProvider.AUTHORITY, PATH);

    public static final EventManager MANAGER = new EventManager(Event.EVENT_TYPE_MEASUREMENT);
}
