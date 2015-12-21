package info.staticfree.healthtracker.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.staticfree.healthtracker.R;
import info.staticfree.healthtracker.data.MeasurementEvent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EntryFragment extends Fragment {

    private static final String ARG_URI = "uri";
    private OnListFragmentInteractionListener mListener;
    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {
                @Override
                public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
                    return new CursorLoader(getContext(), mUri, null, null, null, null);
                }

                @Override
                public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
                    mAdapter.setCursor(data);
                }

                @Override
                public void onLoaderReset(final Loader<Cursor> loader) {

                }
            };

    private RecyclerView mRecyclerView;
    private MyEntryRecyclerViewAdapter mAdapter;
    private Uri mUri;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public EntryFragment() {
    }

    @NonNull
    public static EntryFragment newInstance(@NonNull final Uri contentUri) {
        final EntryFragment fragment = new EntryFragment();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_URI, contentUri);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            final Uri uri = getArguments().getParcelable(ARG_URI);

            if (uri != null) {
                mUri = uri;
            }
        }

        if (mUri == null) {
            mUri = MeasurementEvent.CONTENT_URI;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, new Bundle(), mLoaderCallbacks);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_entry_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) view;
            mAdapter = new MyEntryRecyclerViewAdapter(null, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
