package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.DivisiMapper;
import com.pusilkom.hris.model.DivisiModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class DivisiServiceImpl implements DivisiService {
    @Autowired
    DivisiMapper divisiMapper;

    @Override
    public DivisiModel getDivisiByManajer(int idManajer) {
        return divisiMapper.selectDivisiByManajer(idManajer);
    }

    @Override
    public DivisiModel getDivisiByID(int idDivisi) {
        return divisiMapper.selectDivisiByID(idDivisi);
    }
}
