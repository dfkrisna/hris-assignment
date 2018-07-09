package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.ProyekModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProyekMapper {

    @Select("SELECT DISTINCT PR.id, kode, nama_proyek, nama_klien, keterangan, id_pmo, id_parent_project, id_project_lead, id_start_period, id_end_period\n" +
            "FROM mpp.\"PROYEK\" AS PR, mpp.\"PERIODE\" as P1, mpp.\"PERIODE\" AS P2, mpp.\"PERIODE\" AS P3\n" +
            "WHERE P1.id = id_start_period\n" +
            "\tAND (id_end_period IS NULL OR P2.id = id_end_period)\n" +
            "\tAND P3.id = #{idPeriode}\n" +
            "\tAND DATE(concat_ws('-', P1.bulan, 01, P1.tahun)) <= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "\tAND DATE(concat_ws('-', P2.bulan, 01, P2.tahun)) >= DATE(concat_ws('-', P3.bulan, 01, P3.tahun))\n" +
            "ORDER BY id_start_period ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="namaKlien", column="nama_klien"),
            @Result(property="keterangan", column="keterangan"),
            @Result(property="idPmo", column="id_pmo"),
            @Result(property="idParentProject", column="id_parent_project"),
            @Result(property="idProjectLead", column="id_project_lead"),
            @Result(property="idStartPeriod", column="id_start_period"),
            @Result(property="idEndPeriod", column="id_end_period")
    })
    List<ProyekModel> selectProyekByPeriode(@Param("idPeriode") int idPeriode);
}
