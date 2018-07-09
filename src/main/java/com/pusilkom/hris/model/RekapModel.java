package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RekapModel {
    private int id;
    private int idKaryawanProyek;
    private LocalDate periode;
    private double persentaseKontribusi;
    private boolean isApproved;

    private String penilaianMandiri;
    private LocalDate tanggalPenilaian;

    private int idProyek;
    private int idKaryawan;

    public int getPersentase() {
        return (int) (getPersentaseKontribusi()*100);
    }

    public boolean getStatusPenilaianMandiri(){
        return isApproved;
    }
}
