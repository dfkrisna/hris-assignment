package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.KaryawanProyekMapper;
import com.pusilkom.hris.dao.ProyekMapper;
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
public class KaryawanProyekServiceImpl implements KaryawanProyekService{

	@Autowired
	KaryawanProyekMapper karyawanProyekMapper;

	public List<KaryawanProyekModel> getKaryawanProyekByProyek(int idProyek, int idPeriode) {
		return karyawanProyekMapper.selectKaryawanProyekByProyekPeriode(idProyek, idPeriode);
	}

	public List<KaryawanProyekModel> getKaryawanProyekByProyek(List<ProyekModel> listProyek, int idPeriode) {
		List<KaryawanProyekModel> karyawanProyekModelList = new ArrayList<KaryawanProyekModel>();

		for(ProyekModel proyek : listProyek) {
			karyawanProyekModelList.addAll(karyawanProyekMapper.selectKaryawanProyekByProyekPeriode(proyek.getId(), idPeriode));
		}
		return karyawanProyekModelList;
	}

	public List<KaryawanProyekModel> getKaryawanProyekByPeriode(int idPeriode) {
		return karyawanProyekMapper.selectKaryawanProyekByPeriode(idPeriode);
	}

	public List<List<KaryawanProyekModel>> getKaryawanMapping (List<ProyekModel> proyekList, List<KaryawanModel> karyawanList, List<KaryawanProyekModel> karyawanProyekList) {
		List<List<KaryawanProyekModel>> karyawanMapping = new ArrayList<List<KaryawanProyekModel>>();

		for (KaryawanModel karyawan : karyawanList) {
			List<KaryawanProyekModel> proyekKaryawan = new ArrayList<KaryawanProyekModel>();

			for(ProyekModel proyek : proyekList) {
				int size = proyekKaryawan.size();

				for (KaryawanProyekModel karyawanProyek : karyawanProyekList) {
					if(karyawanProyek.getIdKaryawan() == karyawan.getId() && karyawanProyek.getIdProyek() == proyek.getId()) {
						proyekKaryawan.add(karyawanProyek);
					}
				}

				if (size == proyekKaryawan.size()) {
					proyekKaryawan.add(new KaryawanProyekModel());
				}
			}

		}

		return karyawanMapping;
	}



}
