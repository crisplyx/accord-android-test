package id.rasyiid.accord_android_test.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import id.rasyiid.accord_android_test.domain.auth.AuthRepository
import id.rasyiid.accord_android_test.domain.home.HomeRepository

@Module(includes = [NetworkModule::class])
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
}