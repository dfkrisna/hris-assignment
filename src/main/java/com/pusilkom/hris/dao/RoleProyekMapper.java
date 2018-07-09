package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.RoleProyekModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleProyekMapper {
    @Select("select id, nama from mpp.\"ROLE_PROYEK\";")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "namaRole", column = "nama")
    })
    List<RoleProyekModel> selectAllRoles();

    @Insert("insert into mpp.\"ROLE_PENGGUNA\" (id_pengguna, id_role) " +
            "values (#{id_pengguna}, #{id_role})")
    void addRolePengguna(@Param("id_pengguna") String id_pengguna,
                         @Param("id_role") String id_role);

    @Select("select nama from mpp.\"ROLE_PROYEK\" where id=#{id_role};")
    String selectRoleNameByID(@Param("id_role") int idRole);

    @Select("select id, nama from mpp.\"ROLE_PROYEK\" where nama=#{roleName};")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "namaRole", column = "nama")
    })
    RoleProyekModel selectRoleByName(String roleName);
}
