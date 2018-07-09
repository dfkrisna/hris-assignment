package com.pusilkom.hris.service;

import com.pusilkom.hris.model.DivisiModel;

public interface DivisiService {
    DivisiModel getDivisiByManajer(int idManajer);

    DivisiModel getDivisiByID(int idDivisi);
}
