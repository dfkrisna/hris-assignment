package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.ProyekModel;
import com.pusilkom.hris.model.PenugasanModel;
import com.pusilkom.hris.model.RatingFeedbackModel;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProyekMapper {

    @Select("SELECT P.id, P.kode, P.nama_proyek, P.id_klien, C.name AS nama_klien, P.keterangan, P.id_pmo, P.id_parent_project, P.id_project_lead, P.start_periode, P.end_periode\n" +
            "FROM mpp.\"PROYEK\" AS P JOIN public.\"a07_client\" AS C ON C.id = P.id_klien \n" +
            "WHERE P.id = ${idProyek}\n" +
            "ORDER BY P.start_periode ASC;")
    @Results(value={
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idParent", column="id_parent_project"),
            @Result(property="idKlien", column="id_klien"),
            @Result(property="namaKlien", column="nama_klien"),
            @Result(property="namaRole", column="nama_role"),
            @Result(property="statusProyek", column="status_proyek"),
            @Result(property="persentase", column="persentase_kontribusi"),
            @Result(property="teamLead", column="team_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
            @Result(property="idProjectLead", column="id_project_lead")
    })
    ProyekModel selectProyekById(@Param("idProyek") int idProyek);

    @Select("SELECT DISTINCT id, kode, nama_proyek, id_klien, keterangan, id_pmo, id_parent_project, id_project_lead, start_periode, end_periode\n" +
            "            FROM mpp.\"PROYEK\"\n" +
            "            WHERE start_periode <= '${periode}'\n" +
            "            AND (end_periode >= '${periode}' OR end_periode IS NULL)\n" +
            "            ORDER BY start_periode ASC;")
            @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idKlien", column="id_klien"),
            @Result(property="keterangan", column="keterangan"),
            @Result(property="idPmo", column="id_pmo"),
            @Result(property="idParent", column="id_parent_project"),
            @Result(property="idProjectLead", column="id_project_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode")
    })
    List<ProyekModel> selectProyekByPeriode(@Param("periode") LocalDate periode);


    @Update("UPDATE mpp.\"PROYEK\" set kode = #{kode}, nama_proyek = #{namaProyek}, id_klien = #{idKlien}, " +
            "id_project_lead = #{idProjectLead}, start_periode = #{startPeriode}, end_periode = #{endPeriode}," +
            "keterangan = #{keterangan}, id_pmo =  #{idPmo}, id_parent_project = #{idParent} " +
            "where id = #{id}")
    void updateProyek(ProyekModel proyek);

    @Insert("INSERT INTO mpp.\"PROYEK\" (kode,nama_proyek,id_klien,keterangan,id_pmo," +
            "id_parent_project,id_project_lead,start_periode,end_periode,timestamp) " +
            "VALUES (#{kode}, #{namaProyek}, #{idKlien}, #{keterangan}, #{idPmo}, #{idParent}, " +
            "#{idProjectLead}, '${startPeriode}', '${endPeriode}', now() )")
    void addProyek (ProyekModel proyek);

    @Insert("INSERT INTO mpp.\"PROYEK\" (kode,nama_proyek,id_klien,keterangan,id_pmo," +
            "id_parent_project,id_project_lead,start_periode,end_periode,timestamp) " +
            "VALUES (#{kode}, #{namaProyek}, #{idKlien}, #{keterangan}, #{idPmo}, #{idParent}, " +
            "#{idProjectLead}, '${startPeriode}', null, now() )")
    void addProyekNullEnd (ProyekModel proyek);

    @Insert("INSERT INTO mpp.\"PROYEK\" (kode,nama_proyek,id_klien,keterangan,id_pmo," +
            "id_parent_project,id_project_lead,start_periode,end_periode,timestamp) " +
            "VALUES (#{kode}, #{namaProyek}, #{idKlien}, #{keterangan}, #{idPmo}, #{idParent}, " +
            "#{idProjectLead}, null, null, now() )")
    void addProyekNullBoth (ProyekModel proyek);

    @Update("update mpp.\"PROYEK\" set id_pmo = null where id_pmo = #{id}")
    void deleteIdPmo(@Param("id") int id);

    @Update("update mpp.\"PROYEK\" set id_project_lead = null where id_project_lead = #{id}")
    void deleteIdProjectLead(@Param("id") int id);

    @Select("SELECT DISTINCT id, kode, nama_proyek, id_klien, keterangan, id_pmo, id_parent_project, id_project_lead, start_periode, end_periode\n" +
            "            FROM mpp.\"PROYEK\"\n" +
            "            WHERE start_periode <= '${periode}'\n" +
            "            AND (end_periode > '${periode}' OR end_periode IS NULL)\n" +
            "            ORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idKlien", column="id_klien"),
            @Result(property="keterangan", column="keterangan"),
            @Result(property="idPmo", column="id_pmo"),
            @Result(property="idParent", column="id_parent_project"),
            @Result(property="idProjectLead", column="id_project_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode")
    })
    List<ProyekModel> selectProyekByPeriodePmo(@Param("periode") LocalDate periode);

    @Select("SELECT * " +
            "            FROM mpp.\"PROYEK\" WHERE id = ${idProyek} \n" +
            "            ORDER BY nama_proyek ASC;")
    @Results(value={
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idKlien", column="id_klien"),
            @Result(property="keterangan", column="keterangan"),
            @Result(property="idPmo", column="id_pmo"),
            @Result(property="idParent", column="id_parent_project"),
            @Result(property="idProjectLead", column="id_project_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
    })
    ProyekModel selectProyekByIdPmo(@Param("idProyek") Integer idProyek);

    @Insert("INSERT INTO mpp.\"STATUS_PENGERJAAN_PROYEK\" (id_status,id_proyek,periode,timestamp)"+
            "VALUES (${idStatus}, ${idProyek},'${periode}',now() )" )
    void addProyekStatus(@Param("idProyek") Integer idProyek, @Param("idStatus") Integer idStatus, @Param("periode") LocalDate periode);

    @Select("SELECT DISTINCT id, kode, nama_proyek, id_klien, keterangan, id_pmo, id_parent_project, id_project_lead, start_periode, end_periode\n" +
            "            FROM mpp.\"PROYEK\"\n" +
            "            WHERE kode LIKE '${kode}'\n" +
            "            ORDER BY start_periode ASC;")
    @Results(value={
            @Result(property = "id", column = "id"),
            @Result(property = "kode", column = "kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idKlien", column="idklien"),
            @Result(property="namaRole", column="nama_role"),
            @Result(property="statusProyek", column="status_proyek"),
            @Result(property="persentase", column="persentase_kontribusi"),
            @Result(property="teamLead", column="team_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
    })
    ProyekModel selectProyekByKode(@Param("kode") String kode);

    @Select("SELECT KP.id_proyek, KP.id_karyawan, KP.id, P.kode, P.nama_proyek, "+
            " K.name, KP.start_periode, KP.end_periode, "+
            "   (SELECT persentase_kontribusi FROM mpp.\"REKAPITULASI_BULANAN\" AS RB "+
            "     WHERE RB.id_karyawan_proyek = KP.id ORDER BY RB.periode DESC LIMIT 1) AS kontribusi, "+
            "   (SELECT RP.nama FROM mpp.\"ROLE_PROYEK\" AS RP WHERE RP.id = KP.id_role) AS role " +
            "FROM mpp.\"PROYEK\" AS P, mpp.\"KARYAWAN_PROYEK\" AS KP, \"a07_client\" AS K "+
            "WHERE KP.id_proyek = P.id "+
            "   AND K.id = P.id_klien " +
            "   AND KP.id_role = 1 " +
            "   AND KP.id_karyawan = ${idKaryawan} " +
            "   ORDER BY P.start_periode " +
            "   DESC;"
    )
    @Results(value = {
            @Result(property="idProyek", column="id_proyek"),
            @Result(property="idKaryawan", column="id_karyawan"),
            @Result(property="idKaryawanProyek", column="id"),
            @Result(property="kontribusi", column="kontribusi"),
            @Result(property="kodeProyek", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="role", column="role"),
            @Result(property="namaKlien", column="name"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode")
    })
    List<PenugasanModel> selectProyekDipimpin(@Param("idKaryawan") Integer idKaryawan);

    @Select("SELECT * " +
            "            FROM mpp.\"PROYEK\" WHERE id_parent_project = ${idProyek} \n" +
            "            ORDER BY start_periode DESC;")
    @Results(value={
            @Result(property="kode", column="kode"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="idKlien", column="id_klien"),
            @Result(property="keterangan", column="keterangan"),
            @Result(property="idPmo", column="id_pmo"),
            @Result(property="idParent", column="id_parent_project"),
            @Result(property="idProjectLead", column="id_project_lead"),
            @Result(property="startPeriode", column="start_periode"),
            @Result(property="endPeriode", column="end_periode"),
    })
    List<ProyekModel> selectProyekByIdParent(@Param("idProyek") Integer idProyek);

}



