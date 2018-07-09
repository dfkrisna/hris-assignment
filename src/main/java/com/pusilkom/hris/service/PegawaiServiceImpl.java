package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.PegawaiMapper;
import com.pusilkom.hris.model.PegawaiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
public class PegawaiServiceImpl implements PegawaiService {

	@Autowired
    PegawaiMapper pegawaiMapper;

	@Override
	public List<PegawaiModel> getAllAvailablePegawai() {
		return pegawaiMapper.getAllAvailablePegawai();
	}

	@Override
	public String getAlamatPegawai(int id) {
		return pegawaiMapper.getAlamatPengguna(id);
	}

	@Override
	public String getTelpPegawai(int id) {
		return pegawaiMapper.getTelpPengguna(id);
	}

	@Override
	public String getGenderPegawai(int id) {
		return pegawaiMapper.getGenderPengguna(id);
	}


}
