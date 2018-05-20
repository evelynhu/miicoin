package io.mii.coin.injection.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.mii.coin.data.remote.CoinService;
import retrofit2.Retrofit;

/**
 * Created by evelyn on 10/5/18.
 */
@Module(includes = {NetworkModule.class})
public class ApiModule {

    @Provides
    @Singleton
    CoinService provideCryptoApi(Retrofit retrofit) {
        return retrofit.create(CoinService.class);
    }
}
