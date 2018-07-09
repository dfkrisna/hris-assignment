package com.pusilkom.hris.service;

import com.pusilkom.hris.model.ProyekModel;

import java.util.List;

public interface ProyekService {

	List<ProyekModel> getProyekByPeriode(int idPeriode);

}
