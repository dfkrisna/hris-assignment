package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.RekapModel;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RekapMapper {

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, periode, is_approved\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE id_karyawan_proyek = #{idKaryawanProyek}\n" +
            "\tAND periode = '${periode}';")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="periode", column="periode"),
            @Result(property="isApproved", column="is_approved")
    })
    RekapModel selectRekapByKarPoPer(@Param("idKaryawanProyek") int idKaryawanProyek, @Param("periode") LocalDate periode);

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, periode, is_approved, id_proyek\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE periode = '${periode}';")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="periode", column="periode"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="isApproved", column="is_approved")
    })
    List<RekapModel> selectRekapByPeriode(@Param("periode") LocalDate periode);

    @Select("SELECT SUM(RB.persentase_kontribusi)\n" +
            "FROM mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"REKAPITULASI_BULANAN\" AS RB\n" +
            "WHERE KP.id_karyawan = ${idKaryawan}\n" +
            "\tAND RB.id_karyawan_proyek = KP.id\n" +
            "\tAND RB.periode = '${periode}';")
    Double getKaryawanKontribusi(@Param("idKaryawan") int idKaryawan, @Param("periode") LocalDate periode);

    @Select("SELECT RB.id, K.id as id_karyawan, KP.id as id_karyawan_proyek, KP.id_proyek, RB.penilaian_mandiri, " +
            "RB.tanggal_penilaian, RB.is_approved, RB.periode, RB.persentase_kontribusi\n" +
            "FROM mpp.\"KARYAWAN\" as K, mpp.\"KARYAWAN_PROYEK\" as KP, mpp.\"REKAPITULASI_BULANAN\" as RB\n" +
            "WHERE K.id = ${idKaryawan}\n" +
            "      AND K.id = KP.id_karyawan\n" +
            "      AND KP.id_karyawan = RB.id_karyawan_proyek")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="periode", column="periode"),
            @Result(property="isApproved", column="is_approved")
    })
    List<RekapModel> getRekapPenilaianMandiri(@Param("idKaryawan") int idKaryawan);

    @Insert("INSERT INTO mpp.\"REKAPITULASI_BULANAN\"(\n" +
            "\tpenilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, id_periode, is_approved, periode, id_proyek)\n" +
            "\tVALUES (null, #{idKaryawanProyek}, #{tanggalPenilaian}, #{persentaseKontribusi}, 1, false, #{periode}, #{idProyek});")
    void insertRekap(RekapModel rekap);

    @Update("UPDATE mpp.\"REKAPITULASI_BULANAN\"\n" +
            "\tSET penilaian_mandiri=#{penilaianMandiri}, tanggal_penilaian=#{tanggalPenilaian}, persentase_kontribusi=#{persentaseKontribusi}, is_approved=#{isApproved}\n" +
            "\tWHERE id = #{id};")
    void updateRekap(RekapModel rekap);

    @Delete("DELETE FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "\tWHERE id = #{id};")
    void deleteRekap(RekapModel populatePrevRekap);

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, periode, is_approved, id_proyek\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE id_karyawan_proyek = '${idKaryawanProyek}'\n ;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="periode", column="periode"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="isApproved", column="is_approved")
    })
    List<RekapModel> selectRekapByIdKaryawanProyek(@Param("idKaryawanProyek") int idKaryawanProyek);

    @Update("UPDATE mpp.\"REKAPITULASI_BULANAN\"\n" +
            "\tSET penilaian_mandiri = '${penilaianMandiri}', tanggal_penilaian= #{tanggalPenilaian}" +
            "\tWHERE id = #{id};")
    void updatePenilaianMandiri(RekapModel rekap);

    @Select("SELECT id, penilaian_mandiri, id_karyawan_proyek, tanggal_penilaian, persentase_kontribusi, periode, is_approved, id_proyek\n" +
            "FROM mpp.\"REKAPITULASI_BULANAN\"\n" +
            "WHERE id = '${id}'\n ;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="periode", column="periode"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="isApproved", column="is_approved")
    })
    RekapModel selectRekapById(@Param("id") int id);

    @Select("SELECT RP.id, RP.penilaian_mandiri, RP.id_karyawan_proyek, RP.tanggal_penilaian, RP.persentase_kontribusi, RP.periode, RP.is_approved, RP.id_proyek\n" +
            "      FROM mpp.\"REKAPITULASI_BULANAN\" AS RP JOIN mpp.\"KARYAWAN_PROYEK\" AS KP ON KP.id=RP.id_karyawan_proyek\n" +
            "\t  JOIN mpp.\"KARYAWAN\" AS K ON K.id=KP.id_karyawan\n" +
            "      WHERE K.id = #{idKaryawan} AND RP.periode = '${periode}';")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="penilaianMandiri", column="penilaian_mandiri"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="tanggalPenilaian", column="tanggal_penilaian"),
            @Result(property="persentaseKontribusi", column="persentase_kontribusi"),
            @Result(property="periode", column="periode"),
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="isApproved", column="is_approved")
    })
    List<RekapModel> selectRekapKaryawanByPeriode(@Param("periode") LocalDate periodeDate, @Param("idKaryawan") int idKaryawan);
}
