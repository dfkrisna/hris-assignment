package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.PenugasanMapper;
import com.pusilkom.hris.model.KaryawanAnggotaModel;
import com.pusilkom.hris.model.PenugasanModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Primary
public class PenugasanServiceImpl implements PenugasanService {

	@Autowired
    PenugasanMapper penugasanMapper;

	public List<PenugasanModel> getPenugasanList(int idKaryawan) {
		List<PenugasanModel> penugasanList = penugasanMapper.selectPenugasanByKaryawan(idKaryawan);

		for(PenugasanModel penugasan : penugasanList) {
			Integer avgRating = penugasanMapper.selectAvgRating(penugasan.getIdProyek(), idKaryawan);
			if(avgRating == null) {
				penugasan.setRating(0);
			} else {
				penugasan.setRating(avgRating);
			}

		}

		return assignLatestStatus(penugasanList);
	}

	public List<PenugasanModel> assignLatestStatus(List<PenugasanModel> penugasanList) {
		for(PenugasanModel penugasan : penugasanList) {
			penugasan.setStatusProyek(penugasanMapper.selectLatestStatus(penugasan.getIdProyek()));
		}

		return penugasanList;
	}

	public int getAverageRating(List<PenugasanModel> penugasanList) {
		if (penugasanList.size() == 0) {
			return 0;
		} else {
			int totalRating = 0;
			for (PenugasanModel penugasan : penugasanList) {
				totalRating += penugasan.getRating();
			}

			return (int) totalRating/ penugasanList.size();
		}
	}

	public List<PenugasanModel> getRiwayatPenugasanKaryawan(int idKaryawan){
		return penugasanMapper.getRiwayatPenugasanKaryawan(idKaryawan);
	}

	public PenugasanModel getDetailPenugasanById(int idProyek, int idKaryawan){
		return penugasanMapper.getDetailPenugasanById(idProyek, idKaryawan);
	}

	public List<PenugasanModel> getPenugasanPeriodeIni(int idKaryawan, LocalDate periode){
		return penugasanMapper.getPenugasanPeriodeIni(idKaryawan, periode);
	}

	public List<KaryawanAnggotaModel> getAnggotaProyek(int idProyek) {
		List<KaryawanAnggotaModel> anggotaList = penugasanMapper.selectAnggotaProyek(idProyek);
		for(KaryawanAnggotaModel anggota : anggotaList) {
			Integer avgRating = penugasanMapper.selectAvgRating(idProyek, anggota.getIdKaryawan());
			if (avgRating == null) {
				anggota.setRating(0);
			} else {
				anggota.setRating(avgRating);
			}
		}

		return anggotaList;
	}

	public String getLatestStatus(int idProyek) {
		return penugasanMapper.selectLatestStatus(idProyek);
	}

	@Override
	public void addStatusProyek(String statusAktif, Integer idProyek, LocalDate startPeriode) {
		int idStatus = penugasanMapper.getIDStatus(statusAktif);
		penugasanMapper.insertStatusProyek(idStatus, idProyek, startPeriode);
	}


	public List<PenugasanModel> getListPenugasan( int idProyek) {
		return penugasanMapper.getListPenugasan(idProyek);
	}
}
