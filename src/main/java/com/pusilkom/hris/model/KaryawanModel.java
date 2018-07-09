package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaryawanModel {
    private int id;
    private int idPengguna;
    private int idDivisi;
    private String nama;

    private int ratingKaryawan;
}
