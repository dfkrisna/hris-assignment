package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.KaryawanProyekMapper;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class KaryawanProyekServiceImpl implements KaryawanProyekService{

	@Autowired
    KaryawanProyekMapper karyawanProyekMapper;

	public List<KaryawanProyekModel> getKaryawanProyekByProyek(int idProyek, LocalDate periode) {
		return karyawanProyekMapper.selectKaryawanProyekByProyekPeriode(idProyek, periode);
	}

	public List<KaryawanProyekModel> getKaryawanProyekByProyek(List<ProyekModel> listProyek, LocalDate periode) {
		List<KaryawanProyekModel> karyawanProyekModelList = new ArrayList<KaryawanProyekModel>();

		for(ProyekModel proyek : listProyek) {
			karyawanProyekModelList.addAll(karyawanProyekMapper.selectKaryawanProyekByProyekPeriode(proyek.getId(), periode));
		}
		return karyawanProyekModelList;
	}

	public List<KaryawanProyekModel> getKaryawanProyekByPeriode(LocalDate periode) {
		return karyawanProyekMapper.selectKaryawanProyekByPeriode(periode);
	}

	public List<KaryawanProyekModel> getKaryawanProyekByKaryawan(int idKaryawan) {
		return karyawanProyekMapper.selectKaryawanProyekByKaryawan(idKaryawan);
	}

	@Override
	public KaryawanProyekModel getKaryawanProyekByKaryawanandProyek(int idKaryawan, int idProyek) {
		return karyawanProyekMapper.selectKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);
	}

	@Override
	public void addKaryawanProyekMatrix(KaryawanProyekModel karyawanProyek, Integer idKaryawan, Integer idProyek, int idRole) {
		if(karyawanProyek.getEndPeriode()!= null) {
			if(karyawanProyek.getStartPeriode().compareTo(karyawanProyek.getEndPeriode()) >= 0) {
				karyawanProyek.setEndPeriode(null);
			}
		}

		String timestampInisiasi = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new java.util.Date());

		karyawanProyek.setIdKaryawan(idKaryawan);
		karyawanProyek.setIdProyek(idProyek);
		karyawanProyek.setTimestampInisiasi(timestampInisiasi);
		karyawanProyek.setIdRole(idRole);

		karyawanProyekMapper.insertKaryawanProyek(karyawanProyek);
	}

	@Override
	public KaryawanProyekModel getKaryawanProyekById(Integer idKaryawanProyek) {
		return karyawanProyekMapper.selectKaryawanProyekById(idKaryawanProyek);
	}

	@Override
	public void updateKaryawanProyek(KaryawanProyekModel karyawanProyek) {
		karyawanProyekMapper.updateKaryawanProyek(karyawanProyek);
	}

	@Override
	public List<KaryawanProyekModel> getKaryawanProyekByProyek(Integer idProyek) {
		return karyawanProyekMapper.selectKaryawanProyekByProyek(idProyek);
	}
	public List<KaryawanProyekModel> selectKaryawanProyekByKaryawanPeriode (Integer idKaryawan, LocalDate periode) {
		return karyawanProyekMapper.selectKaryawanProyekByKaryawanPeriode(idKaryawan,periode);
	}

}
