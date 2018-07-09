package com.pusilkom.hris.dao;

import com.pusilkom.hris.model.PenggunaModel;
import com.pusilkom.hris.model.Roles;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PenggunaMapper {

    @Select("Select p.id, p.username, p.nama, string_agg(r.alias,', ') as role " +
            "from " +
            "mpp.\"PENGGUNA\" as p, " +
            "mpp.\"ROLE_PENGGUNA\" as rp, " +
            "mpp.\"ROLE\" as r " +
            "where p.id = rp.id_pengguna and rp.id_role = r.id and p.enabled = 1 group by p.id, " +
            "p.nama, p.username order by p.id asc")
    List<PenggunaModel> getAllPenggunaWithRole();

    @Select("select * from mpp.\"PENGGUNA\" where nama = #{nama}")
    PenggunaModel getAPenggunaByNama(@Param("nama") String nama);

    @Select("select * from mpp.\"PENGGUNA\" where id = #{id}")
    PenggunaModel getAPenggunaById(@Param("id") int id);

    @Select("select * from mpp.\"PENGGUNA\" where username = #{username}")
    PenggunaModel getAPenggunaByUsername(@Param("username") String username);

    @Select("select r.id, alias from mpp.\"PENGGUNA\" as p, mpp.\"ROLE_PENGGUNA\" as rp, " +
            "mpp.\"ROLE\" as r where p.id = #{id} and p.id = rp.id_pengguna and rp.id_role = r.id")
    List<Roles> getPenggunaRoleById(@Param("id") int id);

    @Select("select alias from mpp.\"PENGGUNA\" as p, mpp.\"ROLE_PENGGUNA\" as rp, " +
            "mpp.\"ROLE\" as r where p.username = #{username} and p.id = rp.id_pengguna and rp.id_role = r.id")
    List<String> getPenggunaStringRoleByUsername(@Param("username") String username);

    @Select("select id, alias from mpp.\"ROLE\" where nama_role like 'ROLE%'")
    List<Roles> getAllRole();

    @Select("select r.alias from mpp.\"ROLE\" as r " +
            "where not exists (select * from mpp.\"PENGGUNA\" as p, mpp.\"ROLE_PENGGUNA\" as rp " +
            "where p.id = #{idPengguna} and p.id = rp.id_pengguna and rp.id_role = r.id) and r.nama_role like 'ROLE%';")
    List<Roles> getUnchosenRoles(@Param("idPengguna") int idPengguna);

    @Select("select id from mpp.\"PENGGUNA\" where username = #{username}")
    String getIdByUsername(@Param("username") String username);

    @Insert("insert into mpp.\"PENGGUNA\" (nama, username, password, enabled, id_pegawai) values " +
            "(#{nama}, #{username}, #{password}, 1, #{id_pegawai})")
    void addPengguna (PenggunaModel pengguna);

    @Insert("insert into mpp.\"ROLE_PENGGUNA\" (id_pengguna, id_role) values (#{id_pengguna}, 6)")
    void firstAssignedRole(@Param("id_pengguna") int id_pengguna);

    @Insert("insert into mpp.\"ROLE_PENGGUNA\" (id_pengguna, id_role) values (#{id_pengguna}, #{id_role})")
    void addRoleToPengguna(@Param("id_pengguna") int id_pengguna,
                           @Param("id_role") int id_role);

    @Delete("delete from mpp.\"ROLE_PENGGUNA\" where id_pengguna = #{id_pengguna}")
    void deleteAllRolePengguna(@Param("id_pengguna") int id_pengguna);

    @Update("update mpp.\"PENGGUNA\" set id_pegawai = #{id_pegawai}, nama = #{nama_baru} where id = #{id_pengguna}")
    void updateNamaPengguna(@Param("id_pengguna") int id_pengguna,
                            @Param("id_pegawai") int id_pegawai,
                            @Param("nama_baru") String nama_baru);


    @Select("select id from mpp.\"ROLE\" where lower(alias) = lower(#{alias})")
    int getIdRole(@Param("alias") String alias);


    @Select("SELECT R.nama_role\n" +
            "FROM mpp.\"PENGGUNA\" AS PG, mpp.\"ROLE\" AS R, mpp.\"ROLE_PENGGUNA\" AS RP\n" +
            "WHERE PG.username = '${username}'\n" +
            "\tAND PG.id = RP.id_pengguna\n" +
            "\tAND R.id = RP.id_role;")
    String getRoleByUsername(@Param("username") String username);

    @Update("update mpp.\"PENGGUNA\" set enabled = 0 where id = #{id}")
    void deletePengguna(@Param("id") int id);
}
