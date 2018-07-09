package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.RoleProyekMapper;
import com.pusilkom.hris.model.RoleProyekModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
public class RoleProyekServiceImpl implements RoleProyekService {
    @Autowired
    RoleProyekMapper roleProyekMapper;

    @Override
    public List<RoleProyekModel> getAllRoleProyek() {
        return roleProyekMapper.selectAllRoles();
    }

    @Override
    public String getRoleNameByID(int id) {
        return roleProyekMapper.selectRoleNameByID(id);
    }

    @Override
    public RoleProyekModel getRoleByName(String roleName) {
        return roleProyekMapper.selectRoleByName(roleName);
    }
}
