package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.RekapMapper;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.KaryawanRekapModel;
import com.pusilkom.hris.model.RekapModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class RekapServiceImpl implements RekapService {

	@Autowired
	RekapMapper rekapMapper;

	@Autowired
	KaryawanProyekService karyawanProyekService;

	public RekapModel getRekapBulanan(int idKaryawan, LocalDate periode) {
		return rekapMapper.selectRekapByKarPoPer(idKaryawan, periode);
	}

	public List<RekapModel> getRekapByPeriode(LocalDate periode) {
		log.info("Select rekap by periode {}", periode);
		return rekapMapper.selectRekapByPeriode(periode);
	}

	public double getKaryawanKontribusi(int idKaryawan, LocalDate periode) {
		Double result = rekapMapper.getKaryawanKontribusi(idKaryawan, periode);

		if(result == null) {
			return 0;
		} else {
			return result;
		}
	}

	public List<RekapModel> getRekapPenilaianMandiri(int idKaryawan) {
		return rekapMapper.getRekapPenilaianMandiri(idKaryawan);
	}

	@Override
	public void addRekapMatrix(RekapModel rekap, KaryawanProyekModel karyawanProyek) {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//		String timestamp = karyawanProyek.getStartPeriode().format(formatter);

//
//		rekap.setPeriode(timestampInisiasi);


		rekap.setPeriode(karyawanProyek.getStartPeriode());

		rekapMapper.insertRekap(rekap);
	}

	@Override
	public void addRekapMatrix(Integer idKaryawan, Integer idProyek, double persenKontribusi) {

	}

	@Override
	public void populatePrevRekap(LocalDate prevPeriode, LocalDate periode) {
		List<RekapModel> prevRekaps = rekapMapper.selectRekapByPeriode(prevPeriode);
		List<RekapModel> populatePrevRekaps = new ArrayList<RekapModel>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate periodeNow = LocalDate.now();

		for (RekapModel prevRekap : prevRekaps) {
			RekapModel newRekap = prevRekap;
			newRekap.setPeriode(periode);
			System.out.println("id Proyek" + newRekap.getIdProyek());
			populatePrevRekaps.add(newRekap);
		}
		for (RekapModel populatePrevRekap : populatePrevRekaps) {
			//LocalDate datePeriode = LocalDate.parse(populatePrevRekap.getPeriode(), formatter);
			RekapModel retrievePrev = rekapMapper.selectRekapByKarPoPer(populatePrevRekap.getIdKaryawanProyek(), periode);
			if(retrievePrev == null) {
				rekapMapper.insertRekap(populatePrevRekap);
			}
			else {
				if(retrievePrev.getPersentaseKontribusi() - populatePrevRekap.getPersentaseKontribusi() != 0) {
					System.out.println("masuk ke condition");
					LocalDate endPeriode = karyawanProyekService.getKaryawanProyekById(populatePrevRekap.getIdKaryawanProyek()).getEndPeriode();
					if(endPeriode != null && endPeriode.compareTo(periode) < 0) {
						rekapMapper.deleteRekap(populatePrevRekap);
					}
					if(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1).compareTo(periode) < 0) {
						retrievePrev.setPersentaseKontribusi(populatePrevRekap.getPersentaseKontribusi());
						rekapMapper.updateRekap(retrievePrev);
					}
				}
			}
		}
	}

	@Override
	public void updateRekap(RekapModel retrievedRekap) {
		rekapMapper.updateRekap(retrievedRekap);
	}

	public List<RekapModel> selectRekapByIdKaryawanProyek(int idKaryawanProyek){
		return rekapMapper.selectRekapByIdKaryawanProyek(idKaryawanProyek);
	}

	public void updatePenilaianMandiri(RekapModel rekap){
		rekapMapper.updatePenilaianMandiri(rekap);
	}

	public RekapModel selectRekapById(int id){ return rekapMapper.selectRekapById(id);}

	public String[] getSixPeriod(LocalDate periode) {
		String[] periodeList = new String[6];
		LocalDate currPeriod;

		for(int i = 0;  i < 6; i++) {
			currPeriod = periode.minusMonths(i);
			periodeList[5-i] = currPeriod.getMonth() + " " + currPeriod.getYear();
		}

		return periodeList;
	}

	@Override
	public List<RekapModel> getRekapBulananKaryawan(LocalDate periodeDate, int idKaryawan) {
		return rekapMapper.selectRekapKaryawanByPeriode(periodeDate, idKaryawan);
	}
}
