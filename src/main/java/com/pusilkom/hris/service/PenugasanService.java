package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanAnggotaModel;
import com.pusilkom.hris.model.PenugasanModel;

import java.time.LocalDate;
import java.util.List;

public interface PenugasanService {

	List<PenugasanModel> getPenugasanList(int idKaryawan);

	List<PenugasanModel> assignLatestStatus(List<PenugasanModel> penugasanList);

	int getAverageRating(List<PenugasanModel> penugasanList);

	List<PenugasanModel> getRiwayatPenugasanKaryawan(int idKaryawan);

	PenugasanModel getDetailPenugasanById(int idProyek, int idKaryawan);

	List<PenugasanModel> getPenugasanPeriodeIni(int idKaryawan, LocalDate periode);

	List<KaryawanAnggotaModel> getAnggotaProyek(int idProyek);

	String getLatestStatus(int idProyek);

	void addStatusProyek(String aktif, Integer idProyek, LocalDate startPeriode);

	List<PenugasanModel> getListPenugasan( int idProyek);
}
