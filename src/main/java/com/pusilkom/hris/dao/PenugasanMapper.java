package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.KaryawanAnggotaModel;
import com.pusilkom.hris.model.PenugasanModel;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PenugasanMapper {
    @Select("SELECT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id AS id_proyek, P.kode AS kode_proyek, P.nama_proyek, R.nama AS role, AVG(persentase_kontribusi) AS kontribusi, P.start_periode, P.end_periode\n" +
            "FROM mpp.\"PROYEK\" AS P, mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"KARYAWAN\" AS K, mpp.\"REKAPITULASI_BULANAN\" AS RP, mpp.\"RATING_FEEDBACK\" AS RF, mpp.\"ROLE_PROYEK\" AS R\n" +
            "WHERE K.id = ${idKaryawan}\n" +
            "\tAND P.id = KP.id_proyek\n" +
            "\tAND KP.id_karyawan = K.id\n" +
            "\tAND KP.id_role = R.id\n" +
            "\tAND KP.id = RP.id_karyawan_proyek\n" +
            "GROUP BY K.id, KP.id, P.id, P.kode, P.nama_proyek, R.nama, P.start_periode, P.end_periode\n" +
            "ORDER BY P.start_periode DESC;")
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="kontribusi", column="kontribusi"),
            //@Result(property="rating", column="rating"),
            @Result(property="kodeProyek", column="kode_proyek"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="role", column="role"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode")
    })
    List<PenugasanModel> selectPenugasanByKaryawan(@Param("idKaryawan") int idKaryawan);

    //mapper lihat penugasan
    @Select("SELECT DISTINCT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id as id_proyek, P.nama_proyek, RP.nama as nama_role, RB.persentase_kontribusi, KP.start_periode, KP.end_periode, KP.id\n" +
            "FROM mpp.\"PROYEK\" as P, mpp.\"REKAPITULASI_BULANAN\" as RB, mpp.\"ROLE_PROYEK\" as RP, mpp.\"KARYAWAN_PROYEK\" as KP, mpp.\"KARYAWAN\" as K\n" +
            "WHERE KP.id_karyawan = ${idKaryawan} AND KP.id_karyawan = K.id AND KP.id_proyek = P.id AND KP.id_role = RP.id\n" +
            "      AND KP.id = RB.id_karyawan_proyek\n" +
            "ORDER BY KP.start_periode DESC")
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="role", column="nama_role"),
            @Result(property="persentase", column="persentase_kontribusi"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode")
    })
    List<PenugasanModel> getRiwayatPenugasanKaryawan(@Param("idKaryawan") int idKaryawan);

    @Select("SELECT DISTINCT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id as id_proyek, P.nama_proyek, C.name as nama_klien, RP.nama as nama_role, SP.nama as status_proyek, RB.persentase_kontribusi, \n" +
            "      PENGGUNA.nama as team_lead, KP.start_periode, KP.end_periode, SPR.id_status\n" +
            "FROM mpp.\"PROYEK\" as P, mpp.\"KARYAWAN_PROYEK\" as KP, mpp.\"ROLE_PROYEK\" as RP, mpp.\"STATUS_PENGERJAAN_PROYEK\" as SPR, \n" +
            "      mpp.\"STATUS_PROYEK\" as SP, mpp.\"REKAPITULASI_BULANAN\" as RB, mpp.\"KARYAWAN\" as K, mpp.\"PENGGUNA\" as PENGGUNA, a07_client as C\n" +
            "WHERE KP.id_proyek = #{idProyek} AND KP.id_proyek = P.id AND KP.id_karyawan = #{idKaryawan} AND KP.id_role = RP.id AND\n" +
            "      RB.id_karyawan_proyek = KP.id AND KP.id_karyawan = K.id AND P.id = SPR.id_proyek AND SPR.id_status = SP.id \n" +
            "      AND K.id_pengguna = PENGGUNA.id AND C.id = P.id_klien\n" +
            "ORDER BY SPR.id_status DESC LIMIT 1;")
    @Results(value={
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="namaKlien", column="nama_klien"),
            @Result(property="role", column="nama_role"),
            @Result(property="statusProyek", column="status_proyek"),
            @Result(property="persentase", column="persentase_kontribusi"),
            @Result(property="teamLead", column="team_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
    })
    PenugasanModel getDetailPenugasanById(@Param("idProyek") int idProyek, @Param("idKaryawan") int idKaryawan);

    @Select("SELECT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id as id_proyek, P.nama_proyek, RP.nama as nama_role, \n" +
            "RB.persentase_kontribusi, KP.start_periode, KP.end_periode, (SELECT SP.nama\n" +
            "FROM mpp.\"STATUS_PENGERJAAN_PROYEK\" AS SPP, mpp.\"STATUS_PROYEK\" AS SP\n" +
            "WHERE SPP.id_proyek = P.id\n" +
            "\tAND SPP.id_status = SP.id\n" +
            "ORDER BY SPP.periode DESC LIMIT 1) AS status_proyek\n" +
            "FROM mpp.\"PROYEK\" as P, mpp.\"REKAPITULASI_BULANAN\" as RB, mpp.\"ROLE_PROYEK\" as RP, mpp.\"KARYAWAN_PROYEK\" as KP, mpp.\"KARYAWAN\" as K, public.\"a07_client\" AS C \n" +
            "WHERE K.id = ${idKaryawan} \n" +
            "AND KP.id_karyawan = K.id\n" +
            "\tAND KP.id_proyek = P.id \n" +
            "\tAND \n" +
            "\tKP.id_role = RP.id\n" +
            "\tAND KP.id = RB.id_karyawan_proyek \n" +
            "\tAND P.start_periode <= '${periode}'\n" +
            "\tAND (P.end_periode >= '${periode}' OR P.end_periode IS NULL)\n" +
            "\tAND RB.periode = '${periode}'\n" +
            "\tAND C.id = P.id_klien \n" +
            "ORDER BY KP.start_periode;")
    @Results(value={
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="namaKlien", column="nama_klien"),
            @Result(property="role", column="nama_role"),
            @Result(property="statusProyek", column="status_proyek"),
            @Result(property="persentase", column="persentase_kontribusi"),
            @Result(property="teamLead", column="team_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
    })
    List<PenugasanModel> getPenugasanPeriodeIni(@Param("idKaryawan") int idKaryawan, @Param("periode") LocalDate periode);

    @Select("SELECT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id AS id_proyek, P.kode AS kode_proyek, P.nama_proyek, R.nama AS role, AVG(rating) AS rating, AVG(persentase_kontribusi) AS kontribusi, P.start_periode, P.end_periode\n" +
            "FROM mpp.\"PROYEK\" AS P, mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"KARYAWAN\" AS K, mpp.\"REKAPITULASI_BULANAN\" AS RP, mpp.\"RATING_FEEDBACK\" AS RF, mpp.\"ROLE_PROYEK\" AS R\n" +
            "WHERE K.id = ${idKaryawan}\n" +
            "\tAND P.id = KP.id_proyek\n" +
            "\tAND KP.id_karyawan = K.id\n" +
            "\tAND KP.id_role = R.id\n" +
            "\tAND KP.id = RP.id_karyawan_proyek\n" +
            "\tAND P.id = RF.id_proyek\n" +
            "\tAND K.id = RF.id_karyawan_dinilai\n" +
            "GROUP BY K.id, KP.id, P.id, P.kode, P.nama_proyek, R.nama, P.start_periode, P.end_periode\n" +
            "ORDER BY P.start_periode DESC;")
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="kontribusi", column="kontribusi"),
            @Result(property="rating", column="rating"),
            @Result(property="role", column="role")
    })
    List<PenugasanModel> selectPenugasanByProyek(@Param("idProyek") int idProyek);

    @Select("SELECT DISTINCT K.id AS id_karyawan, KP.id AS id_karyawan_proyek, P.id AS id_proyek, PG.nama AS nama, R.nama AS role,  \n" +
            "            (SELECT persentase_kontribusi \n" +
            "            FROM mpp.\"REKAPITULASI_BULANAN\" AS RB \n" +
            "            WHERE RB.id_karyawan_proyek = KP.id \n" +
            "            ORDER BY RB.periode\n" +
            "            LIMIT 1) AS kontribusi \n" +
            "            FROM mpp.\"KARYAWAN\" AS K, mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"PROYEK\" AS P, mpp.\"ROLE_PROYEK\" AS R, mpp.\"PENGGUNA\" AS PG, mpp.\"REKAPITULASI_BULANAN\" AS RB , mpp.\"RATING_FEEDBACK\" AS RF \n" +
            "            WHERE P.id = ${idProyek}\n" +
            "            AND K.id_pengguna = PG.id \n" +
            "            AND P.id = KP.id_proyek \n" +
            "            AND K.id = KP.id_karyawan \n" +
            "            AND R.id = KP.id_role \n" +
            "            GROUP BY K.id, PG.nama, R.nama, RB.persentase_kontribusi, KP.id, P.id \n" +
            "            ORDER BY kontribusi DESC;")
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id_karyawan_proyek"),
            @Result(property="nama", column="nama"),
            @Result(property="kontribusi", column="kontribusi"),
            @Result(property="rating", column="rating"),
            @Result(property="role", column="role")
    })
    List<KaryawanAnggotaModel> selectAnggotaProyek(@Param("idProyek") int idProyek);

    @Select("SELECT ROUND(AVG(RF.rating))\n" +
            "FROM mpp.\"RATING_FEEDBACK\" AS RF\n" +
            "WHERE RF.id_karyawan_dinilai = ${idKaryawan}\n" +
            "\tAND RF.id_proyek = ${idProyek};")
    Integer selectAvgRating(@Param("idProyek") int idProyek, @Param("idKaryawan") int idKaryawan);

    @Insert("INSERT INTO mpp.\"STATUS_PENGERJAAN_PROYEK\"(\n" +
            "\tid_status, id_proyek, periode)\n" +
            "\tVALUES (${idStatus}, ${idProyek}, '${periode}');")
    void insertStatusProyek(@Param("idStatus") int idStatus, @Param("idProyek") Integer idProyek, @Param("periode") LocalDate startPeriode);

    @Select("SELECT id\n" +
            "\tFROM mpp.\"STATUS_PROYEK\"\n" +
            "\tWHERE nama='${statusAktif}';")
    int getIDStatus(@Param("statusAktif") String statusAktif);

    @Select("SELECT nama FROM (SELECT S.nama\n" +
            "            FROM mpp.\"STATUS_PROYEK\" AS S, mpp.\"STATUS_PENGERJAAN_PROYEK\" AS SP\n" +
            "            WHERE SP.id_proyek = ${idProyek}\n" +
            "            AND SP.id_status = S.id\n" +
            "            ORDER BY SP.timestamp DESC) AS stats\n" +
            "            LIMIT 1;"
    )
    String selectLatestStatus(@Param("idProyek") int idProyek);

    @Select("SELECT P.id AS id_proyek, KP.start_periode, KP.end_periode, K.id AS id_kar, " +
            "(SELECT (AVG(RB.persentase_kontribusi)) FROM mpp.\"REKAPITULASI_BULANAN\" AS RB " +
            "   WHERE RB.id_karyawan_proyek = KP.id) AS kontribusi, " +
            "(SELECT ROUND(AVG(RF.rating)) FROM mpp.\"RATING_FEEDBACK\" " +
            "   AS RF WHERE RF.id_karyawan_dinilai = KP.id AND RF.id_proyek = ${idProyek} ) AS rating, " +
            "(SELECT P.nama FROM mpp.\"PENGGUNA\" AS P WHERE K.id_pengguna = P.id) AS nama_karyawan, " +
            "(SELECT RP.nama FROM mpp.\"ROLE_PROYEK\" AS RP WHERE RP.id = KP.id_role) AS role " +
            "FROM mpp.\"PROYEK\" AS P, mpp.\"KARYAWAN_PROYEK\" AS KP, mpp.\"KARYAWAN\" AS K " +
            "WHERE P.id = KP.id_proyek AND K.id = KP.id_karyawan AND P.id = ${idProyek};\n")
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_kar"),
            @Result(property="namaKaryawan", column="nama_karyawan"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="kontribusi", column="kontribusi"),
            @Result(property="rating", column="rating"),
            @Result(property="role", column="role")
    })
    List<PenugasanModel> getListPenugasan(@Param("idProyek") int idProyek);

}
