package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingFeedbackModel {
    private int id;
    private int idPenilai;
    private String namaPenilai;
    private String kodeProyek;
    private String namaProyek;
    private String rolePenilai;
    private String feedback;
    private int rating;
    private LocalDate tanggalPenilaian;
    private String periode;
}
