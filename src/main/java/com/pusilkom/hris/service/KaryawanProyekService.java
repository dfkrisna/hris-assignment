package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.ProyekModel;

import java.util.List;

public interface KaryawanProyekService {

	List<KaryawanProyekModel> getKaryawanProyekByProyek(int idProyek, int idPeriode);

	List<KaryawanProyekModel> getKaryawanProyekByProyek(List<ProyekModel> proyekList, int idPeriode);

	List<KaryawanProyekModel> getKaryawanProyekByPeriode(int idPeriode);

	List<List<KaryawanProyekModel>> getKaryawanMapping (List<ProyekModel> proyekList, List<KaryawanModel> karyawanList, List<KaryawanProyekModel> karyawanProyekList);

}
