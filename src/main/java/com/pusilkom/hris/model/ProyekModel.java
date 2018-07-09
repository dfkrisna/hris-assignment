package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProyekModel {
    private int id;
    private String kode;
    private String namaProyek;
    private String namaKlien;
    private String keterangan;
    private int idPmo;
    private int idParent;
    private int idProjectLead;
    private int idStartPeriod;
    private int idEndPeriod;
}
