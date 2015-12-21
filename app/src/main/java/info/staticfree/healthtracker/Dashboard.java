package info.staticfree.healthtracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;
import java.util.Locale;

import info.staticfree.healthtracker.data.MeasurementEvent;
import info.staticfree.healthtracker.ui.OnListFragmentInteractionListener;

public class Dashboard extends AppCompatActivity implements OnListFragmentInteractionListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ContentValues data = new ContentValues();
                data.put(MeasurementEvent.VALUE, "kitten");
                data.put(MeasurementEvent.VALUE_INT, 42);
                getContentResolver().insert(MeasurementEvent.CONTENT_URI, data);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(final Cursor item) {
        Snackbar.make(findViewById(R.id.content), String.format(Locale.US, "%s %s",
                        new Date(item.getLong(item.getColumnIndex(MeasurementEvent.CREATION_DATE))),
                        item.getString(item.getColumnIndex(MeasurementEvent.VALUE))),
                Snackbar.LENGTH_LONG).show();
    }
}
