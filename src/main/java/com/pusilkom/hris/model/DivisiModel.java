package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivisiModel {
    private int id;
    private String kode;
    private String namaDivisi;
    private int idManajer;
    private int idDivisiInduk;
}
