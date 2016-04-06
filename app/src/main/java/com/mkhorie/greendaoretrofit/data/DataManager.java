package com.mkhorie.greendaoretrofit.data;

import com.mkhorie.greendaoretrofit.data.greendao.DaoSession;
import com.mkhorie.greendaoretrofit.data.network.RestClient;
import com.mkhorie.greendaoretrofit.models.User;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DataManager {

    private final RestClient restClient;
    private final DaoSession daoSession;

    public DataManager(RestClient restClient, DaoSession daoSession) {
        this.restClient = restClient;
        this.daoSession = daoSession;
    }

    public Observable<List<User>> syncUsers() {
        return restClient
                .getUsers()
                .flatMap(new Func1<List<User>, Observable<List<User>>>() {
                    @Override
                    public Observable<List<User>> call(List<User> users) {
                        daoSession.getUserDao().insertOrReplaceInTx(users);
                        return Observable.just(users);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
