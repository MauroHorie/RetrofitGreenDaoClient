package com.mkhorie.greendaoretrofit;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mkhorie.greendaoretrofit.models.User;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;
    private UserListAdapter adapter;
    private Observable<List<User>> userSyncClient;
    private Subscriber<List<User>> userSyncSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setup the recycler view
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adapter = new UserListAdapter();
            recyclerView.setAdapter(adapter);
        }

        // This initializes the sync observable and subscriber.
        // This call does not bind the subscriber to the observer. That happens onResume().
        // Unbinding happens onPause().
        initSyncClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindToSyncClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindFromSyncClient();
    }

    /**
     * Here we are saving the RxContainer outside of the Activity lifecycle. Note that unbindFromSyncClient()
     * has been called onPause() prior to this method being called.
     * @return the RxContainer we wish to retain.
     */
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return userSyncClient;
    }

    private App getApp() {
        return (App) getApplicationContext();
    }

    //region Sync Code
    @SuppressWarnings("unchecked")
    private void initSyncClient() {
        userSyncClient = (Observable<List<User>>) getLastCustomNonConfigurationInstance();
        if (userSyncClient == null) {
            userSyncClient = getApp().getDataManager().syncUsers();
        }
        userSyncSubscriber = new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "Error fetching data from server", e);
                Snackbar.make(coordinatorLayout, "Error fetching data from server", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNext(List<User> users) {
                adapter.setData(users);
            }
        };
    }

    private void bindToSyncClient() {
        userSyncClient.subscribe(userSyncSubscriber);
    }

    private void unbindFromSyncClient() {
        userSyncSubscriber.unsubscribe();
    }
    //endregion
}
