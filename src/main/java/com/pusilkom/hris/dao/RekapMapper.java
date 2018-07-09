package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.RekapModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RekapMapper {

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, id_periode, is_approved\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE id_karyawan_proyek = #{idKaryawanProyek}\n" +
            "\tAND id_periode = #{idPeriode};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="pesentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="idPeriode", column="id_periode"),
            @Result(property="isApproved", column="is_approved")
    })
    RekapModel selectRekapByKarPoPer(@Param("idKaryawanProyek") int idKaryawanProyek, @Param("idPeriode") int idPeriode);

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, id_periode, is_approved\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE id_periode = #{idPeriode};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="idPeriode", column="id_periode"),
            @Result(property="isApproved", column="is_approved")
    })
    List<RekapModel> selectRekapByPeriode(@Param("idPeriode") int idPeriode);
}
