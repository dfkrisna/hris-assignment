package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.KaryawanProyekModel;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface KaryawanProyekMapper {

    @Select("SELECT DISTINCT KP.id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP\n" +
            "WHERE KP.id_proyek = #{idProyek}\n" +
            "\tAND start_periode <= '${periode}'\n" +
            "\tAND (end_periode >= '${periode}' OR end_periode IS NULL)\n" +
            "ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByProyekPeriode(@Param("idProyek") int idProyek, @Param("periode") LocalDate periode);

    @Select("SELECT DISTINCT KP.id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP\n" +
            "WHERE start_periode <= '${periode}'\n" +
            "\tAND (end_periode >= '${periode}' OR end_periode IS NULL)\n" +
            "ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByPeriode(@Param("periode") LocalDate periode);

    @Select("SELECT id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\"\n" +
            "WHERE id_karyawan = ${idKaryawan}" +
            "ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByKaryawan(@Param("idKaryawan") int idKaryawan);

    @Select("SELECT id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\"\n" +
            "WHERE id_karyawan = ${idKaryawan}" +
            "AND id_proyek = ${idProyek}")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    KaryawanProyekModel selectKaryawanProyekByKaryawanandProyek(@Param("idKaryawan") int idKaryawan, @Param("idProyek") int idProyek);

    @Insert("INSERT INTO mpp.\"KARYAWAN_PROYEK\"(\n" +
            "\tid_karyawan, id_proyek, timestamp_inisiasi, id_role, start_periode, end_periode)\n" +
            "\tVALUES (#{idKaryawan}, #{idProyek}, to_timestamp(#{timestampInisiasi}, 'DD/MM/YYYY HH24:MI'), #{idRole}, #{startPeriode}, #{endPeriode});")
    void insertKaryawanProyek(KaryawanProyekModel karyawanProyekModel);

    @Select("SELECT id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\"\n" +
            "WHERE id = ${idKaryawanProyek};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    KaryawanProyekModel selectKaryawanProyekById(@Param("idKaryawanProyek") Integer idKaryawanProyek);

    @Update("UPDATE mpp.\"KARYAWAN_PROYEK\"\n" +
            "\tSET end_periode=#{endPeriode}\n" +
            "\tWHERE id=#{id};")
    void updateKaryawanProyek(KaryawanProyekModel karyawanProyek);

    @Select("SELECT id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\"\n" +
            "WHERE id_proyek = ${idProyek}" +
            "ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByProyek(@Param("idProyek") Integer idProyek);

    @Select("SELECT KP.id, id_karyawan, id_proyek, start_periode, end_periode, timestamp_inisiasi, id_role\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP\n" +
            "WHERE start_periode <= '${periode}'\n" +
            "\tAND (end_periode >= '${periode}' OR end_periode IS NULL)\n" +
            " AND KP.id_karyawan = ${idKaryawan}" +
            "ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="timestampInisiasi", column="timestamp_inisiasi"),
            @Result(property="idRole", column="id_role")
    })
    List<KaryawanProyekModel> selectKaryawanProyekByKaryawanPeriode(@Param("idKaryawan") Integer idKaryawan,
                                                                    @Param("periode") LocalDate periode);
}
