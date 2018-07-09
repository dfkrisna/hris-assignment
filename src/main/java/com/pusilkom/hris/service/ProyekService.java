package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.PenugasanModel;
import com.pusilkom.hris.model.ProyekModel;
import com.pusilkom.hris.model.RatingFeedbackModel;

import java.time.LocalDate;
import java.util.List;

public interface ProyekService {

	void addProyek(ProyekModel proyek);
	void updateProyek(ProyekModel proyek);
	ProyekModel getProyekById(int idProyek);

	List<ProyekModel> getProyekByKaryawanProyekList(List<KaryawanProyekModel> karyawanProyekList);
	List<ProyekModel> getProyekByPeriode(LocalDate periode);

	List<ProyekModel> selectProyekByPeriodePmo(LocalDate periode);
	ProyekModel selectProyekByIdPmo(int idProyek);
	void addProyekStatus(Integer idProyek, Integer idStatus);
	ProyekModel getProyekByKode(String kodeProyek);

	List<PenugasanModel> selectProyekDipimpin(Integer idKaryawan);
	boolean hasProjectLead(int idProyek);

	List<ProyekModel> selectProyekByIdParent(Integer idProyek);

}
