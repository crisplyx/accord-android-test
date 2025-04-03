package id.rasyiid.accord_android_test.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideMyHttpClient(): MyHttpClient = MyHttpClient()

    @Provides
    fun provideHttpClient(httpClient: MyHttpClient): HttpClient = httpClient.getHttpClient()

}