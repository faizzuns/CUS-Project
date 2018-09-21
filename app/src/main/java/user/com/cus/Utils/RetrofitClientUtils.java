package user.com.cus.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by User on 09/11/2017.
 * @author Ahmad Faiz Sahupala
 *
 * pemanggilan retrofit
 */

public class RetrofitClientUtils {
    public static final String BASE_URL = "http://denail.ml/";
    static Retrofit retrofit = null;



    public static Retrofit client() {
        Gson gson = new GsonBuilder().
                setLenient().create();
        HttpLoggingInterceptor logging =
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(logging);

        retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                client(okHttpClient.build()).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        return retrofit;
    }
}
