package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KaryawanMapper {

    @Select("SELECT DISTINCT K.id, id_pengguna, id_divisi, P.nama\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE K.id_pengguna = P.id;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    List<KaryawanModel> selectKaryawanAll();

    @Select("SELECT DISTINCT K.id, id_pengguna, id_divisi, P.nama\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE K.id = #{idKaryawan}" +
            "\tAND id_pengguna = P.id;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    KaryawanModel selectKaryawanById(@Param("idKaryawan") int idKaryawan);

    @Select("SELECT DISTINCT K.id, id_pengguna, id_divisi, P.nama\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE K.id_pengguna = P.id  \n" +
            "\tAND K.id IN \n" +
            "\t\t(SELECT id_karyawan\n" +
            "\t\tFROM mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"PERIODE\" AS P1, mpp.\"PERIODE\" AS P2, mpp.\"PERIODE\" AS P3\n" +
            "\t\tWHERE P1.id = id_start_period\n" +
            "\t\tAND (id_end_period IS NULL OR P2.id = id_end_period)\n" +
            "\t\tAND P3.id = #{idPeriode}\n" +
            "\t\tAND DATE(concat_ws('-', P1.bulan, 01, P1.tahun)) <= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "\t\tAND DATE(concat_ws('-', P2.bulan, 01, P2.tahun)) >= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "\t\tORDER BY id_start_period ASC);")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    List<KaryawanModel> selectKaryawanByPeriode(@Param("idPeriode") int idPeriode);
}
