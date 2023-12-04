package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import java.util.List;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES")
    List<Note> getAll();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getById(int noteId);

    @Insert("INSERT INTO NOTES(notetitle,notedescription,userid) VALUES(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void add(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle},notedescription = #{noteDescription},userid= #{userId} WHERE noteid = #{noteId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteByID(int noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllByUserId(int userId);
}
