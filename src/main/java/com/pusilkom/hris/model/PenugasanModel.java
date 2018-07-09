package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenugasanModel {
    int idProyek;
    int idKaryawan;
    int idKaryawanProyek;

    private String namaKaryawan;

    private double kontribusi;
    private double rating;
    private String kodeProyek;
    private String namaProyek;
    private String role;
    private LocalDate startPeriode;
    private LocalDate endPeriode;

    private String statusProyek;

    //lihat penugasan
    private String namaKlien;
    private double persentase;
    private String teamLead;

    public int getPersentase(){ return (int) Math.round(persentase * 100);}

    public int getPersentaseKontribusi() {
        return (int) Math.round(kontribusi * 100);
    }

    public String getPeriode() {
        if (startPeriode == null && endPeriode == null) {
            return "-";
        } else if (endPeriode == null) {
            return startPeriode.getMonth() + " " + startPeriode.getYear() + " - Sekarang";
        } else {
            return (startPeriode.getMonth() + " " + startPeriode.getYear() + " - " + endPeriode.getMonth() + " " + endPeriode.getYear());
        }
    }

    public int getIntRating() {
        return (int) rating;
    }
}
