package com.wisn.qm.mode.db.test;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    public void insertUser(User... users);

    @Update
    public void updateUser(User... users);

    @Query("update user set e_mail =:email where uid=:id ")
    public void updateUser(int id,String email);

    @Delete
    public void deleteUser(User... users);

    @Query("delete from user where user_name=:username")
    public void deleteUser(String username);

    @Query("select * from user")
    public User[] getAllUser();

    @Query("select * from user where user_name like :username limit 3")
    public User[] getUserByUsername(String username);

    @Query("select * from user where uid =:uid")
    public User getUserByUId(int  uid);
}
