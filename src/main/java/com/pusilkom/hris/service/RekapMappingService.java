package com.pusilkom.hris.service;

import com.pusilkom.hris.model.*;

import java.time.LocalDate;
import java.util.List;

public interface RekapMappingService {

    List<KaryawanRekapModel> mapRekap(List<KaryawanModel> karyawanList, List<ProyekModel> proyekList, List<KaryawanProyekModel> karyawanProyekList, List<RekapModel> rekapList);

    int[] chartValue(List<KaryawanRekapModel> mapping);

    int totalPercentage(List<KaryawanRekapModel> mapping);

    String getCurrentDate();

    List<KaryawanRekapModel> mapRekapAssignment(List<KaryawanModel> karyawanList, List<ProyekModel> proyekList, List<KaryawanProyekModel> karyawanProyekList, List<RekapModel> rekapList, int idProyek);

    KaryawanRekapModel getRekapBulananKaryawan(LocalDate periodeDate, int idKaryawan);

    KaryawanRekapModel getRekapBulananKaryawanProyek(int idKaryawan, int idProyek);
}
