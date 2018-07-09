package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.ProyekMapper;
import com.pusilkom.hris.model.ProyekModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
public class ProyekServiceImpl implements ProyekService  {

	@Autowired
	ProyekMapper proyekMapper;

	public List<ProyekModel> getProyekByPeriode(int idPeriode) {
		return proyekMapper.selectProyekByPeriode(idPeriode);
	}
	
}
