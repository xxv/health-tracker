package info.staticfree.healthtracker.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public class EventManager {
    /**
     * Inserts or updates, based on when the last entry was made.
     */
    public void insertOrUpdate(@NonNull final InsertOrUpdateListener listener,
            final ContentResolver resolver, final Uri uri, final ContentValues values,
            final long when, final long deDuplicateWindow) {
        final InsertOrUpdateHandler handler =
                new InsertOrUpdateHandler(Looper.getMainLooper(), listener);
        final WeakReference<InsertOrUpdateHandler> handlerRef = new WeakReference<>(handler);
        final Runnable worker = new Runnable() {
            @Override
            public void run() {
                final InsertOrUpdateResult result =
                        insertOrUpdateInternal(resolver, uri, values, when, deDuplicateWindow);
                final InsertOrUpdateHandler handlerInternal = handlerRef.get();

                if (handlerInternal != null) {
                    handlerInternal.obtainMessage(InsertOrUpdateHandler.MSG_RESULT, result)
                            .sendToTarget();
                }
            }
        };

        new Thread(worker).start();
    }

    /**
     * Inserts or updates, based on when the last entry was made.
     */
    private InsertOrUpdateResult insertOrUpdateInternal(final ContentResolver resolver,
            final Uri uri, final ContentValues values, final long when,
            final long deDuplicateWindow) {
        final Uri content;
        final boolean isInsert;

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
                    isInsert = false;
                } else {
                    content = resolver.insert(uri, values);
                    isInsert = true;
                }
            } finally {
                inWindow.close();
            }
        } else {
            throw new IllegalStateException("Received null cursor");
        }

        return new InsertOrUpdateResult(content, isInsert);
    }

    public interface InsertOrUpdateListener {
        void onInsertOrUpdate(@NonNull final InsertOrUpdateResult insertOrUpdateResult);
    }

    public static final class InsertOrUpdateResult {
        public final boolean mIsInsert;
        @Nullable
        public final Uri mUri;

        InsertOrUpdateResult(@Nullable final Uri uri, final boolean isInsert) {
            mUri = uri;
            mIsInsert = isInsert;
        }
    }

    public static class InsertOrUpdateHandler extends Handler {
        public static final int MSG_RESULT = 100;
        private final WeakReference<InsertOrUpdateListener> mListenerRef;

        public InsertOrUpdateHandler(@NonNull final Looper looper,
                @NonNull final InsertOrUpdateListener listener) {
            super(looper);
            mListenerRef = new WeakReference<>(listener);
        }

        @Override
        public void handleMessage(final Message msg) {
            if (MSG_RESULT == msg.what) {
                final InsertOrUpdateListener listener = mListenerRef.get();

                if (listener != null) {
                    listener.onInsertOrUpdate((InsertOrUpdateResult) msg.obj);
                }
            }
        }
    }
}
