package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.KaryawanMapper;
import com.pusilkom.hris.model.FeedbackRatingModel;
import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class KaryawanServiceImpl implements KaryawanService{
	@Override
	public String cekPenugasanKaryawanById(int idKaryawan) {
		String hasil = "";
		List<String> listId = karyawanMapper.cekPenugasanKaryawanById(idKaryawan);

		if(listId.isEmpty()) {
			hasil= "no";
		}else {
			hasil= "yes";
		}

		return hasil;
	}

	@Autowired
    KaryawanMapper karyawanMapper;

	public List<KaryawanModel> getKaryawanAll() {
		return karyawanMapper.selectKaryawanAll();
	}

	public KaryawanModel getKaryawanById(int idKaryawan) {
		return karyawanMapper.selectKaryawanById(idKaryawan);
	}

	public KaryawanModel getKaryawanByIdPengguna(int idPengguna) {
		return karyawanMapper.selectKaryawanByIdPengguna(idPengguna);
	}

	@Override
	public String verifyFeedbackRekan(int idRekan, int idPenilai, int idProyek, LocalDate periode) {
		String hasil = "";
		FeedbackRatingModel feedbackRekan = karyawanMapper.verifyFeedbackRekan(idRekan, idPenilai, idProyek, periode);

		if(feedbackRekan == null) {
			hasil = "belum ada";
		}else {
			hasil = "sudah ada";
		}
		return hasil;
	}

	public List<KaryawanModel> getKaryawanByKaryawanProyek(List<KaryawanProyekModel> karyawanProyekList) {
		List<KaryawanModel> karyawanList = new ArrayList<KaryawanModel>();

		for(KaryawanProyekModel karyawanProyek : karyawanProyekList) {
			KaryawanModel karyawan = getKaryawanById(karyawanProyek.getIdKaryawan());
			if(!karyawanList.contains(karyawan)) {
				karyawanList.add(karyawanMapper.selectKaryawanById(karyawanProyek.getIdKaryawan()));
			}
		}

		return karyawanList;
	}

	public List<KaryawanModel> getKaryawanByPeriode(LocalDate periode) {
		return karyawanMapper.selectKaryawanByPeriode(periode);
	}

	public KaryawanModel selectKaryawanByEmail(String email){
		return karyawanMapper.selectKaryawanByEmail(email);
	}

	@Override
	public List<KaryawanModel> getKaryawanByDivisi(int idDivisi) {
		return karyawanMapper.selectKaryawanByDivisi(idDivisi);
	}

	@Override
	public int getKaryawanIdByUsername(String username) {
		return karyawanMapper.getKaryawanIdByUsername(username);
	}

	@Override
	public List<FeedbackRatingModel> getRekanSeproyekFeedback(List<Integer> idProyek, int idKaryawan, LocalDate periodeNow) {
		ArrayList<FeedbackRatingModel> hasil = new ArrayList<>();
		int idPenilai = 0;

		for (int i = 0; i < idProyek.size(); i++){
			idPenilai = karyawanMapper.getKaryawanIdPenilai(idKaryawan, idProyek.get(i));
			List<FeedbackRatingModel> rekan = karyawanMapper.getRekanSeproyekFeedback(idProyek.get(i), idKaryawan, periodeNow, idPenilai);

			if(rekan.size() >= 1) {
				for(int j = 0; j < rekan.size(); j++) {

					hasil.add(rekan.get(j));
				}
			}
		}
		return hasil;
	}

	@Override
	public List<FeedbackRatingModel> getRekanSeproyek(List<Integer> idProyek, int idKaryawan, LocalDate periodeNow) {

		ArrayList<FeedbackRatingModel> hasil = new ArrayList<>();

		for (int i = 0; i < idProyek.size(); i++){
			List<FeedbackRatingModel> rekan = karyawanMapper.getRekanSeproyek(idProyek.get(i), idKaryawan, periodeNow);

			if(rekan.size() >= 1) {
				for(int j = 0; j < rekan.size(); j++) {
					hasil.add(rekan.get(j));
				}
			}
		}
		return hasil;
	}

	@Override
	public void addFeedbackRekan(String feedback, int rating, int idRekan, int idPenilai, int idProyek, LocalDate periode, Timestamp tanggal)
	{
		karyawanMapper.addFeedbackRekan(feedback, rating, idRekan, idPenilai, idProyek, periode, tanggal);
	}

	@Override
	public void updateFeedbackRekan(String feedback, int rating, int idRekan, int idPenilai, int idProyek, LocalDate periode, Timestamp tanggal)
	{
		karyawanMapper.updateFeedbackRekan(feedback, rating, idRekan, idPenilai, idProyek, periode, tanggal);
	}

	@Override
	public int getKaryawanIdPenilai(int idKaryawan, int idProyek) {
		return karyawanMapper.getKaryawanIdPenilai(idKaryawan, idProyek);
	}

	@Override
	public List<Integer> getUserProyek(int idKaryawan) {
		List<Integer> listProyek = karyawanMapper.getUserProyek(idKaryawan);
		return listProyek;
	}


}
