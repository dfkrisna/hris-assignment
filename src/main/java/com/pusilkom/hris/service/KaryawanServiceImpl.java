package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.KaryawanMapper;
import com.pusilkom.hris.dao.KaryawanProyekMapper;
import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class KaryawanServiceImpl implements KaryawanService{

	@Autowired
	KaryawanMapper karyawanMapper;

	public List<KaryawanModel> getKaryawanAll() {
		return karyawanMapper.selectKaryawanAll();
	}

	public KaryawanModel selectKaryawanById(int idKaryawan) {
		return karyawanMapper.selectKaryawanById(idKaryawan);
	}

	public List<KaryawanModel> getKaryawanByKaryawanProyek(List<KaryawanProyekModel> karyawanProyekList) {
		List<KaryawanModel> karyawanList = new ArrayList<KaryawanModel>();

		for(KaryawanProyekModel karyawanProyek : karyawanProyekList) {
			KaryawanModel karyawan = karyawanMapper.selectKaryawanById(karyawanProyek.getIdKaryawan());
			if(!karyawanList.contains(karyawan)) {
				karyawanList.add(karyawanMapper.selectKaryawanById(karyawanProyek.getIdKaryawan()));
			}
		}

		return karyawanList;
	}

	public List<KaryawanModel> getKaryawanByPeriode(int idPeriode) {
		return karyawanMapper.selectKaryawanByPeriode(idPeriode);
	}

}
