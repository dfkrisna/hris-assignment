package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

@Controller
public class PMOController {
    @Autowired
    PenggunaService penggunaDAO;

    @Autowired
    KlienService klienDAO;

    @Autowired
    ProyekService proyekDAO;

    @Autowired
    RekapMappingService rekapMappingDAO;

    @Autowired
    KaryawanService karyawanDAO;

    @Autowired
    PenugasanService penugasanDAO;

    @Autowired
    KaryawanProyekService karyawanProyekDAO;

    @Autowired
    RekapService rekapDAO;

    @Autowired
    DivisiService divisiDAO;

    @Autowired
    RatingFeedbackService ratingFeedbackDAO;

    /**
     * method ini berfungsi untuk menampilkan form pada fitur tambah proyek.
     * @param model
     * @param principal
     * @return halaman form tambah proyek
     */
    @GetMapping("/proyek/tambah")
    public String addProyek(Model model, Principal principal) {
        //mengambil data pengguna yang sedang login
        PenggunaModel pengguna = penggunaDAO.getPenggunaLama(principal.getName());

        //membuat proyek untuk di pass ke view
        ProyekModel proyek = new ProyekModel();
        proyek.setIdPmo(1);
        proyek.setStartPeriode(null);
        proyek.setEndPeriode(null);
        model.addAttribute("proyek", proyek);

        //pass date ke view
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //mengambil list proyek untuk menjadi pilihan pada dropdown
        List<ProyekModel> parentProyek = null;
        parentProyek = proyekDAO.selectProyekByPeriodePmo(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1));
        model.addAttribute("parentproy", parentProyek);

        //mengambil list klien untuk menjadi pilihan pada dropdown
        List<KlienModel> listklien;
        listklien = klienDAO.selectAllKlien();
        System.out.println(listklien);
        if (listklien != null) {
            model.addAttribute("listklien", listklien);
        }

        return "pmo-form-addproyek";

    }

    /**
     * method ini berfungsi untuk mengambil data proyek yang baru ditambahkan
     * @param model
     * @param proyek
     * @param startPeriode
     * @param endPeriode
     * @return halaman mengelola penugasan
     */
    @PostMapping(value = "/proyek/tambah")
    public String addProyekSubmit(Model model, @ModelAttribute ProyekModel proyek, @RequestParam(value = "startdate", required = false) String startPeriode,
                                  @RequestParam(value = "enddate", required = false) String endPeriode) {
       //null handling
        if (proyek == null) {
//            model.addAttribute("errorMessage", "data tidak bisa kosong");
//            return "error/404";

            return "index";
        }

        // menyocokkan format untuk masukkan dari form
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

        //memasukkan value startperiode
        if (!startPeriode.equals("")) {
            LocalDate startperiode = LocalDate.parse("1 " + startPeriode, formatter);
            proyek.setStartPeriode(LocalDate.of(startperiode.getYear(), startperiode.getMonthValue(), 1));
        }

        //menambahkan proyek pada database
        proyekDAO.addProyek(proyek);

        //menambahkan status proyek menjadi aktif
        Integer proyekID;
        ProyekModel baru = proyekDAO.getProyekByKode(proyek.getKode());
        proyekID = baru.getId();
        proyekDAO.addProyekStatus(proyekID, 2);

        model.addAttribute("proyek", proyek);

        return "redirect:/pmo/proyek/tambah/assign/" + proyekID;
    }

    /**
     * method ini berfungsi untuk menampilkan data proyek pada form update
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/proyek/ubah/{id}")
    public String ubahProyek(Model model, @PathVariable(value = "id") Integer id) {

        //mengambil date today
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //null handling
        if (id == null) {
//            model.addAttribute("errorMessage", "id tidak ditemukan");
//            return "error/404";
        }
        //ambil data proyek sebelumnya
        ProyekModel proyek = proyekDAO.selectProyekByIdPmo(id);

        //mengambil isi form
        if (proyek != null) {
            model.addAttribute("proyek", proyek);
            String periodemulai = "";

            //menampilkan periode sebelumnya
            if (proyek.getStartPeriode() != null) {
                periodemulai = proyek.getStartPeriode().getMonth().toString() + " " + proyek.getStartPeriode().getYear();
            }

            model.addAttribute("startdate", periodemulai);

            //menampilkan parent proyek
            List<ProyekModel> parentProyek = null;
            parentProyek = proyekDAO.selectProyekByPeriodePmo(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1));
            if (proyek.getIdParent() != null) {
                ProyekModel parentProy = proyekDAO.selectProyekByIdPmo(proyek.getIdParent());
                model.addAttribute("parentID", parentProy.getId());
                model.addAttribute("parentName", parentProy.getNamaProyek());
                //tidak menampilkan dirinya sendiri pada proyek
                Predicate<ProyekModel> proyekPredicate = p -> p.getId() == parentProy.getId();
                parentProyek.removeIf(proyekPredicate);
            }

            model.addAttribute("parentproy", parentProyek);


            //menampilkan status proyek
            String statusproyek = penugasanDAO.getLatestStatus(proyek.getId());
            System.out.println(statusproyek);
            model.addAttribute("statproy", statusproyek);

            //menampilkan klien proyek
            List<KlienModel> klienProyek = null;
            klienProyek = klienDAO.selectAllKlien();
            KlienModel klienNow = klienDAO.selectKlienByID(proyek.getIdKlien());
            model.addAttribute("klien", klienNow);
            Predicate<KlienModel> klienPredicate = k -> k.getId() == klienNow.getId();
            klienProyek.removeIf(klienPredicate);
            model.addAttribute("listklien", klienProyek);

            return "pmo-form-updateproyek";
        } else {
//            model.addAttribute("errorMessage", "proyek id " +id+" Tidak Ditemukan");
//            return "error/404";
            //null handling
            return "index";
        }

    }

    /**
     * method ini berfungsi untuk mengambil data dari form update
     * @param model
     * @param proyek
     * @param startPeriode
     * @param endPeriode
     * @return
     */
    @PostMapping(value = "/proyek/ubah/{id}")
    public String ubahProyek(Model model, @ModelAttribute ProyekModel proyek,
                             @RequestParam(value = "startdate", required = false) String startPeriode,
                             @RequestParam(value = "enddate", required = false) String endPeriode) {

        //passsing date today
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //null handling
        if (proyek == null) {
//            model.addAttribute("errorMessage", "Data Tidak Bisa Kosong");
//            return "error/404";
            System.out.println("masuk null");
            return "index";
        }

        //format untuk menyesuaikan
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("d MMMM yyyy").toFormatter(Locale.ENGLISH);

        //jika start periode ada, maka di update
        if (!startPeriode.equals("")) {
            LocalDate startperiode = LocalDate.parse("1 " + startPeriode, formatter);
            proyek.setStartPeriode(LocalDate.of(startperiode.getYear(), startperiode.getMonthValue(), 1));
        }

//        System.out.println(endPeriode);
//
//        if (!endPeriode.equals("")) {
//            LocalDate endperiode = LocalDate.parse("1 " + endPeriode, formatter);
//            proyek.setEndPeriode(LocalDate.of(endperiode.getYear(), endperiode.getMonthValue(), 1));
//        }


//        if(statusProyek != null) {

//            String statusLama = penugasanDAO.getLatestStatus(proyek.getId());
//            Integer idStatus = null;
//
//
//            if(statusLama != null && !statusLama.equalsIgnoreCase(statusProyek)) {
//                //add status
//
//                if(statusProyek.equalsIgnoreCase("Selesai")) {
//                    idStatus = 4;
//                    System.out.println("masukni");
//                }
//                if(statusProyek.equalsIgnoreCase("Aktif")) {
//                    idStatus = 2;
//                }
//
//                if(statusProyek.equalsIgnoreCase("Berjalan")){
//                    idStatus = 3;
//                }
//
//                proyekDAO.addProyekStatus(proyek.getId(),idStatus);
//            }
//            String idStatus = "";
//
//            if(statusProyek.equalsIgnoreCase("Aktif")) {
//                idStatus = "Aktif";
//            }
//            if(statusProyek.equalsIgnoreCase("Tidak Aktif")){
//                idStatus = "Tidak Aktif";
//            }
//            penugasanDAO.addStatusProyek(idStatus,proyek.getId(), LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1));
//        }

        //mengupdate data
        proyekDAO.updateProyek(proyek);

        //menambahkan nama klien pada model
        proyek.setNamaKlien(klienDAO.selectKlienByID(proyek.getIdKlien()).getNama());

        model.addAttribute("proyek", proyek);
//        model.addAttribute("status", statusProyek);
        model.addAttribute("notification", "Data proyek berhasil di ubah");
        //menampilkan status
        model.addAttribute("status", penugasanDAO.getLatestStatus(proyek.getId()));
        //menampilkan subproyek
        model.addAttribute("subproyek", proyekDAO.selectProyekByIdParent(proyek.getId()));

        //menampilkan projectlead
        if (proyek.getIdProjectLead() != null && proyek.getIdProjectLead() != 0) {
            KaryawanProyekModel karprolead = karyawanProyekDAO.getKaryawanProyekById(proyek.getIdProjectLead());
            KaryawanModel projlead = karyawanDAO.getKaryawanById(karprolead.getIdKaryawan());
            model.addAttribute("projlead", projlead);
        }

        //menampilkan parent proyek
        if (proyek.getIdParent() != null) {
            System.out.println("ini parent proyek " + proyekDAO.getProyekById(proyek.getIdParent()).getNamaProyek());
            model.addAttribute("parentproyek", proyekDAO.getProyekById(proyek.getIdParent()));
        }

        return "pmo-detail-proyek";


    }

    /**
     * method ini berfungsi untuk menampilkan detail proyek pmo
     * @param model
     * @param id
     * @param notification
     * @return halaman detail proyek
     */
    @GetMapping("/proyek/detail/{id}")
    public String detailProyek(Model model, @PathVariable(value = "id") Integer id,
                               @ModelAttribute("notification") String notification) {

        //mengambil proyek dari database
        ProyekModel proyek = proyekDAO.getProyekById(id);

        //mengambil periode proyek
        String periodemulai = "";
        String periodeselesai = "";
        if (proyek.getStartPeriode() != null) {
            periodemulai = proyek.getStartPeriode().getMonth().toString() + " " + proyek.getStartPeriode().getYear();
        }

        if (proyek.getEndPeriode() != null) {
            periodeselesai = proyek.getEndPeriode().getMonth().toString() + " " + proyek.getEndPeriode().getYear();
        }

        //pass date today
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //passing atribut periode
        model.addAttribute("status", penugasanDAO.getLatestStatus(proyek.getId()));
        model.addAttribute("startdate", periodemulai);
        model.addAttribute("enddate", periodeselesai);

        //mengambil anggota proyek
        List<KaryawanAnggotaModel> anggotaList = penugasanDAO.getAnggotaProyek(id);
        model.addAttribute("anggotaList", anggotaList);

        //mengambil subproyek dari proyek
        List<ProyekModel> subproyek = proyekDAO.selectProyekByIdParent(id);
        model.addAttribute("subproyek", subproyek);

        //mengambil nama klien
        proyek.setNamaKlien(klienDAO.selectKlienByID(proyek.getIdKlien()).getNama());

        //menambah notifikasi
        if (notification != null) {
            model.addAttribute("notification", notification);
        }

        //mengambil projectlead
        if (proyek.getIdProjectLead() != null && proyek.getIdProjectLead() != 0) {

            KaryawanProyekModel karprolead = karyawanProyekDAO.getKaryawanProyekById(proyek.getIdProjectLead());
            KaryawanModel projlead = karyawanDAO.getKaryawanById(karprolead.getIdKaryawan());
            model.addAttribute("projlead", projlead);
        }

        //mengambil parent proyek
        if (proyek.getIdParent() != null) {
            model.addAttribute("parentproyek", proyekDAO.getProyekById(proyek.getIdParent()));
        }

        model.addAttribute("proyek", proyek);

        return "pmo-detail-proyek";
    }

    /**
     * method ini berfungsi untuk menampilkan proyek dipimpin projectlead
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/karyawan/projectlead")
    public String listProyekDipimin(Model model, Principal principal) {

        //pass date today
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //mengambil user yang sedang login
        PenggunaModel pengguna = penggunaDAO.getPenggunaLama(principal.getName());
        KaryawanModel projlead = karyawanDAO.getKaryawanByIdPengguna(pengguna.getId());

        //mengambil proyek yang dipimpin projectlead yang login
        List<PenugasanModel> listpenugasan = proyekDAO.selectProyekDipimpin(projlead.getId());
        model.addAttribute("listpenugasan", listpenugasan);
        model.addAttribute("isempty", listpenugasan.isEmpty());
        //menampilkan list proyek
        if (!listpenugasan.isEmpty()) {
            String itu = listpenugasan.get(0).getPersentase() + "";
            System.out.println("get persentase : " + itu);

            itu = listpenugasan.get(0).getKontribusi() + "";
            System.out.println("get kontribusi : " + itu);

            itu = listpenugasan.get(0).getPersentaseKontribusi() + "";
            System.out.println("get persentase kontribusi : " + itu);
        }
        return "projectlead-listproyek";
    }

    /**
     * method ini berfungsi untuk menampilkan halaman proyek yang dipimpin projlead
     * @param model
     * @param id
     * @param principal
     * @return
     */
    @GetMapping("/karyawan/projectlead/detail/{id}")
    public String detailProyekProjlead(Model model, @PathVariable(value = "id") Integer id, Principal principal) {
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        //mengambil penugasan
        PenggunaModel pengguna = penggunaDAO.getPenggunaLama(principal.getName());
        KaryawanModel projlead = karyawanDAO.getKaryawanByIdPengguna(pengguna.getId());
        PenugasanModel penugasan = penugasanDAO.getDetailPenugasanById(id, projlead.getId());
        model.addAttribute("penugasan", penugasan);

        //mengambil data proyek
        ProyekModel proyek = proyekDAO.getProyekById(id);
        model.addAttribute("proyek", proyek);

        String status = penugasanDAO.getLatestStatus(proyek.getId());
        model.addAttribute("status", status);
//        List<KaryawanAnggotaModel> anggotaList = penugasanDAO.getAnggotaProyek(id);
//        model.addAttribute("anggotaList",anggotaList);

        //mengambil list anggota
        List<PenugasanModel> anggotaList = penugasanDAO.getListPenugasan(id);

        //menghapus dirinya sendiri pada tabel anggota
        Predicate<PenugasanModel> projleadPredicate = p -> p.getIdKaryawan() == projlead.getId();
        anggotaList.removeIf(projleadPredicate);

        model.addAttribute("anggotaList", anggotaList);

        //mengambil data parent proyek
        if(proyek.getIdParent() != null) {
            System.out.println("ini parent proyek " + proyekDAO.getProyekById(proyek.getIdParent()).getNamaProyek());
            model.addAttribute("parentproyek",proyekDAO.getProyekById(proyek.getIdParent()));
        }

        model.addAttribute("subproyek",proyekDAO.selectProyekByIdParent(proyek.getId()));
        return "projectlead-detailproyek";
    }

    /**
     * method ini berfungsi untuk menampilkan detil karyawan di proyek tertentu
     * @param model
     * @param idProyek
     * @param idKaryawan
     * @param periode
     * @param notification
     * @return
     */
    @GetMapping("/karyawan/projectlead/detail/{idProyek}/{idKaryawan}")
    public String detailKarproProjlead(Model model, @PathVariable(value = "idProyek") Integer idProyek,
                                       @PathVariable(value = "idKaryawan") Integer idKaryawan,
                                       @RequestParam(value = "periode", required = false) String periode,
                                       @ModelAttribute("notification") String notification) {

        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        LocalDate periodeDate;
        if (periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);
        } else {
            periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        }

        //mengambil rekap, proyek dan karyawan
        ProyekModel proyek = proyekDAO.getProyekById(idProyek);
        KaryawanModel karyawan = karyawanDAO.getKaryawanById(idKaryawan);
        KaryawanRekapModel karyawanRekap = rekapMappingDAO.getRekapBulananKaryawanProyek(idKaryawan, idProyek);

        if (notification != null) {
            model.addAttribute("notification", notification);
        }

        //cek apakah ada rekap
        boolean isEmpty = false;
        if (karyawanRekap.getRekapList() == null || karyawanRekap.getRekapList().isEmpty()) {
            isEmpty = true;
        }

        //passing value
        model.addAttribute("isEmpty", isEmpty);
        model.addAttribute("karyawanRekap", karyawanRekap);
        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));
        model.addAttribute("proyek", proyek);
        model.addAttribute("karyawan", karyawan);

        //mengambil list rating feedback
        List<RatingFeedbackModel> listRF;
        listRF = ratingFeedbackDAO.selectRatingFeedbackKP(karyawanProyekDAO.getKaryawanProyekByKaryawanandProyek(idKaryawan,idProyek).getId());
        System.out.println("ini list rf "+listRF);
        boolean isEmptyRF = false;
        if (listRF.isEmpty()) {
            isEmptyRF = true;
        }
        model.addAttribute("isEmptyRF", isEmptyRF);
        model.addAttribute("listRF", listRF);
        return "projectlead-detailkaryawan";
    }


    /**
     * method ini berfungsi untuk melakukan finalisasi evaluasi diri
     * @param model
     * @param ra
     * @param periode
     * @param idKarProy
     * @return
     */
    @PostMapping(value = "/karyawan/projectlead/detail/finalisasi")
    public String finalisasi(Model model,
                             RedirectAttributes ra,
                             @RequestParam(value = "periode") String periode,
                             @RequestParam(value = "idKarProy") int idKarProy) {
        LocalDate periodeDate = LocalDate.parse(periode, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        KaryawanProyekModel karyawanProyek = karyawanProyekDAO.getKaryawanProyekById(idKarProy);

        System.out.println(idKarProy + " <- id kar proy; periode date -> " + periodeDate);

        RekapModel rekap = rekapDAO.getRekapBulanan(idKarProy, periodeDate);
        rekap.setApproved(true);
        rekapDAO.updateRekap(rekap);

        String notification = "Evaluasi mandiri berhasil difinalisasi";

        ra.addFlashAttribute("notification", notification);

        return "redirect:/karyawan/projectlead/detail/" + karyawanProyek.getIdProyek() + "/" + karyawanProyek.getIdKaryawan();
    }

    @RequestMapping(value = "pmo/detail-karyawan/{idKar}")
    public String showDetailKaryawan(Model model,
                                     @RequestParam(value = "periode", required = false) String periode,
                                     @PathVariable(value = "idKar") int idKaryawan) {
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        LocalDate periodeNow = LocalDate.now();

        LocalDate periodeDate;
        if (periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);
        } else {
            periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        }

        KaryawanModel karyawan = karyawanDAO.getKaryawanById(idKaryawan);

        DivisiModel divisi = divisiDAO.getDivisiByID(karyawan.getIdDivisi());

        KaryawanRekapModel karyawanRekap = rekapMappingDAO.getRekapBulananKaryawan(periodeDate, idKaryawan);

        Map mapRekapProyek = new HashMap();

        List<RekapModel> rekapList = karyawanRekap.getRekapList();

        if (!rekapList.isEmpty()) {
            for (RekapModel rekap : rekapList) {
                ProyekModel proyek = proyekDAO.getProyekById(rekap.getIdProyek());
                mapRekapProyek.put(rekap.getId(), proyek);
            }
        }
        boolean isNow = false;
        if (periodeDate.getYear() == periodeNow.getYear() && periodeDate.getMonth().equals(periodeNow.getMonth())) {
            isNow = true;
        }

        model.addAttribute("isNow", isNow);
        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("isEmpty", rekapList.isEmpty());
        model.addAttribute("karyawanRekap", karyawanRekap);
        model.addAttribute("mapRekapProyek", mapRekapProyek);
        model.addAttribute("divisi", divisi);
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));

        List<KaryawanProyekModel> dicari = karyawanProyekDAO.selectKaryawanProyekByKaryawanPeriode(idKaryawan,periodeDate);
        boolean isEmptyRF = false;
        if(dicari.isEmpty()) {
            isEmptyRF = true;
            return "pmo-detailkaryawan";
        } else {

            List<RatingFeedbackModel> listRF=null;

            for(KaryawanProyekModel karpro : dicari) {
                listRF = ratingFeedbackDAO.selectRatingFeedbackPer(karpro.getId(), periodeDate);
            }

            if (listRF.isEmpty() || listRF==null) {
                isEmptyRF = true;
            }
            model.addAttribute("isEmptyRF", isEmptyRF);
            model.addAttribute("listRF", listRF);

            return "pmo-detailkaryawan";
        }


    }

    /**
     * method ini berfungsi untuk mengubah status proyek
     * @param model
     * @param id
     * @param ra
     * @param newstatus
     * @param principal
     * @return
     */
    @GetMapping("/proyek/status/{id}")
    public String changeStatus(Model model, @PathVariable(value = "id") Integer id,
                               RedirectAttributes ra,
                               @RequestParam(value = "newstatus", required = false) Integer newstatus,
                               Principal principal) {
        String dateToday = rekapMappingDAO.getCurrentDate();
        model.addAttribute("date_today", dateToday);

        System.out.println("masuk controller");

        ProyekModel proyeklama = proyekDAO.getProyekById(id);
        ProyekModel proyekbaru = proyekDAO.getProyekById(id);

        String notification ="";
        //null handling
        if(newstatus==null) {

            return "redirect:/proyek/detail/" + proyeklama.getId();
        }

        LocalDate periodeNow = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        //jika proyek menjadi tidak aktif
        if(newstatus==1) {
            proyekDAO.addProyekStatus(proyeklama.getId(), 1);
            //ngeset end periode jadi skrg
            proyekbaru.setEndPeriode(LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1));
            notification = "Status proyek berhasil diubah menjadi Tidak Aktif";

            //set end periode penugasan
            List<KaryawanProyekModel> listKarPro = karyawanProyekDAO.getKaryawanProyekByProyek(proyekbaru.getId());
            if(!listKarPro.isEmpty()) {
                for (KaryawanProyekModel karyawanProyekItr : listKarPro) {
                    if (karyawanProyekItr.getEndPeriode() == null || karyawanProyekItr.getEndPeriode().compareTo(periodeNow) > 0) {
                        karyawanProyekItr.setEndPeriode(periodeNow);
                        karyawanProyekDAO.updateKaryawanProyek(karyawanProyekItr);
                    }
                }
            }
        }
        //jika proyek menjadi aktif
        if(newstatus==2) {
            System.out.println("masuk ganti status jd 2");
            proyekDAO.addProyekStatus(proyeklama.getId(), 2);
            //ngeset end periode jadi null lagi
            proyekbaru.setEndPeriode(null);
            notification = "Status proyek berhasil diubah menjadi Aktif";

            //set end periode penugasan
            List<KaryawanProyekModel> listKarPro = karyawanProyekDAO.getKaryawanProyekByProyek(proyeklama.getId());
            if(!listKarPro.isEmpty()) {
                for (KaryawanProyekModel karyawanProyekItr : listKarPro) {
                    if (karyawanProyekItr.getEndPeriode().equals(proyeklama.getEndPeriode())) {
                        karyawanProyekItr.setEndPeriode(null);
                        karyawanProyekDAO.updateKaryawanProyek(karyawanProyekItr);
                    }
                }
            }
        }


        proyekDAO.updateProyek(proyekbaru);

        model.addAttribute("notification", notification);

        //add notifikasi flash
        ra.addFlashAttribute("notification", notification);

        return "redirect:/proyek/detail/" + proyeklama.getId();
    }
}