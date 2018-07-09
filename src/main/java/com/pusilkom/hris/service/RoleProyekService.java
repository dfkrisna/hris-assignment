package com.pusilkom.hris.service;

import com.pusilkom.hris.model.RoleProyekModel;

import java.util.List;

public interface RoleProyekService {
    List<RoleProyekModel> getAllRoleProyek();

    String getRoleNameByID(int id);

    RoleProyekModel getRoleByName(String namaRole);
}
