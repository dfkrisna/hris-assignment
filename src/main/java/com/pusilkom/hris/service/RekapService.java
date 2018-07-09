package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.RekapModel;

import java.util.List;

public interface RekapService {

	RekapModel getRekapBulanan(int idKaryawanProyek, int idPeriode);

	List<RekapModel> getRekapByPeriode(int idPeriode);

}
