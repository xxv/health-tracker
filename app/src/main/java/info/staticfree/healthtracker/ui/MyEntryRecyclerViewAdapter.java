package info.staticfree.healthtracker.ui;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.staticfree.healthtracker.R;
import info.staticfree.healthtracker.data.MeasurementEvent;

public class MyEntryRecyclerViewAdapter
        extends RecyclerView.Adapter<MyEntryRecyclerViewAdapter.ViewHolder> {

    @NonNull
    private final OnListFragmentInteractionListener mListener;
    @Nullable
    private Cursor mItems;

    public MyEntryRecyclerViewAdapter(@Nullable final Cursor items,
            @NonNull final OnListFragmentInteractionListener listener) {
        mListener = listener;
        mItems = items;
    }

    @Override
    public void onViewAttachedToWindow(final ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_entry, parent, false);
        return new ViewHolder(view);
    }

    public void setCursor(@Nullable final Cursor cursor) {
        mItems = cursor;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mItems == null) {
            return;
        }

        mItems.moveToPosition(position);

        holder.mItem = mItems;
        holder.mIdView.setText(String.valueOf(mItems.getInt(mItems.getColumnIndex(MeasurementEvent._ID))));
        holder.mContentView
                .setText(mItems.getString(mItems.getColumnIndex(MeasurementEvent.VALUE)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mItems.moveToPosition(position);
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.getCount() : 0;
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        public final View mView;
        @NonNull
        public final TextView mIdView;
        @NonNull
        public final TextView mContentView;
        @Nullable
        public Cursor mItem;

        public ViewHolder(@NonNull final View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
