package com.pusilkom.hris.util;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.RekapService;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MappingUtil {

    public List<KaryawanMappingModel> mapRekap(List<KaryawanModel> karyawanList, List<ProyekModel> proyekList, List<KaryawanProyekModel> karyawanProyekList, List<RekapModel> rekapList) {
        List<KaryawanMappingModel> mapping = new ArrayList<KaryawanMappingModel>();

        for (KaryawanModel karyawan : karyawanList) {
            KaryawanMappingModel karyawanMapping = new KaryawanMappingModel();
            List<RekapModel> karyawanRekap = new ArrayList<RekapModel>();

            for(ProyekModel proyek : proyekList) {
                int size = karyawanRekap.size();
                int idKaryawanProyek = 0;

                for(KaryawanProyekModel karyawanProyek : karyawanProyekList) {
                    if(karyawanProyek.getIdKaryawan() == karyawan.getId() && karyawanProyek.getIdProyek() == proyek.getId()) {
                        idKaryawanProyek = karyawanProyek.getId();
                        break;
                    }
                }

                for(RekapModel rekap : rekapList) {
                    if(rekap.getIdKaryawanProyek() == idKaryawanProyek) {
                        karyawanRekap.add(rekap);
                        break;
                    }
                }

                if(size == karyawanRekap.size()) {
                    karyawanRekap.add(new RekapModel());
                }
            }

            karyawanMapping.setKaryawan(karyawan);
            karyawanMapping.setRekapList(karyawanRekap);
            mapping.add(karyawanMapping);
        }

        return mapping;
    }

    public int[] chartValue(List<KaryawanMappingModel> mapping) {
        int onefifth = 0;
        int twofifth = 0;
        int threefifth = 0;
        int fourfifth = 0;
        int fivefifth = 0;

        int[] value = new int[5];

        for (KaryawanMappingModel karyawanMapping : mapping) {
            if (karyawanMapping.getTotal() <= 20) {
                value[0] += 1;
            } else if (karyawanMapping.getTotal() > 20 && karyawanMapping.getTotal() <= 40) {
                value[1] += 1;
            } else if (karyawanMapping.getTotal() > 40 && karyawanMapping.getTotal() <= 60) {
                value[2] += 1;
            } else if (karyawanMapping.getTotal() > 60 && karyawanMapping.getTotal() <= 80) {
                value[3] += 1;
            } else if (karyawanMapping.getTotal() > 80) {
                value[4] += 1;
            }
        }

        return value;
        //return ("" + onefifth + ", " + twofifth + ", " + threefifth + ", " + fourfifth + ", " + fivefifth);
    }
}
