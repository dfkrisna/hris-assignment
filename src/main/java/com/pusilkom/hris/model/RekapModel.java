package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RekapModel {
    private int id;
    private int idKaryawanProyek;
    private int idPeriode;
    private double persentaseKontribusi;
    private boolean isApproved;

    private String penilaianMandiri;
    private String tanggalPenilaian;

    public int getPersentase() {
        return (int) (getPersentaseKontribusi()*100);
    }
}
