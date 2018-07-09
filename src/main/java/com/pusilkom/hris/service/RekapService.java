package com.pusilkom.hris.service;

import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.KaryawanRekapModel;
import com.pusilkom.hris.model.RekapModel;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.List;

public interface RekapService {

	RekapModel getRekapBulanan(int idKaryawanProyek, LocalDate periode);

	List<RekapModel> getRekapByPeriode(LocalDate periode);

	double getKaryawanKontribusi(int idKaryawan, LocalDate periode);

	List<RekapModel> getRekapPenilaianMandiri(int idKaryawan);

	void addRekapMatrix(Integer idKaryawan, Integer idProyek, double persenKontribusi);

	void addRekapMatrix(RekapModel rekap, KaryawanProyekModel karyawanProyek);

	void populatePrevRekap(LocalDate prevPeriode, LocalDate periode);

	void updateRekap(RekapModel retrievedRekap);

	List<RekapModel> selectRekapByIdKaryawanProyek(int idKaryawanProyek);

	void updatePenilaianMandiri(RekapModel rekap);

	RekapModel selectRekapById(int id);

	List<RekapModel> getRekapBulananKaryawan(LocalDate periodeDate, int idKaryawan);

	String[] getSixPeriod(LocalDate periode);
}
