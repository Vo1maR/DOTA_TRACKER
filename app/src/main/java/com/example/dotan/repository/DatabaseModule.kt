package com.example.dotan.repository
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "OpenDotaApp" // Replace with your desired database name
        ).build()
    }

    @Provides
    fun provideFavoriteAccountDao(appDatabase: AppDatabase): FavoriteAccountDao {
        return appDatabase.favoriteAccountDao()
    }
}