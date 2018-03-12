package classes;

import interfaces.RetroFitService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by root on 08.03.18.
 */

public class RestClient {

    public static RetroFitService getRetrofitService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetroFitService.class);
    }
}
