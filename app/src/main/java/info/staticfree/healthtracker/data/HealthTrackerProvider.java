package info.staticfree.healthtracker.data;

import edu.mit.mobile.android.content.GenericDBHelper;
import edu.mit.mobile.android.content.SimpleContentProvider;
import info.staticfree.healthtracker.BuildConfig;

public class HealthTrackerProvider extends SimpleContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final int DB_VERSION = 1;

    public HealthTrackerProvider() {
        super(AUTHORITY, DB_VERSION);

        final GenericDBHelper dbHelper =
                new GenericDBHelper(MeasurementEvent.class);

        addDirAndItemUri(dbHelper, MeasurementEvent.PATH);
    }
}
