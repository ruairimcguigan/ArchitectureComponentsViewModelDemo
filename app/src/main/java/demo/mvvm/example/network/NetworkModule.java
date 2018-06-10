package demo.mvvm.example.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

@Module
public abstract class NetworkModule {

    private static final String BASE_URL = "https://api.github.com/";
    private static RepoService repoService;

    @Provides
    @Singleton
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(provideClient())
                .build();
    }

    @Provides
    @Singleton
    static RepoService provideRepoService(Retrofit retrofit){
        return retrofit.create(RepoService.class);
    }

    @Provides
    @Singleton
    static OkHttpClient provideClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(provideInterceptor());
        return client.build();
    }

    @Provides
    @Singleton
    static HttpLoggingInterceptor provideInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BODY);
        return interceptor;
    }
}
