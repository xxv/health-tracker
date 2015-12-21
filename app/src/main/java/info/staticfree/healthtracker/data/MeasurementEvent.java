package info.staticfree.healthtracker.data;

import android.net.Uri;

import edu.mit.mobile.android.content.ProviderUtils;
import edu.mit.mobile.android.content.column.DBColumn;
import edu.mit.mobile.android.content.column.IntegerColumn;

public class MeasurementEvent extends Event {

    @DBColumn(type = IntegerColumn.class)
    public static final String VALUE_INT = "value_int";

    public static final String PATH = "measurements";
    public static final Uri CONTENT_URI =
            ProviderUtils.toContentUri(HealthTrackerProvider.AUTHORITY, PATH);
}
