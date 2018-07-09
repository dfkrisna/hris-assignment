package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.KaryawanMapper;
import com.pusilkom.hris.dao.RekapMapper;
import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.KaryawanProyekModel;
import com.pusilkom.hris.model.RekapModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Primary
public class RekapServiceImpl implements RekapService {

	@Autowired
	RekapMapper rekapMapper;

	public RekapModel getRekapBulanan(int idKaryawan, int idPeriode) {
		return rekapMapper.selectRekapByKarPoPer(idKaryawan, idPeriode);
	}

	public List<RekapModel> getRekapByPeriode(int idPeriode) {
		return rekapMapper.selectRekapByPeriode(idPeriode);
	}

}
