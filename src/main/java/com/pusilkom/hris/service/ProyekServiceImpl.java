package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.*;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.PenugasanModel;
import com.pusilkom.hris.model.ProyekModel;
import com.pusilkom.hris.model.RatingFeedbackModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class ProyekServiceImpl implements ProyekService {

	@Autowired
	ProyekMapper proyekMapper;

	@Autowired
	PenugasanMapper penugasanMapper;

	@Autowired
	KaryawanProyekService karyawanProyekService;

	@Autowired
	RoleProyekService roleProyekService;

	public ProyekModel getProyekById(int idProyek) {
		return proyekMapper.selectProyekById(idProyek);
	}

	public List<ProyekModel> getProyekByKaryawanProyekList(List<KaryawanProyekModel> karyawanProyekList) {
		List<ProyekModel> proyekList = new ArrayList<ProyekModel>();
		for(KaryawanProyekModel karyawanProyek : karyawanProyekList) {
			proyekList.add(proyekMapper.selectProyekById(karyawanProyek.getIdProyek()));
		}
		return proyekList;
	}

	public List<ProyekModel> getProyekByPeriode(LocalDate periode) {
		return proyekMapper.selectProyekByPeriode(periode);
	}
	public void addProyek(ProyekModel proyek) {
		LocalDate start = proyek.getStartPeriode();

		if(start != null) {
			proyekMapper.addProyekNullEnd(proyek);
		} else {
			proyekMapper.addProyekNullBoth(proyek);
		}
	}
    public void updateProyek(ProyekModel proyek) {
	    proyekMapper.updateProyek(proyek);
	}
    public List<ProyekModel> selectProyekByPeriodePmo(LocalDate periode) { return proyekMapper.selectProyekByPeriodePmo(periode);}

	public ProyekModel selectProyekByIdPmo(int idProyek) {
		return proyekMapper.selectProyekByIdPmo(idProyek);
	}

	public void addProyekStatus (Integer idProyek, Integer idStatus) { proyekMapper.addProyekStatus(idProyek,idStatus, LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1));}
	public ProyekModel getProyekByKode(String kode) {
		return proyekMapper.selectProyekByKode(kode);
	}
	public List<PenugasanModel> selectProyekDipimpin(Integer idKaryawan) {

		List<PenugasanModel> listpenugasan = proyekMapper.selectProyekDipimpin(idKaryawan);


		return assignLatestStatus(listpenugasan);
	}

	public List<PenugasanModel> assignLatestStatus(List<PenugasanModel> penugasanList) {
		for(PenugasanModel penugasan : penugasanList) {
			penugasan.setStatusProyek(penugasanMapper.selectLatestStatus(penugasan.getIdProyek()));
		}

		return penugasanList;
	}
	@Override
	public boolean hasProjectLead(int idProyek) {
		ProyekModel proyek = proyekMapper.selectProyekById(idProyek);

		if(proyek.getIdProjectLead() != null) {
			return true;
		} else {
			List<KaryawanProyekModel> karyawanProyekModels = karyawanProyekService.getKaryawanProyekByProyek(idProyek);
			for (KaryawanProyekModel karyawanProyek : karyawanProyekModels) {
				String role = roleProyekService.getRoleNameByID(karyawanProyek.getIdRole());
				if(role != null && role.equalsIgnoreCase("project lead")) {
					return true;
				}
			}
			return false;
		}
	}

	public List<ProyekModel> selectProyekByIdParent(Integer idProyek) {
		return proyekMapper.selectProyekByIdParent(idProyek);
	}

}
