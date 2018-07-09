package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KaryawanProyekMapper {

    @Select("SELECT DISTINCT KP.id, id_karyawan, id_proyek, id_start_period, id_end_period, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"PERIODE\" AS P1, mpp.\"PERIODE\" AS P2, mpp.\"PERIODE\" AS P3\n" +
            "WHERE KP.id_proyek = #{idProyek}\n" +
            "\tAND P1.id = id_start_period\n" +
            "    AND (id_end_period IS NULL OR P2.id = id_end_period)\n" +
            "    AND P3.id = #{idPeriode}\n" +
            "    AND DATE(concat_ws('-', P1.bulan, 01, P1.tahun)) <= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "    AND DATE(concat_ws('-', P2.bulan, 01, P2.tahun)) >= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "ORDER BY id_start_period ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idStartPeriod", column="id_start_period"),
            @Result(property="idEndPeriod", column="id_end_period"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByProyekPeriode(@Param("idProyek") int idProyek, @Param("idPeriode") int idPeriode);

    @Select("SELECT DISTINCT KP.id, id_karyawan, id_proyek, id_start_period, id_end_period, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"PERIODE\" AS P1, mpp.\"PERIODE\" AS P2, mpp.\"PERIODE\" AS P3\n" +
            "WHERE P1.id = id_start_period\n" +
            "    AND (id_end_period IS NULL OR P2.id = id_end_period)\n" +
            "    AND P3.id = #{idPeriode}\n" +
            "    AND DATE(concat_ws('-', P1.bulan, 01, P1.tahun)) <= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "    AND DATE(concat_ws('-', P2.bulan, 01, P2.tahun)) >= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "ORDER BY id_start_period ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idStartPeriod", column="id_start_period"),
            @Result(property="idEndPeriod", column="id_end_period"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByPeriode(@Param("idPeriode") int idPeriode);
}
