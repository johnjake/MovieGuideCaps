package ph.movieguide.com.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import ph.movieguide.com.BuildConfig
import ph.movieguide.com.BuildConfig.BASE_URL
import ph.movieguide.com.api.ApiServices
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

val networkModule = module {
    factory { providesOkHttpClient() }
    factory { providesGson() }
    factory { providesBaseUrl() }
    single { providesRetrofit(get(), get(), get()) }
    factory { provideInterceptors() }
    single { providesApiServices(get()) }
}

const val API = "api"
const val VERSION = 3
const val CATEGORY = "movie"

fun providesOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()
}

fun providesGson(): Gson {
    return GsonBuilder()
        .setLenient()
        .create()
}

fun providesBaseUrl(): String {
    return "$BASE_URL/$VERSION/"
}

fun providesBaseUrlImagePoster(): String {
    return BuildConfig.BASE_URL_POSTER
}

fun providesBaseUrlImageOriginal(): String {
    return BuildConfig.BASE_URL_ORIGINAL
}

fun providesRetrofit(@Named("BASE_URL") url: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()
}

fun provideInterceptors(): ArrayList<Interceptor> {
    val interceptors = arrayListOf<Interceptor>()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    interceptors.add(loggingInterceptor)
    return interceptors
}

fun providesApiServices(retrofit: Retrofit): ApiServices {
    return retrofit.create(ApiServices::class.java)
}
