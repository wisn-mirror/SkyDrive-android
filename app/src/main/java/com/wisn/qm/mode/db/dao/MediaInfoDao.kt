package com.wisn.qm.mode.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo

@Dao
interface MediaInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaInfo(mediaInfos: List<MediaInfo>)

    @Query("select * from mediainfo where  pid= :pid")
    suspend fun getMediaInfoList(pid: Long): MutableList<MediaInfo>

    @Query("select * from mediainfo")
    suspend fun getMediaInfoListAll(): MutableList<MediaInfo>

    @Query("select * from mediainfo  where uploadStatus !=${FileType.MediainfoStatus_Deleted} ")
    suspend fun getMediaInfoListAllNotDelete(): MutableList<MediaInfo>

    @Query("select * from mediainfo where uploadStatus =:uploadStatus")
    suspend fun getMediaInfoListPreUpload(uploadStatus: Int): MutableList<MediaInfo>

    @Query("update mediainfo set uploadStatus =:uploadStatus where id=:id")
    suspend fun updateMediaInfoStatusById(id: Long, uploadStatus: Int)

    @Query("update mediainfo set uploadStatus =:uploadStatus where sha1=:sha1")
    suspend fun updateMediaInfoStatusBySha1(sha1: String, uploadStatus: Int)

    @Query("select MAX(id) from mediainfo")
    suspend fun getMediaInfoMaxId():Long

}