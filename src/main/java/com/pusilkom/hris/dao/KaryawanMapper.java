package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.FeedbackRatingModel;
import com.pusilkom.hris.model.KaryawanModel;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface KaryawanMapper {

    @Select("select id from mpp.\"KARYAWAN_PROYEK\" where id_karyawan = #{idKaryawan}")
    List<String> cekPenugasanKaryawanById(@Param("idKaryawan") int idKaryawan);

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
            "WHERE P.id = #{idPengguna}" +
            "\tAND K.id_pengguna = P.id;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    KaryawanModel selectKaryawanByIdPengguna(@Param("idPengguna") int idPengguna);

    @Select("SELECT DISTINCT K.id, id_pengguna, id_divisi, P.nama\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE K.id_pengguna = P.id  \n" +
            "\tAND K.id IN \n" +
            "\t\t(SELECT id_karyawan\n" +
            "\t\tFROM mpp.\"KARYAWAN_PROYEK\"\n" +
            "\t\tWHERE start_periode <= '${periode}'\n" +
            "\t\tAND (end_periode >= '${periode}' OR end_periode IS NULL)\n" +
            "\t\tORDER BY start_periode ASC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    List<KaryawanModel> selectKaryawanByPeriode(@Param("idPeriode") LocalDate periode);

    @Select("SELECT K.id, K.id_divisi\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE P.id = K.id_pengguna AND P.username = #{email};")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idDivisi", column="id_divisi"),
    })
    KaryawanModel selectKaryawanByEmail(@Param("email") String email);

    @Select("SELECT DISTINCT K.id, id_pengguna, id_divisi, P.nama\n" +
            "FROM mpp.\"KARYAWAN\" AS K, mpp.\"PENGGUNA\" AS P\n" +
            "WHERE K.id_divisi = #{idDivisi}" +
            "\tAND id_pengguna = P.id;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="idPengguna", column="id_pengguna"),
            @Result(property="idDivisi", column="id_divisi"),
            @Result(property="nama", column="nama")
    })
    List<KaryawanModel> selectKaryawanByDivisi(@Param("idDivisi") int idDivisi);

    @Select("select distinct kp.id from " +
            "mpp.\"RATING_FEEDBACK\" as rt, " +
            "mpp.\"KARYAWAN_PROYEK\" as kp " +
            "where kp.id_karyawan = #{idKaryawan} and kp.id_proyek = #{idProyek}")
    int getKaryawanIdPenilai(@Param("idKaryawan") int idKaryawan,
                             @Param("idProyek") int idProyek);

    @Select("select distinct id from mpp.\"PENGGUNA\" where username = #{username}")
    int getKaryawanIdByUsername(@Param("username") String username);

    @Select("select id_proyek from mpp.\"KARYAWAN_PROYEK\" where id_karyawan = #{idKaryawan}")
    List<Integer> getUserProyek(@Param("idKaryawan") int idKaryawan);

    @Select("select kr.id_karyawan as idRekan, p.nama as namaRekan, rp.nama as roleRekan, " +
            "pr.kode as kodeProyek, rt.rating as ratingRekan, rt.feedback as feedback " +
            "from " +
            "mpp.\"PENGGUNA\" as p, mpp.\"ROLE_PROYEK\" as rp, " +
            "mpp.\"PROYEK\" as pr, mpp.\"RATING_FEEDBACK\" as rt, " +
            "mpp.\"KARYAWAN\" as k, mpp.\"KARYAWAN_PROYEK\" as kr " +
            "where kr.id_karyawan != #{idKaryawan} and kr.id_proyek = #{idProyek} and kr.id_karyawan = k.id " +
            "and k.id_pengguna = p.id and kr.id_role = rp.id and kr.id_proyek = pr.id " +
            "and kr.start_periode <= #{periodeNow} and (not kr.end_periode < #{periodeNow} or kr.end_periode is null)" +
            " and kr.id = rt.id_karyawan_dinilai " +
            "and rt.id_penilai = #{idPenilai} and rt.periode = #{periodeNow}")
    List<FeedbackRatingModel> getRekanSeproyekFeedback(@Param("idProyek") int idProyek,
                                                       @Param("idKaryawan") int idKaryawan,
                                                       @Param("periodeNow") LocalDate periodeNow,
                                                       @Param("idPenilai") int idPenilai);

    @Select("select kr.id_karyawan as idRekan, p.nama as namaRekan, rp.nama as roleRekan, " +
            "pr.kode as kodeProyek, 0 as ratingRekan, " +
            "'Belum memberikan Feedback' as feedback " +
            "from " +
            "mpp.\"PENGGUNA\" as p, mpp.\"ROLE_PROYEK\" as rp, " +
            "mpp.\"PROYEK\" as pr, mpp.\"KARYAWAN\" as k, mpp.\"KARYAWAN_PROYEK\" as kr " +
            "where " +
            "kr.id_karyawan != #{idKaryawan} and kr.id_proyek = #{idProyek} and kr.id_karyawan = k.id " +
            "and k.id_pengguna = p.id and kr.id_role = rp.id and kr.id_proyek = pr.id " +
            "and kr.start_periode <= #{periodeNow} and (not kr.end_periode < #{periodeNow} or kr.end_periode is null) " +
            "and not exists" +
            "(select * from mpp.\"RATING_FEEDBACK\" as rt where kr.id = rt.id_karyawan_dinilai " +
            "and rt.periode = #{periodeNow})")
    List<FeedbackRatingModel> getRekanSeproyek(@Param("idProyek") int idProyek,
                                               @Param("idKaryawan") int idKaryawan,
                                               @Param("periodeNow") LocalDate periodeNow);

    @Select("select rating as ratingRekan, feedback as feedback " +
            "from mpp.\"RATING_FEEDBACK\" " +
            "where id_karyawan_dinilai = #{idKaryawanProyek} and id_penilai = #{idPenilai} " +
            "and id_proyek = #{idProyek} and periode = #{periodeSelected}")
    FeedbackRatingModel verifyFeedbackRekan(@Param("idKaryawanProyek") int idKaryawanProyek,
                                            @Param("idPenilai") int idPenilai,
                                            @Param("idProyek") int idProyek,
                                            @Param("periodeSelected") LocalDate periodeSelected);

    @Insert("insert into mpp.\"RATING_FEEDBACK\" (rating, feedback, tanggal, id_karyawan_dinilai, id_penilai, periode, id_proyek)\n" +
            "values (#{ratingRekan}, #{feedback}, #{waktuIsi}, #{idKaryawanProyek}, #{idPenilai}, #{periodeSelected}, " +
            "#{idProyek})")
    void addFeedbackRekan(@Param("feedback") String feedback,
                          @Param("ratingRekan") int ratingRekan,
                          @Param("idKaryawanProyek") int idKaryawanProyek,
                          @Param("idPenilai") int idPenilai,
                          @Param("idProyek") int idProyek,
                          @Param("periodeSelected") LocalDate periodeSelected,
                          @Param("waktuIsi") Timestamp waktuIsi);

    @Update("update mpp.\"RATING_FEEDBACK\" " +
            "set feedback = #{feedback}, rating = #{ratingRekan}, tanggal = #{waktuIsi} " +
            "where id_karyawan_dinilai = #{idKaryawanProyek} and id_penilai = #{idPenilai} " +
            "and id_proyek = #{idProyek} and periode = #{periodeSelected}")
    void updateFeedbackRekan(@Param("feedback") String feedback,
                          @Param("ratingRekan") int ratingRekan,
                          @Param("idKaryawanProyek") int idKaryawanProyek,
                          @Param("idPenilai") int idPenilai,
                          @Param("idProyek") int idProyek,
                          @Param("periodeSelected") LocalDate periodeSelected,
                          @Param("waktuIsi") Timestamp waktuIsi);

    @Delete("delete from mpp.\"KARYAWAN\" where id_pengguna = #{id}")
    void deleteKaryawan(@Param("id") int id);

}
