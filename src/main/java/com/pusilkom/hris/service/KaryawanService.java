package com.pusilkom.hris.service;

import com.pusilkom.hris.model.FeedbackRatingModel;
import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import org.apache.tomcat.jni.Local;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface KaryawanService {

	String cekPenugasanKaryawanById(int idKaryawan);

	int getKaryawanIdByUsername(String username);

	int getKaryawanIdPenilai(int idKaryawan, int idProyek);

	KaryawanModel getKaryawanById(int idKaryawan);

	KaryawanModel getKaryawanByIdPengguna(int idPengguna);

	String verifyFeedbackRekan(int idRekan, int idPenilai, int idProyek, LocalDate periode);

	List<KaryawanModel> getKaryawanAll();

	List<KaryawanModel> getKaryawanByKaryawanProyek(List<KaryawanProyekModel> karyawanProyekList);

	List<KaryawanModel> getKaryawanByPeriode(LocalDate periode);

	KaryawanModel selectKaryawanByEmail(String email);

	List<KaryawanModel> getKaryawanByDivisi(int idDivisi);

	List<Integer> getUserProyek(int idKaryawan);

	List<FeedbackRatingModel> getRekanSeproyekFeedback(List<Integer> idProyeks, int idKaryawan, LocalDate periodeNow);

	List<FeedbackRatingModel> getRekanSeproyek(List<Integer> idProyeks, int idKaryawan, LocalDate periodeNow);

	void addFeedbackRekan(String feedback, int rating, int idRekan, int idPenilai,
						  int idProyek, LocalDate periode, Timestamp tanggal);

	void updateFeedbackRekan(String feedback, int rating, int idRekan, int idPenilai,
							 int idProyek, LocalDate periode, Timestamp tanggal);


}
