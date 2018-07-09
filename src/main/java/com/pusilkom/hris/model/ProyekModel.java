package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekModel {
    private int id;
    private String kode;
    private String namaProyek;
    private Integer idKlien;
    private String namaKlien;
    private String keterangan;
    private Integer idPmo;
    private Integer idParent;
    private Integer idProjectLead;
    private String timestamp;
    private LocalDate startPeriode;
    private LocalDate endPeriode;

    public String getPeriode() {
        if (startPeriode == null && endPeriode == null) {
            return "-";
        } else if (endPeriode == null) {
            return startPeriode.getMonth() + " " + startPeriode.getYear() + " - Sekarang";
        } else {
            return (startPeriode.getMonth() + " " + startPeriode.getYear() + " - " + endPeriode.getMonth() + " " + endPeriode.getYear());
        }
    }

}