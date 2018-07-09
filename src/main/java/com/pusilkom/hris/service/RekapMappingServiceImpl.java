package com.pusilkom.hris.service;

import com.pusilkom.hris.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Primary
public class RekapMappingServiceImpl implements RekapMappingService {
    @Autowired
    PenugasanService penugasanService;

    @Autowired
    KaryawanService karyawanService;

    @Autowired
    RekapService rekapService;

    @Autowired
    KaryawanProyekService karyawanProyekService;

    public List<KaryawanRekapModel> mapRekap(List<KaryawanModel> karyawanList, List<ProyekModel> proyekList, List<KaryawanProyekModel> karyawanProyekList, List<RekapModel> rekapList) {
        List<KaryawanRekapModel> mapping = new ArrayList<KaryawanRekapModel>();

        for (KaryawanModel karyawan : karyawanList) {
            KaryawanRekapModel karyawanMapping = new KaryawanRekapModel();
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
                RekapModel rekapEmpty = new RekapModel();
                rekapEmpty.setIdKaryawan(karyawan.getId());
                rekapEmpty.setIdProyek(proyek.getId());
                if(size == karyawanRekap.size()) {
                    karyawanRekap.add(rekapEmpty);
                }
            }

            karyawanMapping.setKaryawan(karyawan);
            karyawanMapping.setRekapList(karyawanRekap);
            mapping.add(karyawanMapping);
        }
        Collections.sort(mapping);
        return mapping;
    }

    public int[] chartValue(List<KaryawanRekapModel> mapping) {
        int onefifth = 0;
        int twofifth = 0;
        int threefifth = 0;
        int fourfifth = 0;
        int fivefifth = 0;

        int[] value = new int[5];

        for (KaryawanRekapModel karyawanMapping : mapping) {
            int total = karyawanMapping.getTotal();
            if (total <= 20) {
                value[0] += 1;
            } else if (total > 20 && total <= 40) {
                value[1] += 1;
            } else if (total > 40 && total <= 60) {
                value[2] += 1;
            } else if (total > 60 && total <= 80) {
                value[3] += 1;
            } else if (total > 80) {
                value[4] += 1;
            }
            int totalRgb = (int) (2.55 * total);
            karyawanMapping.setGreen(255 - totalRgb);
            karyawanMapping.setRed(totalRgb);
        }

        return value;
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss EEEE, dd MMM yyyy");
        Date now = new Date();
        String dateToday = sdf.format(now);
        return dateToday;
    }

    public int totalPercentage(List<KaryawanRekapModel> mapping) {
        int total = 0;
        for (KaryawanRekapModel karyawanMapping : mapping) {
            total += karyawanMapping.getTotal();
        }

        if(mapping.size() == 0) {return 0;}
        else {return total/mapping.size();}
    }

    public List<KaryawanRekapModel> mapRekapAssignment(List<KaryawanModel> karyawanList, List<ProyekModel> proyekList, List<KaryawanProyekModel> karyawanProyekList, List<RekapModel> rekapList, int idProyek) {
        List<KaryawanRekapModel> mapping = new ArrayList<KaryawanRekapModel>();
        boolean isAssigned;

        for (KaryawanModel karyawan : karyawanList) {
            isAssigned = false;
            KaryawanRekapModel karyawanMapping = new KaryawanRekapModel();
            List<RekapModel> karyawanRekap = new ArrayList<RekapModel>();

            for(ProyekModel proyek : proyekList) {
                int size = karyawanRekap.size();
                int idKaryawanProyek = 0;

                for(KaryawanProyekModel karyawanProyek : karyawanProyekList) {

                    if(karyawanProyek.getIdKaryawan() == karyawan.getId() && karyawanProyek.getIdProyek() == proyek.getId()) {
                        idKaryawanProyek = karyawanProyek.getId();
                        isAssigned = true;
                        break;
                    }
                }

                for(RekapModel rekap : rekapList) {
//                    if(proyek.getId() == idProyek) {
//                        isAssigned = true;
//                        break;
//                    }

                    if(rekap.getIdKaryawanProyek() == idKaryawanProyek) {
                        karyawanRekap.add(rekap);
                        break;
                    }
                }

                RekapModel rekapEmpty = new RekapModel();
                rekapEmpty.setIdKaryawan(karyawan.getId());
                rekapEmpty.setIdProyek(proyek.getId());
                if(size == karyawanRekap.size()) {
                    karyawanRekap.add(rekapEmpty);
                }
            }

            if(!isAssigned) {
                karyawan.setRatingKaryawan(penugasanService.getAverageRating(penugasanService.getPenugasanList(karyawan.getId())));

                karyawanMapping.setKaryawan(karyawan);
                karyawanMapping.setRekapList(karyawanRekap);
                mapping.add(karyawanMapping);
            }
        }
        Collections.sort(mapping);
        return mapping;
    }

    @Override
    public KaryawanRekapModel getRekapBulananKaryawan(LocalDate periodeDate, int idKaryawan) {
        KaryawanRekapModel karyawanMapping = new KaryawanRekapModel();

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);

        List<RekapModel> rekap = rekapService.getRekapBulananKaryawan(periodeDate, idKaryawan);

        karyawanMapping.setKaryawan(karyawan);
        karyawanMapping.setRekapList(rekap);

        return karyawanMapping;
    }

    @Override
    public KaryawanRekapModel getRekapBulananKaryawanProyek(int idKaryawan, int idProyek) {
        KaryawanRekapModel karyawanMapping = new KaryawanRekapModel();

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);

        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);

        List<RekapModel> rekap = rekapService.selectRekapByIdKaryawanProyek(karyawanProyek.getId());

        karyawanMapping.setKaryawan(karyawan);
        karyawanMapping.setRekapList(rekap);

        return karyawanMapping;
    }
}
