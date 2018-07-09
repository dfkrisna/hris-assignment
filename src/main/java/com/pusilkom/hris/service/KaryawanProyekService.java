package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;

import java.time.LocalDate;
import java.util.List;

public interface KaryawanProyekService {

	List<KaryawanProyekModel> getKaryawanProyekByProyek(int idProyek, LocalDate periode);

	List<KaryawanProyekModel> getKaryawanProyekByProyek(List<ProyekModel> proyekList, LocalDate periode);

	List<KaryawanProyekModel> getKaryawanProyekByPeriode(LocalDate periode);

	List<KaryawanProyekModel> getKaryawanProyekByKaryawan(int idKaryawan);

	KaryawanProyekModel getKaryawanProyekByKaryawanandProyek(int idKaryawan, int idProyek);

	void addKaryawanProyekMatrix(KaryawanProyekModel karyawanProyek, Integer idKaryawan, Integer idProyek, int idRole);

    KaryawanProyekModel getKaryawanProyekById(Integer idKaryawanProyek);

    void updateKaryawanProyek(KaryawanProyekModel karyawanProyek);

    List<KaryawanProyekModel> getKaryawanProyekByProyek(Integer idProyek);
	List<KaryawanProyekModel> selectKaryawanProyekByKaryawanPeriode (Integer idKaryawan, LocalDate periode);

}
