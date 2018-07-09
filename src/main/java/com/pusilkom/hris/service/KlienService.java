package com.pusilkom.hris.service;

import com.pusilkom.hris.model.DivisiModel;
import com.pusilkom.hris.model.KlienModel;

import java.util.List;

public interface KlienService {
    KlienModel selectKlienByID(Integer idKlien);
    List<KlienModel> selectAllKlien();
}
