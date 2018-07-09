package com.pusilkom.hris.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenggunaModel {
    private int id;
    private String username;
    private String password;
    private String nama;
    private String id_role;
    private String role;
    private List<Roles> roles;
    private String id_pegawai_temp;
    private int id_pegawai;
    private String alamat;
    private String nomor_telepon;
    private String jenis_kelamin;
}
