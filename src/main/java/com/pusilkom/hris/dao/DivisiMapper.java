package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.DivisiModel;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DivisiMapper {

    @Select("SELECT id, kode, nama_divisi, id_manager, id_divisi_induk\n" +
            "\tFROM mpp.\"DIVISI\"\n" +
            "\tWHERE id_manager=#{id_manajer};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaDivisi", column="nama_divisi"),
            @Result(property="idManajer", column="id_manager"),
            @Result(property="idDivisiInduk", column="id_divisi_induk")
    })
    DivisiModel selectDivisiByManajer(@Param("id_manajer") int idManajer);


    @Select("SELECT id, kode, nama_divisi, id_manager, id_divisi_induk\n" +
            "\tFROM mpp.\"DIVISI\"\n" +
            "\tWHERE id=#{id_divisi};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaDivisi", column="nama_divisi"),
            @Result(property="idManajer", column="id_manager"),
            @Result(property="idDivisiInduk", column="id_divisi_induk")
    })
    DivisiModel selectDivisiByID(@Param("id_divisi") int idDivisi);

    @Delete("delete from mpp.\"DIVISI\" where id_manager = #{idManager}")
    void deleteManajerDivisi(@Param("idManager") int idManager);
}
