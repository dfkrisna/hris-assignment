package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.RatingFeedbackModel;
import com.pusilkom.hris.model.RoleProyekModel;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RatingFeedbackMapper {
    @Select("SELECT RF.id, P.id AS id_penilai, P.nama AS nama_penilai, PR.kode, RP.nama AS role_penilai, RF.feedback, RF.rating, RF.tanggal AS \"tanggal_penilaian\"\n" +
            "FROM mpp.\"RATING_FEEDBACK\" AS RF\n" +
            "\tJOIN mpp.\"KARYAWAN\" AS K ON K.id = RF.id_penilai\n" +
            "\tJOIN mpp.\"PENGGUNA\" AS P ON P.id = K.id_pengguna\n" +
            "\tJOIN mpp.\"PROYEK\" AS PR ON PR.id = RF.id_proyek \n" +
            "\tLEFT JOIN mpp.\"KARYAWAN_PROYEK\" AS KP ON KP.id_karyawan = K.id AND KP.id_proyek = PR.id\n" +
            "\tLEFT JOIN mpp.\"ROLE_PROYEK\" AS RP ON RP.id = KP.id_role\n" +
            "WHERE RF.id_karyawan_dinilai = #{idKaryawan}\n" +
            "\tAND RF.periode = '${periode}'\n" +
            "ORDER BY RF.id ASC;")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "idPenilai", column = "id_penilai"),
            @Result(property = "namaPenilai", column = "nama_penilai"),
            @Result(property = "kodeProyek", column = "kode"),
            @Result(property = "feedback", column = "feedback"),
            @Result(property = "rating", column = "rating"),
            @Result(property = "tanggalPenilaian", column = "tanggal_penilaian"),
            @Result(property = "rolePenilai", column = "role_penilai")
    })
    List<RatingFeedbackModel> selectPenilaianKaryawan(@Param("idKaryawan") int idKaryawan, @Param("periode") LocalDate periode);

    @Select("SELECT ROUND(AVG(rating)) FROM mpp.\"RATING_FEEDBACK\" WHERE id_karyawan_dinilai = #{idKaryawan};")
    Integer selectAvgRatingKaryawan(@Param("idKaryawan") int idKaryawan);

    @Select("SELECT ROUND(AVG(RF.rating))\n" +
            "FROM mpp.\"RATING_FEEDBACK\" AS RF\n" +
            "WHERE RF.id_karyawan_dinilai = #{idKaryawan}\n" +
            "\tAND RF.periode = '${periode}';")
    Integer selectAvgRating(@Param("idKaryawan") int idKaryawan, @Param("periode") LocalDate periode);

    @Select("SELECT ROUND(AVG(RF.rating))\n" +
            "FROM mpp.\"RATING_FEEDBACK\" AS RF\n" +
            "WHERE RF.id_karyawan_dinilai = #{idKaryawan}\n" +
            "\tAND RF.periode <= '${periode}';")
    Integer selectCumRating(@Param("idKaryawan") int idKaryawan, @Param("periode") LocalDate periode);


//punya jihan
    @Select("SELECT RF.periode, RF.id, PA.nama AS namaPenilai, RF.feedback, RF.rating, RF.tanggal, " +
            " RP.nama AS rolePenilai, P.nama_proyek FROM mpp.\"RATING_FEEDBACK\" AS RF, mpp.\"KARYAWAN_PROYEK\" AS KPA, " +
            " mpp.\"KARYAWAN_PROYEK\" AS KPB, mpp.\"KARYAWAN\" AS KA, mpp.\"PENGGUNA\" AS PA, mpp.\"ROLE_PROYEK\" AS RP, " +
            " mpp.\"PROYEK\" AS P WHERE RF.id_karyawan_dinilai = ${idKaryawanProyek} AND RF.id_karyawan_dinilai = KPB.id AND " +
            " KPA.id = RF.id_penilai AND KPA.id_karyawan = KA.id AND KA.id_pengguna = PA.id AND KPA.id_role = RP.id AND " +
            " P.id = RF.id_proyek;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="namaPenilai", column="namaPenilai"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="feedback", column="feedback"),
            @Result(property="rating", column="rating"),
            @Result(property="tanggalPenilaian", column="tanggal"),
            @Result(property="rolePenilai", column="rolePenilai"),
            @Result(property ="periode", column="periode")
    })
    List<RatingFeedbackModel> selectRatingFeedbackKP(@Param("idKaryawanProyek") Integer idKaryawanProyek);

    @Select("SELECT RF.periode, RF.id, PA.nama AS namaPenilai, RF.feedback, RF.rating, RF.tanggal, " +
            " RP.nama AS rolePenilai, P.nama_proyek FROM mpp.\"RATING_FEEDBACK\" AS RF, mpp.\"KARYAWAN_PROYEK\" AS KPA, " +
            " mpp.\"KARYAWAN_PROYEK\" AS KPB, mpp.\"KARYAWAN\" AS KA, mpp.\"PENGGUNA\" AS PA, mpp.\"ROLE_PROYEK\" AS RP, " +
            " mpp.\"PROYEK\" AS P WHERE RF.id_karyawan_dinilai = ${idKaryawanProyek} AND RF.id_karyawan_dinilai = KPB.id AND " +
            " KPA.id = RF.id_penilai AND KPA.id_karyawan = KA.id AND KA.id_pengguna = PA.id AND KPA.id_role = RP.id AND " +
            " P.id = RF.id_proyek AND RF.periode = '${periode}' ORDER BY RF.tanggal DESC;")
    @Results(value = {
            @Result(property="id", column="id"),
            @Result(property="namaPenilai", column="namaPenilai"),
            @Result(property="namaProyek", column="nama_proyek"),
            @Result(property="feedback", column="feedback"),
            @Result(property="rating", column="rating"),
            @Result(property="tanggalPenilaian", column="tanggal"),
            @Result(property="rolePenilai", column="rolePenilai"),
            @Result(property ="periode", column="periode")
    })
    List<RatingFeedbackModel> selectRatingFeedbackPer(@Param("idKaryawanProyek") Integer idKaryawanProyek,
                                                     @Param("periode") LocalDate periode);





//    @Select(" SELECT RF.id, PG.nama AS namaPenilai, P.nama_proyek AS namaProyek, RF.feedback, RF.rating, " +
//            "        RF.tanggal AS tanggalPenilaian, RP.nama AS rolePenilai " +
//            " FROM mpp.\"RATING_FEEDBACK\" AS RF, " +
//            "     mpp.\"PROYEK\" AS P, " +
//            "     mpp.\"KARYAWAN\" AS K," +
//            "     mpp.\"PENGGUNA\" AS PG, " +
//            "     mpp.\"KARYAWAN_PROYEK\" AS KP, " +
//            "     mpp.\"ROLE_PROYEK\" AS RP " +
//            " WHERE RF.id_proyek = P.id " +
//            "    AND RP.id = KP.id_role " +
//            "    AND KP.id_karyawan = RF.id_penilai " +
//            "    AND KP.id_proyek = RF.id_proyek " +
//            "    AND PG.id = K.id_pengguna " +
//            "    AND RF.id_penilai = K.id " +
//            "    AND RF.id_karyawan_dinilai = ${idKaryawan} " +
//            "    AND RF.periode = '${periode}' " +
//            "    ORDER BY periode DESC;")
//    @Results(value = {
//            @Result(property="id", column="id"),
//            @Result(property="namaPenilai", column="namaPenilai"),
//            @Result(property="namaProyek", column="namaProyek"),
//            @Result(property="feedback", column="feedback"),
//            @Result(property="rating", column="rating"),
//            @Result(property="tanggalPenilaian", column="tanggalPenilaian"),
//            @Result(property="rolePenilai", column="rolePenilai")
//    })
//    List<RatingFeedbackModel> selectRatingFeedbackPer(@Param("idKaryawan") Integer idKaryawan,
//                                                      @Param("periode") LocalDate periode);



}
