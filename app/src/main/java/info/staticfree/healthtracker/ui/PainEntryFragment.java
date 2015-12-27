package info.staticfree.healthtracker.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import info.staticfree.healthtracker.R;
import info.staticfree.healthtracker.data.EventManager;
import info.staticfree.healthtracker.data.EventManager.InsertOrUpdateListener;
import info.staticfree.healthtracker.data.MeasurementEvent;

public class PainEntryFragment extends Fragment {
    private TextView mPainEntryValue;
    private final RatingBar.OnRatingBarChangeListener mRatingChange =
            new RatingBar.OnRatingBarChangeListener() {

                private final InsertOrUpdateListener mListener = new InsertOrUpdateListener() {
                    @Override
                    public void onInsertOrUpdate(
                            @NonNull final EventManager.InsertOrUpdateResult insertOrUpdateResult) {

                        final View parent = getView();

                        if (parent != null) {
                            if (insertOrUpdateResult.mIsInsert) {
                                Snackbar.make(parent, R.string.notice_new_entry_created,
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(parent, R.string.notice_last_entry_updated,
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                };

                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float rating,
                        final boolean fromUser) {
                    final int ratingInt = (int) Math.floor(rating);

                    mPainEntryValue.setText(String.format("%d", ratingInt));
                    mPainEntryDescription.setText(getActivity().getResources()
                            .getStringArray(R.array.pain_entry_descriptions)[ratingInt]);

                    if (fromUser) {
                        final ContentValues values = new ContentValues();
                        values.put(MeasurementEvent.VALUE, String.valueOf(rating));
                        MeasurementEvent.MANAGER
                                .insertOrUpdate(mListener, getActivity().getContentResolver(),
                                        MeasurementEvent.CONTENT_URI, values,
                                        System.currentTimeMillis(), TimeUnit.SECONDS.toMillis(30));
                    }
                }
            };
    private TextView mPainEntryLastEntry;
    private TextView mPainEntryDescription;

    public PainEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @return A new instance of fragment PainEntryFragment.
     */
    public static PainEntryFragment newInstance() {
        final PainEntryFragment fragment = new PainEntryFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pain_entry, container, false);
        ((RatingBar) view.findViewById(R.id.ratingBar)).setOnRatingBarChangeListener(mRatingChange);
        mPainEntryValue = (TextView) view.findViewById(R.id.pain_entry_value);
        mPainEntryLastEntry = (TextView) view.findViewById(R.id.pain_entry_last_entry);
        mPainEntryDescription = (TextView) view.findViewById(R.id.pain_entry_pain_description);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(0, null, mLastEntryLoaderCallbacks);
    }

    private final LoaderCallbacks<Cursor> mLastEntryLoaderCallbacks =
            new LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
                    return new CursorLoader(getActivity(), MeasurementEvent.CONTENT_URI, null,
                            MeasurementEvent.TYPE + " IS ?",
                            new String[] { MeasurementEvent.EVENT_TYPE_MEASUREMENT },
                            MeasurementEvent.EVENT_DATE + " DESC");
                }

                @Override
                public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
                    data.moveToFirst();
                    setLastEntry(data);
                }

                @Override
                public void onLoaderReset(final Loader<Cursor> loader) {

                }
            };

    public void setLastEntry(@NonNull final Cursor lastEntry) {
        if (lastEntry.getCount() > 0) {
            final long when =
                    lastEntry.getLong(lastEntry.getColumnIndexOrThrow(MeasurementEvent.EVENT_DATE));
            final CharSequence whenString = DateUtils
                    .getRelativeDateTimeString(getActivity(), when, DateUtils.MINUTE_IN_MILLIS,
                            3 * DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
            final String value =
                    lastEntry.getString(lastEntry.getColumnIndexOrThrow(MeasurementEvent.VALUE));
            mPainEntryLastEntry
                    .setText(getString(R.string.pain_entry_last_entry_template, whenString, value));
            mPainEntryLastEntry.setVisibility(View.VISIBLE);
        } else {
            mPainEntryLastEntry.setVisibility(View.GONE);
        }
    }
}
