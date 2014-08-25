package com.solstice.cdc.loaders.app;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.solstice.cdc.loaders.AutoRefreshLoader;
import com.solstice.cdc.loaders.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoRefreshLoaderFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<List<UUID>> {

    private static int NUM_ITEMS = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                NUM_ITEMS++;
                getLoaderManager().getLoader(0).onContentChanged();
                break;
            case R.id.action_refresh:
                NUM_ITEMS = 3;
                getLoaderManager().restartLoader(0, null, this);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public Loader<List<UUID>> onCreateLoader(int i, Bundle bundle) {
        return new AutoRefreshUuidLoader(getActivity(), 1000*5);
    }

    @Override
    public void onLoadFinished(Loader<List<UUID>> listLoader, List<UUID> uuids) {
        getListView().setAdapter(new ArrayAdapter<UUID>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                uuids
        ));
        setListShown(true);
    }

    @Override
    public void onLoaderReset(Loader<List<UUID>> listLoader) {
        // not needed yet
    }

    public static class AutoRefreshUuidLoader extends AutoRefreshLoader<List<UUID>> {

        public AutoRefreshUuidLoader(Context context, long interval) {
            super(context, interval);
        }

        @Override
        public List<UUID> loadInBackground() {
            List<UUID> list = new ArrayList<UUID>();
            for (int i = 0; i < NUM_ITEMS; i++) {
                list.add(UUID.randomUUID());
            }
            return list;
        }
    }
}
