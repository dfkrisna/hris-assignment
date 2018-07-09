package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


@Controller
public class RekapController {
    @Autowired
    ProyekService proyekService;

    @Autowired
    KaryawanProyekService karyawanProyekService;

    @Autowired
    KaryawanService karyawanService;

    @Autowired
    RekapService rekapService;

    @Autowired
    PenugasanService penugasanService;

    @Autowired
    RatingFeedbackService ratingFeedbackService;

    /**
     * method untuk membuka halaman rekap karyawan yang berisi rekap keseluruhan karyawan dan
     * data riwayat penugasan karyawan pada proyek-proyek
     * @param model
     * @param idKaryawan
     * @return
     */
    @GetMapping(value = "/rekap/karyawan/riwayat/{idKaryawan}")
    public String rekapKaryawanRiwayat(Model model, @PathVariable Integer idKaryawan) {
        LocalDate  periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);
        model.addAttribute("karyawan", karyawan);

        if(karyawan != null) {
            List<PenugasanModel> penugasanList = penugasanService.getPenugasanList(idKaryawan);
            int ratingKaryawan = ratingFeedbackService.getAvgRatingKaryawan(karyawan.getId());
            int persentaseKontribusi = (int) (rekapService.getKaryawanKontribusi(karyawan.getId(), LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1)) * 100);


            model.addAttribute("ratingKaryawan", ratingKaryawan);
            model.addAttribute("persentaseKontribusi", persentaseKontribusi);
            model.addAttribute("penugasanList", penugasanList);
            model.addAttribute("chartLabel", rekapService.getSixPeriod(periodeDate));
            model.addAttribute("chartValueCum", ratingFeedbackService.getRecapRating(idKaryawan, periodeDate));
            model.addAttribute("chartValueAvg", ratingFeedbackService.getRecapRatingAvg(idKaryawan, periodeDate));
        }

        return "rekap-karyawan";
    }

    /**
     * method untuk membuka halaman rekap karyawan yang berisi rekap feedback dan nilai karyawan pada periode tertentu
     * @param model
     * @param idKaryawan
     * @param periode
     * @return
     */
    @RequestMapping(value = "/rekap/karyawan/feedback/{idKaryawan}", method = RequestMethod.GET)
    public String rekapKaryawanFeedback(Model model, @PathVariable Integer idKaryawan,
                                        @RequestParam(value = "periode", required = false) String periode) {
        LocalDate periodeDate;
        if(periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);
        } else { periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1); }

        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);
        model.addAttribute("karyawan", karyawan);

        if(karyawan != null) {
            List<PenugasanModel> penugasanList = penugasanService.getPenugasanList(idKaryawan);
            List<RatingFeedbackModel> feedbackList = ratingFeedbackService.getPenilaianKaryawan(karyawan.getId(), periodeDate);
            int ratingKaryawan = ratingFeedbackService.getAvgRatingKaryawan(karyawan.getId());
            int persentaseKontribusi = (int) (rekapService.getKaryawanKontribusi(karyawan.getId(), LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1)) * 100);

            model.addAttribute("penugasanList", penugasanList);
            model.addAttribute("karyawan", karyawan);
            model.addAttribute("ratingKaryawan", ratingKaryawan);
            model.addAttribute("persentaseKontribusi", persentaseKontribusi);
            model.addAttribute("feedbackList", feedbackList);
            model.addAttribute("chartLabel", rekapService.getSixPeriod(periodeDate));
            model.addAttribute("chartValueCum", ratingFeedbackService.getRecapRating(idKaryawan, periodeDate));
            model.addAttribute("chartValueAvg", ratingFeedbackService.getRecapRatingAvg(idKaryawan, periodeDate));
        }
        return "rekap-karyawan-feedback";
    }

    /**
     * method untuk membuka halaman rekap proyek yang berisi data proyek dan anggota-anggota
     * yang di-assign pada proyek
     * @param model
     * @param idProyek
     * @return
     */
    @RequestMapping(value = "/rekap/proyek/{idProyek}")
    public String rekapProyek(Model model, @PathVariable Integer idProyek) {
        ProyekModel proyek = proyekService.getProyekById(idProyek);

        if(proyek != null) {
            String statusProyek = penugasanService.getLatestStatus(idProyek);
            List<KaryawanAnggotaModel> anggotaList = penugasanService.getAnggotaProyek(idProyek);

            if (proyek.getIdProjectLead() != null && proyek.getIdProjectLead() != 0) {
                KaryawanProyekModel karprolead = karyawanProyekService.getKaryawanProyekById(proyek.getIdProjectLead());
                KaryawanModel projlead = karyawanService.getKaryawanById(karprolead.getIdKaryawan());
                System.out.println("ini projelad " + projlead.getNama());
                model.addAttribute("projlead", projlead);
            }

            if (proyek.getIdParent() != null) {
                System.out.println("ini parent proyek " + proyekService.getProyekById(proyek.getIdParent()).getNamaProyek());
                model.addAttribute("parentproyek", proyekService.getProyekById(proyek.getIdParent()));
            }

            model.addAttribute("proyek", proyek);
            model.addAttribute("status", statusProyek);
            model.addAttribute("anggotaList", anggotaList);
        } else {
            model.addAttribute("idProyek", idProyek);
        }

        return "rekap-proyek";
    }
}

