package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.RatingFeedbackMapper;
import com.pusilkom.hris.model.RatingFeedbackModel;
import com.pusilkom.hris.model.RoleProyekModel;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface RatingFeedbackService {
    List<RatingFeedbackModel> getPenilaianKaryawan(int idKaryawan, LocalDate periode);

    int getAvgRatingKaryawan(int idKaryawan);

    int getCumRating(int idKaryawan, LocalDate periode);

    int getAvgRating(int idKaryawan, LocalDate periode);

    int[] getRecapRating(int idKaryawan, LocalDate periode);

    int[] getRecapRatingAvg(int idKaryawan, LocalDate periode);

    List<RatingFeedbackModel> selectRatingFeedbackKP(Integer idKaryawanProyek);
    List<RatingFeedbackModel> selectRatingFeedbackPer(Integer idKaryawanProyek, LocalDate periode);

}

