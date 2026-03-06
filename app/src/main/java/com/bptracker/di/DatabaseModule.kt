package com.bptracker.di

import android.content.Context
import androidx.room.Room
import com.bptracker.data.local.BPDatabase
import com.bptracker.data.local.BPReadingDao
import com.bptracker.data.local.ReminderDao
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BPDatabase {
        return Room.databaseBuilder(
            context,
            BPDatabase::class.java,
            BPDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBPReadingDao(database: BPDatabase): BPReadingDao {
        return database.bpReadingDao()
    }

    @Provides
    @Singleton
    fun provideReminderDao(database: BPDatabase): ReminderDao {
        return database.reminderDao()
    }
}
