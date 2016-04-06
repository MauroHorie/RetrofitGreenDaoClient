package com.mkhorie.greendaoretrofit;

import android.app.Application;

import com.google.gson.Gson;
import com.mkhorie.greendaoretrofit.data.DataManager;
import com.mkhorie.greendaoretrofit.data.greendao.DaoMaster;
import com.mkhorie.greendaoretrofit.data.greendao.DaoSession;
import com.mkhorie.greendaoretrofit.data.network.RestClient;

public class App extends Application {

    private DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        RestClient restClient = new RestClient(new Gson());
        DaoSession daoSession = new DaoMaster(this).newSession();
        dataManager = new DataManager(restClient, daoSession);
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
