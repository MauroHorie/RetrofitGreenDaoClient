package com.mkhorie.greendaoretrofit.data.network;

import com.google.gson.Gson;
import com.mkhorie.greendaoretrofit.models.User;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

public class RestClient {

    private static final String ENVIRONMENT = "http://jsonplaceholder.typicode.com";

    /**
     * Define the REST endpoints here
     */
    public interface ApiInterface {
        @GET("users")
        Observable<List<User>> getUsers();

        //@GET("users/{username}")
        //Call<User> getUser(@Path("username") String username);

        //@GET("group/{id}/users")
        //Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

        //@POST("users/new")
        //Call<User> createUser(@Body User user);
    }

    private ApiInterface restClient;

    public RestClient(Gson gson) {
        restClient = new Retrofit.Builder()
                .baseUrl(ENVIRONMENT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(ApiInterface.class);
    }

    public Observable<List<User>> getUsers() {
        return restClient.getUsers();
    }
}
