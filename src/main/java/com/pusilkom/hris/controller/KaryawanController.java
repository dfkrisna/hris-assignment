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

    /**
     * method ini berfungsi untuk menampilkan beranda karyawan yang berisi penugasan pada periode ini
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/karyawan")
    public String indexKaryawan(Model model, Principal principal) {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());

        //get periode saat ini dan mengecek penugasan pada periode tersebut
        LocalDate periode = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        List<PenugasanModel> penugasanPeriodeIni = penugasanService.getPenugasanPeriodeIni(karyawan.getId(), periode);

        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        if(penugasanPeriodeIni != null){
            //pass penugasan periode ini ke view
            model.addAttribute("page_title","Beranda Karyawan");
            model.addAttribute("penugasanPeriodeIni", penugasanPeriodeIni);
        }
        else{
            String notification = "Tidak ada penugasan pada periode ini.";
            model.addAttribute("notification", notification);
        }

        return "index-karyawan";
    }

    /**
     * method ini digunakan untuk menampilkan seluruh penugasan karyawan yang sedang login
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping("/karyawan/penugasan")
    public String penugasanKaryawan(Model model, Principal principal) {
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());

        //select seluruh penugasan karyawan pada seluruh periode
        List<PenugasanModel> penugasanKaryawan = penugasanService.assignLatestStatus(penugasanService.getRiwayatPenugasanKaryawan(karyawan.getId()));

        if(penugasanKaryawan != null){
            //pass penugasan ke view
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

    /**
     * method ini digunakan untuk menampilka detail penugasan proyek
     * @param model
     * @param idProyek
     * @param principal
     * @return
     */
    @GetMapping(value ="/karyawan/penugasan/detail/{idProyek}")
    public String detailpenugasanKaryawan(Model model, @PathVariable Integer idProyek, Principal principal) {
        //select karyawan, lalu cek detail penugasan dan rekap evaluasi diri
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        PenugasanModel detailPenugasan = penugasanService.getDetailPenugasanById(idProyek, karyawan.getId());
        List<RekapModel> rekapPenilaianMandiri = rekapService.selectRekapByIdKaryawanProyek(detailPenugasan.getIdKaryawanProyek());

        //pass object ke view
        String dateToday = rekapMappingService.getCurrentDate();
        model.addAttribute("date_today", dateToday);
        model.addAttribute("page_title","Detail Penugasan");
        model.addAttribute("detailPenugasan", detailPenugasan);
        model.addAttribute("rekapPenilaianMandiri", rekapPenilaianMandiri);

        return "detail-proyek";
    }

    /**
     * method ini digunakan untuk menambahkan evaluasi diri karyawan
     * @param model
     * @param session
     * @param idProyek
     * @param id
     * @return
     */
    @GetMapping(value="/karyawan/penilaian-mandiri/tambah/{idProyek}/{id}")
    public String tambahPenilaianMandiri (Model model, HttpSession session,@PathVariable Integer idProyek, @PathVariable Integer id){
        //select rekap bulanan milik karyawan
        RekapModel rekap = rekapService.selectRekapById(id);
        //set deadline pengisian
        LocalDate deadlinePengisian = rekap.getPeriode().plusMonths(1);

        //pass ke view
        model.addAttribute("deadlinePengisian", deadlinePengisian);
        model.addAttribute("rekap", rekap);

        return "form-add-penilaianmandiri";
    }

    /**
     * method ini digunakan untuk submit evaluasi diri yang dimasukkan karyawan
     * @param model
     * @param session
     * @param rekap
     * @param idProyek
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping(value="/karyawan/penilaian-mandiri/tambah/{idProyek}/{id}")
    public String tambahPenilaianMandiriSubmit (Model model, HttpSession session, @ModelAttribute RekapModel rekap,
                                                @PathVariable Integer idProyek, @PathVariable Integer id, RedirectAttributes redirectAttributes){

        //set tanggal penilaian
        LocalDate tanggalPenilaian = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
        rekap.setTanggalPenilaian(tanggalPenilaian);

        //update object rekap bulanan
        rekapService.updatePenilaianMandiri(rekap);

        //add flash attribute
        String notification = "Evaluasi diri berhasil disimpan";
        redirectAttributes.addFlashAttribute("notification", notification);

        return "redirect:/karyawan/penugasan/detail/"+rekap.getIdProyek();
    }

    /**
     * Method ini berfungsi untuk menampilkan daftar rekan seproyek suatu karyawan,
     * dan juga memfasilitasi untuk mengisi feedback dan penilaian
     * @param principal
     * @param periode
     * @return
     */
    @GetMapping(value="/karyawan/rekanseproyek")
    public String lihatRekanSekproyek(Model model, HttpSession session, Principal principal,
                                      @RequestParam(value = "periode", required = false) String periode)
    {

        LocalDate periodeNow = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        //Melakukan handling periode yang akan ditampilkan pada halaman rekan seproyek
        if(periode != null) {
            String[] split = periode.split(" ");
            int year = Integer.parseInt(split[1]);
            String monthString = split[0].toUpperCase();
            LocalDate periodeCompare = LocalDate.of(year, Month.valueOf(split[0].toUpperCase()),1);
            if(periodeCompare.equals(periodeNow)) {
                model.addAttribute("offPeriode", "offPeriode");
                session.setAttribute("periodeSelected", periodeNow);
            }else{
                if(periodeCompare.equals(periodeNow.plusMonths(1))) {
                    session.setAttribute("periodeSelected", periodeNow);
                    model.addAttribute("periodeOut", "Pengisian Feedback dan Penilaian Belum Dimulai atau Sudah Ditutup");
                }else {
                    periodeNow = periodeCompare;
                    session.setAttribute("periodeSelected", periodeNow);
                    model.addAttribute("periodeOut", "Pengisian Feedback dan Penilaian Belum Dimulai atau Sudah Ditutup");
                }
            }
        }else {
            model.addAttribute("offPeriode", "offPeriode");
            session.setAttribute("periodeSelected", periodeNow);
        }

        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        int userId = karyawan.getId();

        String penugasan = karyawanService.cekPenugasanKaryawanById(userId);

        //kondisional jika karyawan yang sedang login punya penugasan atau tidak punya penugasan
        if(penugasan.equalsIgnoreCase("yes")){

            List<Integer> proyekSekarang = karyawanService.getUserProyek(userId);

            List<FeedbackRatingModel> rekanWithFeedback =
                    karyawanService.getRekanSeproyekFeedback(proyekSekarang, userId, periodeNow);

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

    /**
     * Method ini berfungsi untuk menampilkan daftar rekan seproyek suatu karyawan,
     *  dan juga memfasilitasi untuk mengisi feedback dan penilaian
     * @param principal
     * @param namaRekan
     * @param idRekan
     * @param kodeProyek
     * @param feedback
     * @param ratingRekan
     * @return
     */
    @GetMapping(value="/karyawan/rekanseproyek/feedback")
    public String manageFeedbackRekan(Model model, HttpSession session, Principal principal,
                                      @RequestParam(value = "namaRekan", required = false) String namaRekan,
                                      @RequestParam(value = "idRekan", required = false) int idRekan,
                                      @RequestParam(value = "kodeProyek", required = false) String kodeProyek,
                                      @RequestParam(value = "feedback", required = false) String feedback,
                                      @RequestParam(value = "ratingRekan", required = false) int ratingRekan) throws ParseException {

        //Mempersiapkan variabel yang akan digunakan untuk mengambil rekan dari database
        KaryawanModel karyawan = karyawanService.selectKaryawanByEmail(principal.getName());
        int userId = karyawan.getId();
        LocalDate periodeSelected = (LocalDate) session.getAttribute("periodeSelected");
        ProyekModel proyek = proyekService.getProyekByKode(kodeProyek);
        int idProyek = proyek.getId();
        int idPenilai = karyawanService.getKaryawanIdPenilai(userId, idProyek);

        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idRekan, idProyek);
        int idKaryawanProyek = karyawanProyek.getId();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        Date now = new Date();
        java.sql.Timestamp waktuIsi = new java.sql.Timestamp(now.getTime());


        //Memvalidasi apakah rekan sudah memiliki feedback atau belum
        //jika belum ada maka akan di-add, jika ada maka akan di-update
        String verifFeedback = karyawanService.verifyFeedbackRekan(idKaryawanProyek, idPenilai, idProyek, periodeSelected);
        if(verifFeedback.equalsIgnoreCase("belum ada")) {
            karyawanService.addFeedbackRekan(feedback, ratingRekan, idKaryawanProyek, idPenilai, idProyek, periodeSelected, waktuIsi);
            model.addAttribute("successEdit", "Berhasil memberikan feedback untuk rekan yang bernama " + namaRekan);
        }else {
            karyawanService.updateFeedbackRekan(feedback, ratingRekan, idKaryawanProyek, idPenilai, idProyek, periodeSelected, waktuIsi);
            model.addAttribute("successEdit", "Berhasil mengubah feedback untuk rekan yang bernama " + namaRekan);
        }


        List<Integer> proyekSekarang = karyawanService.getUserProyek(userId);

        //Mendapatkan rekan seproyek yang sudah diberi feedback dan penilaian dan yang belum diberi feedback dan penilaian
        List<FeedbackRatingModel> rekanWithFeedback =
                karyawanService.getRekanSeproyekFeedback(proyekSekarang, userId, periodeSelected);

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

