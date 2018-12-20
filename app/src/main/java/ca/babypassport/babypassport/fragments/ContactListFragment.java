package ca.babypassport.babypassport.fragments;

import android.app.ActionBar;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import ca.babypassport.babypassport.contract.BabyPassportContract.Contact;
import ca.hajjar.babypassport.R;

public class ContactListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = ContactListFragment.class.getCanonicalName();
    private long babyId;
    private SimpleCursorAdapter mAdapter;

    private static final String[] CONTACT_SUMMARY_PROJECTION = {
            Contact._ID,
            Contact.COLUMN_NAME_CONTACT_NAME,
            Contact.COLUMN_NAME_TYPE
    };

    private static final String SELECT = Contact.COLUMN_NAME_BABY_ID + " = ?";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.setHasOptionsMenu(true);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.contact_item, null, new String[]{Contact.COLUMN_NAME_CONTACT_NAME, Contact.COLUMN_NAME_TYPE}, new int[]{R.id.contact_name, R.id.contact_type}, 0);

        setListAdapter(mAdapter);
        Bundle bundle = getActivity().getIntent().getExtras();
        getLoaderManager().initLoader(0, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Contact.CONTENT_URI;

        babyId = args.getLong("baby_id");
        return new CursorLoader(getActivity(), baseUri,
                CONTACT_SUMMARY_PROJECTION, SELECT, new String[]{Long.toString(babyId)},
                Contact.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.swapCursor(null);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.util.Log.v(TAG, "called options menu");
        inflater.inflate(R.menu.add_contact_actions, menu);
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.app_name);
        actionBar.setIcon(R.drawable.ic_launcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        android.util.Log.v(TAG, "item selected");
        switch (item.getItemId()) {
            case R.id.add_contact_action:
                EditContactFragment editContactFragment = new EditContactFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = getActivity().getIntent().getExtras();
                editContactFragment.setArguments(bundle);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack

                transaction.add(android.R.id.content, editContactFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ContactFragment newFragment = new ContactFragment();
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            android.util.Log.d(TAG, "contact_id " + id);
            bundle.putLong("contact_id", id);
            newFragment.setArguments(bundle);
        }
        newFragment.show(fragmentManager, "dialog");
    }


}
