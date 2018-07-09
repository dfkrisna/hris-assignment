package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ManajerDivisiController {

    @Autowired
    KaryawanService karyawanService;

    @Autowired
    DivisiService divisiService;

    @Autowired
    RekapMappingService rekapMappingService;

    @Autowired
    ProyekService proyekService;

    @Autowired
    RekapService rekapService;

    @Autowired
    RatingFeedbackService ratingFeedbackService;

    @Autowired
    KaryawanProyekService karyawanProyekService;

    /**
     * method ini digunakan untuk menampilkan detail karyawan yang merupakan bawahan manajer divisi
     * @param model
     * @param periode
     * @param idKaryawan
     * @param notification
     * @return
     */
    @GetMapping(value="/mngdivisi/rekap/{idKar}")
    public String showDetailKaryawan(Model model,
                                     @RequestParam(value = "periode", required = false) String periode,
                                     @PathVariable(value = "idKar") int idKaryawan,
                                     @ModelAttribute("notification") String notification) {
        LocalDate periodeDate;
        if(periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);
        } else { periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1); }

        System.out.println(periodeDate);

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);

        DivisiModel divisi = divisiService.getDivisiByID(karyawan.getIdDivisi());

        KaryawanRekapModel karyawanRekap = rekapMappingService.getRekapBulananKaryawan(periodeDate, idKaryawan);

        Map mapRekapProyek = new HashMap();

        List<RekapModel> rekapList = karyawanRekap.getRekapList();

        if(!rekapList.isEmpty()) {
            for(RekapModel rekap : rekapList) {
                ProyekModel proyek = proyekService.getProyekById(rekap.getIdProyek());
                mapRekapProyek.put(rekap.getId(), proyek);
            }
        }

        if(notification != null) {
            model.addAttribute("notification", notification);
        }

        LocalDate periodeNow = LocalDate.now();
        boolean isNow = false;
        if(periodeDate.getYear() == periodeNow.getYear() && periodeDate.getMonth().equals(periodeNow.getMonth())) {
            isNow = true;
        }

        model.addAttribute("isNow", isNow);

        model.addAttribute("isEmpty", rekapList.isEmpty());
        model.addAttribute("karyawanRekap", karyawanRekap);
        model.addAttribute("mapRekapProyek", mapRekapProyek);
        model.addAttribute("divisi", divisi);
        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));

        //ambil dulu id karpro
        List<KaryawanProyekModel> dicari = karyawanProyekService.selectKaryawanProyekByKaryawanPeriode(idKaryawan,periodeDate);
        boolean isEmptyRF = false;
        if(dicari.isEmpty()) {
            isEmptyRF = true;
            return "mngdivisi-penilaian";
        } else {

            List<RatingFeedbackModel> listRF=null;

            for(KaryawanProyekModel karpro : dicari) {
                listRF = ratingFeedbackService.selectRatingFeedbackPer(karpro.getId(), periodeDate);
            }

            if (listRF.isEmpty() || listRF==null) {
                isEmptyRF = true;
            }
            model.addAttribute("isEmptyRF", isEmptyRF);
            model.addAttribute("listRF", listRF);

            return "mngdivisi-penilaian";
        }
    }

    /**
     * method ini digunakan untuk memproses finalisasi evaluasi diri anggota divisi
     * @param model
     * @param ra
     * @param periode
     * @param idKarProy
     * @return
     */
    @PostMapping(value = "/mngdivisi/rekap/finalisasi")
    public String finalisasi(Model model,
                             RedirectAttributes ra,
                             @RequestParam(value = "periode") String periode,
                             @RequestParam(value = "idKarProy") int idKarProy) {
        LocalDate periodeDate = LocalDate.parse(periode, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekById(idKarProy);

        RekapModel rekap = rekapService.getRekapBulanan(idKarProy, periodeDate);
        rekap.setApproved(true);
        rekapService.updateRekap(rekap);

        String notification = "Evaluasi mandiri berhasil difinalisasi";

        ra.addFlashAttribute("notification", notification);

        System.out.println("nyampe sini lho");

        return "redirect:/mngdivisi/rekap/" + karyawanProyek.getIdKaryawan();
    }
}
