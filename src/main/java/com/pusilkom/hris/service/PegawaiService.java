package com.pusilkom.hris.service;

import com.pusilkom.hris.model.PegawaiModel;

import java.util.List;

public interface PegawaiService {

	List<PegawaiModel> getAllAvailablePegawai();

	String getAlamatPegawai(int id);

	String getTelpPegawai(int id);

	String getGenderPegawai(int id);


}
