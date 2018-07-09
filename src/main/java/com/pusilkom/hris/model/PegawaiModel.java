package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PegawaiModel {
    private int id;
    private String nama;
    private String jenis_kelamin;
    private String alamat;
    private String nomor_telepon;
}
