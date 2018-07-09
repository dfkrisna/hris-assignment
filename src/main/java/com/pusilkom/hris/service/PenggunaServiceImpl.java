package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.*;
import com.pusilkom.hris.model.PenggunaModel;
import com.pusilkom.hris.model.Roles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@Service
@Primary
public class PenggunaServiceImpl implements PenggunaService {

	@Autowired
    PenggunaMapper penggunaMapper;

	@Autowired
    PegawaiMapper pegawaiMapper;

	@Autowired
	KaryawanMapper karyawanMapper;

	@Autowired
	ProyekMapper proyekMapper;

	@Autowired
	DivisiMapper divisiMapper;

	@Override
	public PenggunaModel getPengguna(int idPengguna) {
		PenggunaModel pengguna = penggunaMapper.getAPenggunaById(idPengguna);
		pengguna.setRoles(penggunaMapper.getPenggunaRoleById(idPengguna));
		pengguna.setAlamat(pegawaiMapper.getAlamatPengguna(pengguna.getId_pegawai()));
		pengguna.setNomor_telepon(pegawaiMapper.getTelpPengguna(pengguna.getId_pegawai()));
		pengguna.setJenis_kelamin(pegawaiMapper.getGenderPengguna(pengguna.getId_pegawai()));
		pengguna.setId_pegawai_temp(pengguna.getId_pegawai() + " " + pengguna.getNama());

		return pengguna;
	}

	@Override
	public PenggunaModel getPenggunaLama(String username)
	{
		return penggunaMapper.getAPenggunaByUsername(username);
	}

	@Override
	public void addPengguna(PenggunaModel pengguna)
	{
		StringTokenizer st = new StringTokenizer(pengguna.getId_pegawai_temp());

		String id = st.nextToken();
		String nama = st.nextToken();

		pengguna.setId_pegawai(Integer.parseInt(id));
		pengguna.setNama(nama);

		penggunaMapper.addPengguna(pengguna);
		PenggunaModel newPengguna = penggunaMapper.getAPenggunaByNama(pengguna.getNama());
		penggunaMapper.firstAssignedRole(newPengguna.getId());
	}

	@Override
	public void unassignAllRole(int id_pengguna)
	{
		penggunaMapper.firstAssignedRole(id_pengguna);
	}

	@Override
	public void deletePengguna(int id_pengguna)
	{
		penggunaMapper.deletePengguna(id_pengguna);
	}

	@Override
	public void updateNamaPengguna(int id_pengguna, String id_pegawai, String nama_baru)
	{
		String[] st = id_pegawai.split(":");

		int id = Integer.parseInt(st[0]);
		penggunaMapper.updateNamaPengguna(id_pengguna, id, nama_baru);
	}

	@Override
	public void deleteAllRolePengguna(int id_pengguna)
	{
		System.out.println(id_pengguna);
		penggunaMapper.deleteAllRolePengguna(id_pengguna);
	}

	@Override
	public void addRoleBaru(int id_pengguna, List<String> roleBaru)
	{
		int id_role;
		System.out.println(roleBaru);
		for(String role : roleBaru) {
			id_role = penggunaMapper.getIdRole(role);
			penggunaMapper.addRoleToPengguna(id_pengguna, id_role);
		}
	}

	@Override
	public List<PenggunaModel> getListPengguna() {
		return penggunaMapper.getAllPenggunaWithRole();
	}

	@Override
	public List<Roles> getUnchosenRoles(int id) {
		return penggunaMapper.getUnchosenRoles(id);
	}

	@Override
	public List<String> getPenggunaStringRoleByUsername(String username) {
		return penggunaMapper.getPenggunaStringRoleByUsername(username);
	}


	@Override
	public String compareRole(List<String> roleSekarang, List<String> roleBaru)
	{
		int duplicateRole = 0;
		if(roleBaru.size() == 0 && roleSekarang.size() == 0) {
			return "sama";

		}else if(roleBaru.size() == 0 || roleSekarang.size() == 0) {
			return "beda";

		}else {
			for(int i = 0; i < roleSekarang.size(); i++) {
				for(int j = 0; j < roleBaru.size(); j++) {
					System.out.println(roleSekarang.get(i));
					System.out.println(roleBaru.get(j));
					if(roleBaru.get(j).equalsIgnoreCase(roleSekarang.get(i))) {
						duplicateRole++;
					}
				}
			}

			if(duplicateRole == 1 && roleBaru.size() > roleSekarang.size() ||
					duplicateRole == 1 && roleSekarang.size() > roleBaru.size()) {

				return "beda";

			}else if(duplicateRole < 1) {

				return "beda";

			}else {

				return "sama";
			}

		}

	}

	@Override
	public String getIdByUsername(String username) {
		return penggunaMapper.getIdByUsername(username);
	}


	/**@Override
	public List<PenggunaModel> getPenggunaByNama(String nama) {
		return penggunaMapper.getPenggunaByNama(nama);
	}

	@Override
	public List<PenggunaModel> getPenggunaByRole(String role) {
		return penggunaMapper.getPenggunaByRole(role);
	}

	@Override
	public List<PenggunaModel> getPenggunaByNamaDanRole(String nama, String role) {
		return penggunaMapper.getPenggunaByNamaDanRole(nama, role);
	}*/

	public String getRoleByUsername(String username) {
		return penggunaMapper.getRoleByUsername(username);
	}
}
