package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.RatingFeedbackMapper;
import com.pusilkom.hris.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Primary
public class RatingFeedbackServiceImpl implements RatingFeedbackService {
    @Autowired
    RatingFeedbackMapper ratingFeedbackMapper;

    public List<RatingFeedbackModel> getPenilaianKaryawan(int idKaryawan, LocalDate periode) {
        return ratingFeedbackMapper.selectPenilaianKaryawan(idKaryawan, periode);
    }

    public int getAvgRatingKaryawan(int idKaryawan) {
        Integer result = ratingFeedbackMapper.selectAvgRatingKaryawan(idKaryawan);

        if (result == null) {
            return 0;
        } else {
            return result;
        }
    }

    public int getCumRating(int idKaryawan, LocalDate periode) {
        Integer result = ratingFeedbackMapper.selectCumRating(idKaryawan, periode);

        if (result == null) {
            return 0;
        } else {
            return result;
        }
    }

    public int getAvgRating(int idKaryawan, LocalDate periode) {
        Integer result = ratingFeedbackMapper.selectAvgRating(idKaryawan, periode);

        if (result == null) {
            return 0;
        } else {
            return result;
        }
    }

    public int[] getRecapRating(int idKaryawan, LocalDate periode) {
        int[] ratings = new int[6];
        for(int i = 0; i < 6; i++) {
            ratings[5-i] = getCumRating(idKaryawan, periode.minusMonths(i));
        }
        return ratings;
    }

    public int[] getRecapRatingAvg(int idKaryawan, LocalDate periode) {
        int[] ratings = new int[6];
        for(int i = 0; i < 6; i++) {
            ratings[5-i] = getAvgRating(idKaryawan, periode.minusMonths(i));
        }
        return ratings;
    }

    public List<RatingFeedbackModel> selectRatingFeedbackKP(Integer idKaryawanProyek) {
        return ratingFeedbackMapper.selectRatingFeedbackKP(idKaryawanProyek);
    }
    public List<RatingFeedbackModel> selectRatingFeedbackPer(Integer idKaryawanProyek, LocalDate periode){
        return ratingFeedbackMapper.selectRatingFeedbackPer(idKaryawanProyek,periode);
    }

}
