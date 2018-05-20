package io.mii.coin.injection.module;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.mii.coin.BuildConfig;
import io.mii.coin.util.NetworkUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by evelyn on 10/5/18.
 */
@Module
public class NetworkModule {

    private final Context context;
    private final String baseUrl;

    public NetworkModule(final Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                     StethoInterceptor stethoInterceptor,
                                     ChuckInterceptor chuckInterceptor) {


        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 50 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // Network is not available
                if (!NetworkUtil.isNetworkConnected(context)) {
                    // Force using cache
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                // Network is available
                if (NetworkUtil.isNetworkConnected(context)) {
                    int maxAge = 60 * 5;
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("pragma")
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("pragma")
                            .build();
                }
                return response;
            }
        };

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
        .addInterceptor(cacheInterceptor)
        .cache(cache);

        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(chuckInterceptor);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
            httpClientBuilder.addNetworkInterceptor(stethoInterceptor);
        }
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    ChuckInterceptor provideChuckInterceptor() {
        return new ChuckInterceptor(context);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }
}
