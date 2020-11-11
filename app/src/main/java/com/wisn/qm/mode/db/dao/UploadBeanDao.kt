package com.wisn.qm.mode.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wisn.qm.mode.db.beans.UploadBean

@Dao
interface UploadBeanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUploadBeanList(uploadBeanlist: List<UploadBean>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUploadBean(uploadBean: UploadBean)

    @Query("select * from uploadbean where  pid= :pid")
    suspend fun getUploadBeanList(pid: Long): List<UploadBean>

    @Query("select * from uploadbean where uploadStatus =:uploadStatus")
    suspend fun getUploadBeanListPreUpload(uploadStatus: Int): List<UploadBean>

    @Query("update uploadbean set uploadStatus =:uploadStatus,uploadSuccessTime=:uploadSuccessTime where id=:id ")
    suspend fun updateUploadBeanStatus(id: Long, uploadStatus: Int,uploadSuccessTime: Long)

    @Query("select * from uploadbean order by id desc")
    suspend fun getUploadBeanListAll(): MutableList<UploadBean>


}