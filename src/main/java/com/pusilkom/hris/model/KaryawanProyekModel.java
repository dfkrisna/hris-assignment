package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaryawanProyekModel {
    private int id;
    private int idKaryawan;
    private int idProyek;
    private String timestampInisiasi;
    private int idRole;

    private String namaProyek;
    private String namaKlien;
    private String statusProyek;
    private String teamLead;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startPeriode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endPeriode;

    public String getPeriode() {
        if (startPeriode == null && endPeriode == null) {
            return "-";
        } else if (endPeriode == null) {
            return startPeriode.getMonth() + " " + startPeriode.getYear() + " - Present";
        } else {
            return (startPeriode.getMonth() + " " + startPeriode.getYear() + " - " + endPeriode.getMonth() + " " + endPeriode.getYear());
        }
    }
}
