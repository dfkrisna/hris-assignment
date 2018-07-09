package com.pusilkom.hris.service;

import com.pusilkom.hris.model.PenggunaModel;
import com.pusilkom.hris.model.Roles;

import java.util.List;

public interface PenggunaService {

	void addPengguna(PenggunaModel pengguna);

	void deleteAllRolePengguna(int id_pengguna);

	void addRoleBaru(int id_pengguna, List<String> roleBaru);

	void updateNamaPengguna(int id_pengguna, String id_pegawai, String nama_baru);

	void unassignAllRole(int id_pengguna);

	void deletePengguna(int id_pengguna);

	List<PenggunaModel> getListPengguna();

	List<Roles> getUnchosenRoles(int id);

	List<String> getPenggunaStringRoleByUsername(String username);

	PenggunaModel getPengguna(int idPengguna);

	PenggunaModel getPenggunaLama(String username);

	String compareRole(List<String> roleSekarang, List<String> RoleBaru);

	String getIdByUsername(String username);

	String getRoleByUsername(String username);

	/*List<PenggunaModel> getPenggunaByNama(String nama);

	List<PenggunaModel> getPenggunaByRole(String role);

	List<PenggunaModel> getPenggunaByNamaDanRole(String nama, String role);*/
}
