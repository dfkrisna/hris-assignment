package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.*;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class AssignmentController {
    @Autowired
    KaryawanService karyawanService;

    @Autowired
    ProyekService proyekService;

    @Autowired
    RoleProyekService roleProyekService;

    @Autowired
    KaryawanProyekService karyawanProyekService;

    @Autowired
    RekapService rekapService;

    @Autowired
    RekapMappingService rekapMappingService;

    @Autowired
    PenugasanService penugasanService;

    /**
     * method ini digunakan untuk menampilkan form tambah penugasan karyawan pada suatu proyek melalui matriks
     * @param model
     * @param tahun
     * @param bulan
     * @param idKaryawan
     * @param idProyek
     * @return
     */
    @GetMapping("/pmo/assign/{idKaryawan}/{idProyek}/{tahun}/{bulan}")
    public String assignKaryawanMatrix(Model model,
                                       @PathVariable(value = "tahun", required = true) Integer tahun,
                                       @PathVariable(value = "bulan", required = true) Integer bulan,
                                       @PathVariable("idKaryawan") Integer idKaryawan,
                                       @PathVariable("idProyek") Integer idProyek) {
        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);
        ProyekModel proyek = proyekService.getProyekById(idProyek);
        List<RoleProyekModel> roles = roleProyekService.getAllRoleProyek();
        KaryawanProyekModel karyawanProyek = new KaryawanProyekModel();


        LocalDate periode = LocalDate.of(tahun, bulan, 1);

        String periodeString = periode.getMonth().toString() + " " + periode.getYear();

        karyawanProyek.setStartPeriode(periode);

        boolean hasProjectLead = proyekService.hasProjectLead(idProyek);

        if(hasProjectLead) {
            RoleProyekModel roleProjectLead = roleProyekService.getRoleByName("Project Lead");
            roles.remove(roleProjectLead);
        }

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("proyek", proyek);
        model.addAttribute("roles", roles);
        model.addAttribute("karyawanProyek", karyawanProyek);
        model.addAttribute("periodeMulai", periodeString);

        return "pmo-form-assign-add-matrix";
    }

    /**
     * method ini digunakan untuk menyimpan penugasan baru karyawan pada suatu proyek melalui matriks
     * @param model
     * @param idKaryawan
     * @param idProyek
     * @param persenKontribusi
     * @param idRole
     * @param periodeMulai
     * @param periodeSelesai
     * @return
     */
    @PostMapping(value = "/pmo/assign/{idKaryawan}/{idProyek}")
    public String saveNewAssignmentMatrix(Model model,
                                          RedirectAttributes ra,
                                          @PathVariable("idKaryawan") Integer idKaryawan,
                                          @PathVariable("idProyek") Integer idProyek,
                                          @RequestParam(value = "persenKontribusi") double persenKontribusi,
                                          @RequestParam(value = "role") int idRole,
                                          @RequestParam(value = "periodeMulai") String periodeMulai,
                                          @RequestParam(value = "periodeSelesai") String periodeSelesai) {
        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);
        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);

        if(karyawanProyek == null) {
            karyawanProyek = new KaryawanProyekModel();
            System.out.println("masuk gaa");
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive() .appendPattern("d MMMM yyyy").toFormatter(Locale.ENGLISH);

        if(!periodeMulai.equals("")) {
            LocalDate startperiode = LocalDate.parse("1 " + periodeMulai, formatter);
            karyawanProyek.setStartPeriode(LocalDate.of(startperiode.getYear(), startperiode.getMonthValue(), 1));
        }

        if(!periodeSelesai.equals("")) {
            LocalDate endPeriode = LocalDate.parse("1 " + periodeSelesai, formatter);
            karyawanProyek.setEndPeriode(LocalDate.of(endPeriode.getYear(), endPeriode.getMonthValue(), 1));
        }

        System.out.println("ini hasil nyari db " + karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek));

        if(karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek) == null) {
            karyawanProyekService.addKaryawanProyekMatrix(karyawanProyek, idKaryawan, idProyek, idRole);
            System.out.println("knp masuk gaa");
        } else {
            karyawanProyek.setEndPeriode(null);
            karyawanProyek.setStartPeriode(karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek).getStartPeriode());
            System.out.println("alhamdulillah gaa");

            karyawanProyekService.updateKaryawanProyek(karyawanProyek);
        }

        KaryawanProyekModel karyawanAdded = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);

        String namaRole = roleProyekService.getRoleNameByID(idRole);
        if(namaRole.equalsIgnoreCase("project lead")) {
            ProyekModel proyek = proyekService.getProyekById(idProyek);
            proyek.setIdProjectLead(karyawanAdded.getId());
            proyekService.updateProyek(proyek);
        }

        RekapModel rekap = new RekapModel(0, karyawanAdded.getId(), null, persenKontribusi, false, null, null, idProyek, idKaryawan);

        rekapService.addRekapMatrix(rekap, karyawanAdded);

        ra.addFlashAttribute("notification", "Penugasan Berhasil Ditambahkan");

        return "redirect:/pmo";
    }

    /**
     * method ini digunakan untuk menampilkan form ubah penugasan karyawan pada suatu proyek melalui matriks
     * @param model
     * @param tahun
     * @param bulan
     * @param idKaryawanProyek
     * @return
     */
    @RequestMapping(value = "/pmo/update_assignment/{idKaryawanProyek}/{tahun}/{bulan}")
    public String updateAssignmentMatrix(Model model,
                                         @PathVariable(value = "tahun", required = true) Integer tahun,
                                         @PathVariable(value = "bulan", required = true) Integer bulan,
                                         @PathVariable("idKaryawanProyek") Integer idKaryawanProyek) {
        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekById(idKaryawanProyek);
        KaryawanModel karyawan = karyawanService.getKaryawanById(karyawanProyek.getIdKaryawan());
        ProyekModel proyek = proyekService.getProyekById(karyawanProyek.getIdProyek());
        List<RoleProyekModel> roles = roleProyekService.getAllRoleProyek();

        LocalDate periode = LocalDate.of(tahun, bulan, 1);

        LocalDate now = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        RekapModel rekapNow = rekapService.getRekapBulanan(idKaryawanProyek, periode);

        String stringStartPeriode = karyawanProyek.getStartPeriode().getMonth().toString() + " " + karyawanProyek.getStartPeriode().getYear();

        RoleProyekModel roleKaryawanProyek = roleProyekService.getRoleByName(roleProyekService.getRoleNameByID(karyawanProyek.getIdRole()));

        model.addAttribute("karyawan", karyawan);
        model.addAttribute("proyek", proyek);
        model.addAttribute("role", roleKaryawanProyek);
        model.addAttribute("karyawanProyek", karyawanProyek);
        model.addAttribute("rekap", rekapNow);
        model.addAttribute("startPeriode", stringStartPeriode);

        if(karyawanProyek.getEndPeriode() != null) {
            String stringEndPeriode = karyawanProyek.getEndPeriode().getMonth().toString() + " " + karyawanProyek.getEndPeriode().getYear();
            model.addAttribute("endPeriode", stringEndPeriode);
        }

        model.addAttribute("periode", periode);
        model.addAttribute("now", now);

        return "pmo-form-assign-upd-matrix";
    }

    /**
     * method ini digunakan untuk menyimpan perubahan penugasan karyawan pada suatu proyek melalui matriks
     * @param model
     * @param ra
     * @param idKaryawanProyek
     * @param persenKontribusi
     * @param periodeRekap
     * @param action
     * @param endPeriode
     * @return
     */
    @RequestMapping(value = "/pmo/update_assignment/{idKaryawanProyek}",method = RequestMethod.POST)
    public String updateAssignmentMatrix(Model model,
                                         RedirectAttributes ra,
                                         @PathVariable("idKaryawanProyek") Integer idKaryawanProyek,
                                         @RequestParam(value = "persenKontribusi") double persenKontribusi,
                                         @RequestParam(value = "periodeRekap") String periodeRekap,
                                         @RequestParam(value="action") String action,
                                         //@RequestParam(value = "startPeriode", required = false) String startPeriode,
                                         @RequestParam(value = "endPeriode", required = false) String endPeriode) {
        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekById(idKaryawanProyek);

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive() .appendPattern("d MMMM yyyy").toFormatter(Locale.ENGLISH);

        LocalDate periodeRekapDate = null;

        if(!periodeRekap.equals("")) {
            periodeRekapDate = LocalDate.parse(periodeRekap, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if(action.equals("end")) {
                karyawanProyek.setEndPeriode(periodeRekapDate);
            }
        }

        LocalDate periodeSelesaiDate = null;

        if(!endPeriode.equals("")) {
            String stringEndPeriode = "1 " + endPeriode;
            System.out.println(stringEndPeriode + " ini periode selesai date");
            periodeSelesaiDate = LocalDate.parse(stringEndPeriode, formatter);
            karyawanProyek.setEndPeriode(periodeSelesaiDate);
        }

        karyawanProyekService.updateKaryawanProyek(karyawanProyek);

        RekapModel retrievedRekap = rekapService.getRekapBulanan(karyawanProyek.getId(), periodeRekapDate);

        retrievedRekap.setPersentaseKontribusi(persenKontribusi);

        rekapService.updateRekap(retrievedRekap);

        ra.addFlashAttribute("notification", "Penugasan Berhasil Diubah");

        return "redirect:/pmo";
    }

    /**
     * method ini digunakan untuk menampilkan daftar rekomendasi karyawan setelah membuat proyek
     * @param model
     * @param idProyek
     * @return
     */
    @GetMapping("/pmo/proyek/tambah/assign/{idProyek}")
    public String assignKaryawan(Model model, @PathVariable(value = "idProyek") int idProyek) {
        ProyekModel proyek = proyekService.getProyekById(idProyek);

        LocalDate periode = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);

        List<KaryawanProyekModel> karyawanProyekList = karyawanProyekService.getKaryawanProyekByPeriode(periode);
        List<ProyekModel> proyekList = proyekService.getProyekByPeriode(periode);
        List<KaryawanModel> karyawanList = karyawanService.getKaryawanAll();
        List<RekapModel> rekapList = rekapService.getRekapByPeriode(periode);
        List<KaryawanRekapModel> mapping = rekapMappingService.mapRekap(karyawanList, proyekList, karyawanProyekList, rekapList);

        model.addAttribute("proyek", proyek);
        model.addAttribute("current", periode);
        model.addAttribute("mapping", mapping);

        return "pmo-karyawan-recommendation";
    }

    /**
     * method ini digunakan untuk menampilkan daftar form penugasan baru karyawan pada suatu proyek setelah proyek dibuat
     * @param model
     * @param idProyek
     * @param karyawanIDList
     * @return
     */
    @PostMapping(value = "pmo/proyek/tambah/assign/{idProyek}")
    public String assignKaryawan(Model model, @PathVariable(value = "idProyek") int idProyek,
                                 @RequestParam(value = "selectedKaryawan") List<Integer> karyawanIDList) {
        List<KaryawanModel> karyawanList = new ArrayList<KaryawanModel>();
        for(int karyawanID : karyawanIDList) {
            KaryawanModel karyawan = karyawanService.getKaryawanById(karyawanID);
            karyawanList.add(karyawan);
        }

        ProyekModel proyek = proyekService.getProyekById(idProyek);

        List<RoleProyekModel> roles = roleProyekService.getAllRoleProyek();

        boolean hasProjectLead = proyekService.hasProjectLead(idProyek);

        if(hasProjectLead) {
            RoleProyekModel roleProjectLead = roleProyekService.getRoleByName("Project Lead");
            roles.remove(roleProjectLead);
        }

        model.addAttribute("roles", roles);
        model.addAttribute("karyawanList", karyawanList);
        model.addAttribute("karyawanIDList", karyawanIDList);
        model.addAttribute("proyek", proyek);
        return "pmo-form-assign-list";
    }

    /**
     * method ini digunakan untuk menyimpan penugasan yang dibuat setelah proyek dibuat
     * @param model
     * @param idProyek
     * @param idKaryawan
     * @param listKaryawanID
     * @param persenKontribusi
     * @param idRole
     * @param periodeMulai
     * @param periodeSelesai
     * @return
     */
    @PostMapping(value = "pmo/proyek/tambah/assign/{idKaryawan}/{idProyek}")
    public String assignKaryawan(Model model, @PathVariable(value = "idKaryawan") int idKaryawan,
                                 @PathVariable(value = "idProyek") int idProyek,
                                 @RequestParam(value = "listKaryawanID", required = false) String listKaryawanID,
                                 @RequestParam(value = "persenKontribusi") double persenKontribusi,
                                 @RequestParam(value = "role") int idRole,
                                 @RequestParam(value = "periodeMulai") String periodeMulai,
                                 @RequestParam(value = "periodeSelesai") String periodeSelesai) {
        List<Integer> karyawanIDList = new ArrayList<Integer>();

        if(listKaryawanID.length() > 2) {
            listKaryawanID = listKaryawanID.substring(1, listKaryawanID.length()-1);
            List<String> karyawanIDListString = new ArrayList<String>((Arrays.asList(listKaryawanID.split(", "))));
            for(String karyawanIDString : karyawanIDListString) {
                int a = Integer.parseInt(karyawanIDString);
                karyawanIDList.add(a);
            }
        }

        KaryawanModel karyawan = karyawanService.getKaryawanById(idKaryawan);
        ProyekModel proyek = proyekService.getProyekById(idProyek);

        KaryawanProyekModel karyawanProyek = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);

        if(karyawanProyek == null) {
            karyawanProyek = new KaryawanProyekModel();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");

        LocalDate periodeMulaiDate = null;
        LocalDate periodeSelesaiDate = null;

        if(!periodeMulai.equals("")) {
            periodeMulaiDate = LocalDate.parse("1 " + periodeMulai, formatter);
            karyawanProyek.setStartPeriode(LocalDate.of(periodeMulaiDate.getYear(), periodeMulaiDate.getMonthValue(), 1));
        }

        if(!periodeSelesai.equals("")) {
            periodeSelesaiDate = LocalDate.parse("1 " + periodeSelesai, formatter);
            karyawanProyek.setEndPeriode(LocalDate.of(periodeSelesaiDate.getYear(), periodeSelesaiDate.getMonthValue(), 1));
        }

        if(karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek) == null) {
            karyawanProyekService.addKaryawanProyekMatrix(karyawanProyek, idKaryawan, idProyek, idRole);
        } else {
            karyawanProyek.setEndPeriode(null);
            karyawanProyek.setStartPeriode(karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek).getStartPeriode());
            karyawanProyekService.updateKaryawanProyek(karyawanProyek);
        }

        KaryawanProyekModel karyawanAdded = karyawanProyekService.getKaryawanProyekByKaryawanandProyek(idKaryawan, idProyek);

        String namaRole = roleProyekService.getRoleNameByID(idRole);
        if(namaRole.equalsIgnoreCase("project lead")) {
            proyek.setIdProjectLead(karyawanAdded.getId());
            proyekService.updateProyek(proyek);
        }

        RekapModel rekap = new RekapModel(0, karyawanAdded.getId(), null, persenKontribusi, false, null, null, idProyek, idKaryawan);

        rekapService.addRekapMatrix(rekap, karyawanAdded);

        System.out.println("size array = " + karyawanIDList.size());
        karyawanIDList.remove((Integer) idKaryawan);

        if(karyawanIDList.size() != 0) {
            List<RoleProyekModel> roles = roleProyekService.getAllRoleProyek();

            List<KaryawanModel> karyawanList = new ArrayList<KaryawanModel>();
            for(int karyawanID : karyawanIDList) {
                KaryawanModel k1 = karyawanService.getKaryawanById(karyawanID);
                karyawanList.add(k1);
            }

            model.addAttribute("roles", roles);
            model.addAttribute("karyawanList", karyawanList);
            model.addAttribute("karyawanIDList", karyawanIDList);
            model.addAttribute("proyek", proyek);
            return "pmo-form-assign-list";
        } else {
            model.addAttribute("proyek",proyek);

            String periodemulai = "";
            String periodeselesai ="";

            if(proyek.getStartPeriode() != null) {
                periodemulai = proyek.getStartPeriode().getMonth().toString() + " " + proyek.getStartPeriode().getYear();
            }

            if(proyek.getEndPeriode() != null) {
                periodemulai = proyek.getEndPeriode().getMonth().toString() + " " + proyek.getEndPeriode().getYear();
            }

            String dateToday = rekapMappingService.getCurrentDate();
            model.addAttribute("date_today",dateToday);

            model.addAttribute("status", penugasanService.getLatestStatus(proyek.getId()));
            model.addAttribute("startdate",periodemulai);
            model.addAttribute("enddate",periodeselesai);
            model.addAttribute("action", "add");
            model.addAttribute("notification", "Penugasan Berhasil Ditambahkan");

            return "pmo-detail-proyek";
        }
    }
}