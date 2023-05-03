package com.sigarda.jurnalkas.data.local.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sigarda.jurnalkas.data.local.dao.TransactionDao
import com.sigarda.jurnalkas.data.local.entity.Event
import com.sigarda.jurnalkas.data.local.entity.TransactionEntity
import com.sigarda.jurnalkas.utils.Converters

@Database(
    entities = [TransactionEntity::class, Event::class],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao


    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    fun getDatabaseCreated(): LiveData<Boolean> {
        return mIsDatabaseCreated
    }
    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }



    }

