package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES")
    List<FileModel> getAll();

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    FileModel get(int fileId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, filedata, userid) VALUES(#{fileName},#{contentType},#{fileSize},#{fileData},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void add(FileModel fileModel);

    @Update("UPDATE FILES SET filename = #{fileName},contenttype = #{contenType}, filesize = #{fileSize}, filedata = #{fileData}, userid = #{userId} WHERE fileId = #{fileId}")
    void update(FileModel fileModel);


    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteByID(int fileId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<FileModel> getAllByUserId(int userId);
}
