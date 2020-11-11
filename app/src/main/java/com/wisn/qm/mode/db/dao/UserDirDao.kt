package com.wisn.qm.mode.db.dao

import androidx.room.*
import com.wisn.qm.mode.db.beans.UserDirBean

@Dao
interface UserDirDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDirBeanList(userdirbes: MutableList<UserDirBean>)

    @Query("DELETE FROM userdirlist")
    suspend fun deleteAllDirBeanList()


    @Query("select * from userdirlist")
    suspend fun getAllUserDirBeanList(): MutableList<UserDirBean>

}