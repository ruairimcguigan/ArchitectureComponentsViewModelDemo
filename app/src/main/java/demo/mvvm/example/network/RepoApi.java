package demo.mvvm.example.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class RepoApi {

    public static final String BASE_URL = "https://api.github.com/";

    private static Retrofit retrofit;
    private static RepoService repoService;


    private static OkHttpClient provideClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(provideInterceptor());
        return client.build();
    }

    private static HttpLoggingInterceptor provideInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BODY);
        return interceptor;
    }


    private static void initialiseRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(provideClient())
                .build();
    }
    public static RepoService getInstance(){
        if (repoService !=null){
            return repoService;
        }
        if (retrofit == null){
            initialiseRetrofit();
        }
        repoService = retrofit.create(RepoService.class);
        return repoService;
    }

    private RepoApi(){ }

}
