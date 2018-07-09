package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaryawanAnggotaModel {
    private int idKaryawan;
    private int idKaryawanProyek;
    private int idProyek;
    private String nama;
    private String role;
    private double kontribusi;
    private double rating;

    public int getIntRating() {
        return (int) rating;
    }

    public int getPersentase() {
        return (int) (kontribusi * 100);
    }
}
