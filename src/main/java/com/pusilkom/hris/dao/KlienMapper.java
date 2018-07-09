package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.DivisiModel;
import com.pusilkom.hris.model.KlienModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KlienMapper {


    @Select("SELECT * " +
            "\tFROM \"a07_client\"\n" +
            "\tWHERE id=#{idKlien};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="nama", column="name"),
            @Result(property="isDeleted", column="is_deleted")
    })
    KlienModel selectKlienByID(@Param("idKlien") Integer idKlien);


    @Select("SELECT * FROM \"a07_client\";")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="nama", column="name"),
            @Result(property="isDeleted", column="is_deleted")
    })
    List<KlienModel> selectAllKlien();



}
