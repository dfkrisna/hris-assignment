package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.model.FeedbackRatingModel;
import com.pusilkom.hris.model.KaryawanModel;
import com.pusilkom.hris.model.PenugasanModel;
import com.pusilkom.hris.model.RekapModel;
import com.pusilkom.hris.service.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class KaryawanController {
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
    RekapMappingService rekapMappingService;

    @Autowired
    DivisiService divisiService;

    @GetMapping("/karyawan")
    public String indexKaryawan(Model model, Principal principal) {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());

        LocalDate periode = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        List<PenugasanModel> penugasanPeriodeIni = penugasanService.getPenugasanPeriodeIni(karyawan.getId(), periode);

        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        if(penugasanPeriodeIni != null){
            model.addAttribute("page_title","Beranda Karyawan");
            model.addAttribute("penugasanPeriodeIni", penugasanPeriodeIni);
        }
        else{
            String notification = "Tidak ada penugasan pada periode ini.";
            model.addAttribute("notification", notification);
        }

        return "index-karyawan";
    }

    @RequestMapping("/karyawan/penugasan")
    public String penugasanKaryawan(Model model, Principal principal) {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());

        List<PenugasanModel> penugasanKaryawan = penugasanService.assignLatestStatus(penugasanService.getRiwayatPenugasanKaryawan(karyawan.getId()));

        if(penugasanKaryawan != null){
            model.addAttribute("page_title","Beranda Karyawan");
            model.addAttribute("penugasanKaryawan", penugasanKaryawan);
        }
        else{
            String notification = "Tidak ada penugasan.";
            model.addAttribute("notification", notification);
        }

        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("date_today", dateToday);
        model.addAttribute("page_title","Riwayat Penugasan");
        model.addAttribute("penugasanKaryawan", penugasanKaryawan);

        return "penugasan-karyawan";
    }

    @GetMapping(value ="/karyawan/penugasan/detail/{idProyek}")
    public String detailpenugasanKaryawan(Model model, @PathVariable Integer idProyek, Principal principal) {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        PenugasanModel detailPenugasan = penugasanService.getDetailPenugasanById(idProyek, karyawan.getId());
        List<RekapModel> rekapPenilaianMandiri = rekapService.selectRekapByIdKaryawanProyek(detailPenugasan.getIdKaryawanProyek());

        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("date_today", dateToday);
        model.addAttribute("page_title","Detail Penugasan");
        model.addAttribute("detailPenugasan", detailPenugasan);
        model.addAttribute("rekapPenilaianMandiri", rekapPenilaianMandiri);

        return "detail-proyek";
    }

    @GetMapping(value="/karyawan/penilaian-mandiri/tambah/{idProyek}/{id}")
    public String tambahPenilaianMandiri (Model model, HttpSession session,@PathVariable Integer idProyek, @PathVariable Integer id){
        RekapModel rekap = rekapService.selectRekapById(id);
        LocalDate deadlinePengisian = rekap.getPeriode().plusMonths(1);

        model.addAttribute("deadlinePengisian", deadlinePengisian);
        model.addAttribute("rekap", rekap);

        return "form-add-penilaianmandiri";
    }

    @PostMapping(value="/karyawan/penilaian-mandiri/tambah/{idProyek}/{id}")
    public String tambahPenilaianMandiriSubmit (Model model, HttpSession session, @ModelAttribute RekapModel rekap,
                                                @PathVariable Integer idProyek, @PathVariable Integer id, RedirectAttributes redirectAttributes){

        LocalDate tanggalPenilaian = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());

        rekap.setTanggalPenilaian(tanggalPenilaian);
        rekapService.updatePenilaianMandiri(rekap);

        String notification = "Evaluasi diri berhasil disimpan";
        redirectAttributes.addFlashAttribute("notification", notification);

        return "redirect:/karyawan/penugasan/detail/"+rekap.getIdProyek();
    }

    @GetMapping(value="/karyawan/rekanseproyek")
    public String lihatRekanSekproyek(Model model, HttpSession session, Principal principal,
                                      @RequestParam(value = "periode", required = false) String periode)
    {

        LocalDate periodeNow = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        if(periode != null) {
            String[] split = periode.split(" ");
            int year = Integer.parseInt(split[1]);
            String monthString = split[0].toUpperCase();
            LocalDate periodeCompare = LocalDate.of(year, Month.valueOf(split[0].toUpperCase()),1);
            if(periodeCompare.equals(periodeNow)) {
                periodeNow = periodeNow;
                model.addAttribute("offPeriode", "offPeriode");
                session.setAttribute("periodeSelected", periodeNow);
            }else{
                periodeNow = periodeCompare;
                session.setAttribute("periodeSelected", periodeNow);
                model.addAttribute("periodeOut", "Pengisian Feedback dan Penilaian Belum Dimulai atau Sudah Ditutup");
            }
        }else {
            periodeNow = periodeNow;
            model.addAttribute("offPeriode", "offPeriode");
            session.setAttribute("periodeSelected", periodeNow);
        }

        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        int userId = karyawan.getId();

        String penugasan = karyawanService.cekPenugasanKaryawanById(userId);
        if(penugasan.equalsIgnoreCase("yes")){
            int idPenilai = karyawanService.getKaryawanIdPenilai(userId);
            session.setAttribute("idPenilai", Integer.toString(idPenilai));
            List<Integer> proyekSekarang = karyawanService.getUserProyek(userId);

            List<FeedbackRatingModel> rekanWithFeedback =
                    karyawanService.getRekanSeproyekFeedback(proyekSekarang, userId, periodeNow, idPenilai);

            List<FeedbackRatingModel> rekanNoFeedback =
                    karyawanService.getRekanSeproyek(proyekSekarang, userId, periodeNow);

            if(rekanWithFeedback.isEmpty() && rekanNoFeedback.isEmpty()) {
                model.addAttribute("notification","Tidak ada rekan seproyek di periode ini");
            }else if(rekanWithFeedback.isEmpty()) {
                model.addAttribute("rekans",rekanNoFeedback);
                model.addAttribute("noFeedback","noFeedback");
            }else if(rekanNoFeedback.isEmpty()){
                model.addAttribute("rekans",rekanWithFeedback);
            }else {
                model.addAttribute("rekans", rekanWithFeedback);
                model.addAttribute("rekansNoFeedback", rekanNoFeedback);
            }

            String dateToday = rekapMappingService.getCurrentDate();
            model.addAttribute("prevPeriode", periodeNow.minusMonths(1));
            model.addAttribute("periodeNow", periodeNow);
            model.addAttribute("nextPeriode", periodeNow.plusMonths(1));
            model.addAttribute("date_today", dateToday);

        }else {
            model.addAttribute("noPenugasan", "noPenugasan");
            String dateToday = rekapMappingService.getCurrentDate();
            model.addAttribute("date_today", dateToday);
        }

        return "karyawan-rekanseproyek";
    }

    @GetMapping(value="/karyawan/rekanseproyek/feedback")
    public String manageFeedbackRekan(Model model, HttpSession session, Principal principal,
                                      @RequestParam(value = "namaRekan", required = false) String namaRekan,
                                      @RequestParam(value = "idRekan", required = false) int idRekan,
                                      @RequestParam(value = "kodeProyek", required = false) String kodeProyek,
                                      @RequestParam(value = "feedback", required = false) String feedback,
                                      @RequestParam(value = "ratingRekan", required = false) int ratingRekan) throws ParseException {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        int userId = karyawan.getId();
        LocalDate periodeSelected = (LocalDate) session.getAttribute("periodeSelected");
        int idPenilai = Integer.parseInt((String) session.getAttribute("idPenilai"));

        ProyekModel proyek = proyekService.getProyekByKode(kodeProyek);
        int idProyek = proyek.getId();

        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idRekan, idProyek);
        int idKaryawanProyek = karyawanProyek.getId();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date now = new Date();
        java.sql.Timestamp waktuIsi = new java.sql.Timestamp(now.getTime());

        String verifFeedback = karyawanService.verifyFeedbackRekan(idKaryawanProyek, idPenilai, idProyek, periodeSelected);
        if(verifFeedback.equalsIgnoreCase("belum ada")) {
            karyawanService.addFeedbackRekan(feedback, ratingRekan, idKaryawanProyek, idPenilai, idProyek, periodeSelected, waktuIsi);
            model.addAttribute("successEdit", "Berhasil memberikan feedback untuk rekan yang bernama " + namaRekan);
        }else {
            karyawanService.updateFeedbackRekan(feedback, ratingRekan, idKaryawanProyek, idPenilai, idProyek, periodeSelected, waktuIsi);
            model.addAttribute("successEdit", "Berhasil mengubah feedback untuk rekan yang bernama " + namaRekan);
        }


        List<Integer> proyekSekarang = karyawanService.getUserProyek(userId);

        List<FeedbackRatingModel> rekanWithFeedback =
                karyawanService.getRekanSeproyekFeedback(proyekSekarang, userId, periodeSelected, idPenilai);

        List<FeedbackRatingModel> rekanNoFeedback =
                karyawanService.getRekanSeproyek(proyekSekarang, userId, periodeSelected);

        if(rekanWithFeedback.isEmpty() && rekanNoFeedback.isEmpty()) {
            model.addAttribute("notification","Tidak ada rekan seproyek di periode ini");
        }else if(rekanWithFeedback.isEmpty()) {
            model.addAttribute("rekans",rekanNoFeedback);
            model.addAttribute("noFeedback","noFeedback");
        }else if(rekanNoFeedback.isEmpty()){
            model.addAttribute("rekans",rekanWithFeedback);
        }else {
            model.addAttribute("rekans", rekanWithFeedback);
            model.addAttribute("rekansNoFeedback", rekanNoFeedback);
        }

        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("prevPeriode", periodeSelected.minusMonths(1));
        model.addAttribute("offPeriode", "offPeriode");
        model.addAttribute("periodeNow", periodeSelected);
        model.addAttribute("nextPeriode", periodeSelected.plusMonths(1));
        model.addAttribute("date_today", dateToday);
        return "karyawan-rekanseproyek";
    }
}

