package com.pusilkom.hris.service;

import com.pusilkom.hris.dao.DivisiMapper;
import com.pusilkom.hris.dao.KlienMapper;
import com.pusilkom.hris.model.DivisiModel;
import com.pusilkom.hris.model.KlienModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Primary
public class KlienServiceImpl implements KlienService {
    @Autowired
    KlienMapper klienMapper;

    @Override
    public KlienModel selectKlienByID(Integer idKlien) {
        return klienMapper.selectKlienByID(idKlien);
    }

    @Override
    public List<KlienModel> selectAllKlien(){ return klienMapper.selectAllKlien();}
}
