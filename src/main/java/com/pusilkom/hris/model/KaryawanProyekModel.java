package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KaryawanProyekModel {
    private int id;
    private int idKaryawan;
    private int idProyek;
    private int idStartPeriod;
    private int idEndPeriod;
    private String timestampInisiasi;
    private int idRole;

}
