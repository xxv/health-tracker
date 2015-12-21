package info.staticfree.healthtracker.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import java.util.concurrent.TimeUnit;

import info.staticfree.healthtracker.R;
import info.staticfree.healthtracker.data.MeasurementEvent;

public class PainEntryFragment extends Fragment {
    private final RatingBar.OnRatingBarChangeListener mRatingChange =
            new RatingBar.OnRatingBarChangeListener() {

                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float rating,
                        final boolean fromUser) {
                    if (fromUser) {
                        final ContentValues values = new ContentValues();
                        values.put(MeasurementEvent.VALUE, String.valueOf(rating));
                        MeasurementEvent.MANAGER.insertOrUpdate(getActivity().getContentResolver(),
                                MeasurementEvent.CONTENT_URI, values, System.currentTimeMillis(),
                                TimeUnit.SECONDS.toMillis(30));
                    }
                }
            };

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

        return view;
    }
}
