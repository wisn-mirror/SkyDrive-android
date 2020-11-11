package com.wisn.qm.mode.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.library.base.BaseApp
import com.wisn.qm.mode.db.test.User
import com.wisn.qm.mode.db.test.UserDao
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.db.dao.MediaInfoDao
import com.wisn.qm.mode.db.beans.UploadBean
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.mode.db.dao.UploadBeanDao
import com.wisn.qm.mode.db.dao.UserDirDao

@Database(entities = [User::class, UploadBean::class, MediaInfo::class, UserDirBean::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract val userDao: UserDao?
    abstract val mediaInfoDao: MediaInfoDao?
    abstract val uploadBeanDao: UploadBeanDao?
    abstract val userDirDao: UserDirDao?

    companion object {
        fun getInstanse() = SingletonHolder.wdata
    }

    private object SingletonHolder {
        val wdata: AppDataBase by lazy {
            Room.databaseBuilder(BaseApp.app, AppDataBase::class.java, "wdata")
//            .allowMainThreadQueries()
                    .build()
        }
    }



}