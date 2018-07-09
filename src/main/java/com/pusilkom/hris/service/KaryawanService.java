package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;

import java.util.List;

public interface KaryawanService {

	List<KaryawanModel> getKaryawanAll();

	List<KaryawanModel> getKaryawanByKaryawanProyek(List<KaryawanProyekModel> karyawanProyekList);

	List<KaryawanModel> getKaryawanByPeriode(int idPeriode);

}
