package info.staticfree.healthtracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class EventManager {
    /**
     * Inserts or updates, based on when the last entry was made.
     */
    public void insertOrUpdate(final ContentResolver resolver, final Uri uri,
            final ContentValues values, final long when, final long deDuplicateWindow) {
        final Runnable worker = new Runnable() {
            @Override
            public void run() {
                insertOrUpdateInternal(resolver, uri, values, when, deDuplicateWindow);
            }
        };

        new Thread(worker).start();
    }

    /**
     * Inserts or updates, based on when the last entry was made.
     */
    private Uri insertOrUpdateInternal(final ContentResolver resolver, final Uri uri,
            final ContentValues values, final long when, final long deDuplicateWindow) {
        final Uri content;

        final Cursor inWindow = resolver.query(uri, new String[] { Event.EVENT_DATE, Event._ID },
                Event.EVENT_DATE + " >= (? - ?)", new String[] {
                        String.valueOf(when), String.valueOf(deDuplicateWindow)
                }, Event.EVENT_DATE + " DESC");

        if (inWindow != null) {
            try {
                final boolean hasExisting = inWindow.getCount() > 0;

                if (hasExisting) {
                    inWindow.moveToPosition(0);
                    final long existingId =
                            inWindow.getLong(inWindow.getColumnIndexOrThrow(Event._ID));
                    content = ContentUris.withAppendedId(uri, existingId);
                    resolver.update(content, values, null, null);
                } else {
                    content = resolver.insert(uri, values);
                }
            } finally {
                inWindow.close();
            }
        } else {
            throw new IllegalStateException("Received null cursor");
        }

        return content;
    }
}
