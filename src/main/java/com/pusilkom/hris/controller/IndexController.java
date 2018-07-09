package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice
public class IndexController
{
    @Autowired
    ProyekService proyekService;

    @Autowired
    KaryawanProyekService karyawanProyekService;

    @Autowired
    KaryawanService karyawanService;

    @Autowired
    RekapService rekapService;

    @Autowired
    PenggunaService penggunaDAO;

    @Autowired
    RekapMappingService rekapMappingService;

    @Autowired
    UserService userService;

    @Autowired
    PenugasanService penugasanService;

    @Autowired
    DivisiService divisiService;

    @Autowired
    RatingFeedbackService ratingFeedbackService;

    /**
     * method untuk mengakses beranda utama
     * @param model
     * @param auth
     * @return
     */
    @GetMapping("/")
    @PreAuthorize("hasAuthority('GET_')")
    public String index(Model model, @NotNull Authentication auth) {
        UserWeb user = (UserWeb) auth.getPrincipal();
        model.addAttribute("currentUser", user);
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "site/login";
    }

    /**
     * method untuk mendapatkan user yang saat ini login
     * @param auth
     * @return
     */
    @ModelAttribute("currentUser")
    public UserWeb getLoggedInUser(@NotNull Authentication auth) {
        UserWeb user = (UserWeb) auth.getPrincipal();
        return user;
    }

    @RequestMapping("/login")
    public String login (Model model,
                         @RequestParam(value="error", required = false) String error)
    {
        if(error != null) {
            System.out.println(error);
        }
        return "login";
    }

    /**
     * method untuk menampilkan beranda eksekutif yang berisi matriks penugasan
     * @param model
     * @param periode
     * @return
     */
    @GetMapping(value = "/eksekutif")
    public String indexEksekutif (Model model, @RequestParam(value = "periode", required = false) String periode)
    {
        LocalDate periodeDate;
        if(periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);
        } else { periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1); }
        List<ProyekModel> proyekList = proyekService.getProyekByPeriode(periodeDate);
        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));

        if(proyekList.size() > 0) {
            List<KaryawanModel> karyawanList = karyawanService.getKaryawanAll();
            List<KaryawanProyekModel> karyawanProyekList = karyawanProyekService.getKaryawanProyekByPeriode(periodeDate);
            List<RekapModel> rekapList = rekapService.getRekapByPeriode(periodeDate);
            List<KaryawanRekapModel> mapping = rekapMappingService.mapRekap(karyawanList, proyekList, karyawanProyekList, rekapList);
            int totalPerc = rekapMappingService.totalPercentage(mapping);
            int[] chartValue = rekapMappingService.chartValue(mapping);
            int chartSize = karyawanList.size();

            model.addAttribute("proyekList", proyekList);
            model.addAttribute("mapping", mapping);
            model.addAttribute("chartValue", chartValue);
            model.addAttribute("chartSize", chartSize);
            model.addAttribute("totalPercentage", totalPerc);
            model.addAttribute("totalGreen", (int) (255 - (2.55 * totalPerc)));
            model.addAttribute("totalRed", (int) (2.55 * totalPerc));

            return "index-eksekutif";
        } else {
            return "index-eksekutif-empty";
        }
    }

    /**
     * method untuk menampilkan beranda admin
     * @param model
     * @return
     */
    @GetMapping("/admin")
    public String indexAdmin (Model model)
    {
        String dateToday = rekapMappingService.getCurrentDate();
        List<PenggunaModel> penggunas = penggunaDAO.getListPengguna();
        model.addAttribute("penggunas",penggunas);
        model.addAttribute("date_today",dateToday);
        model.addAttribute("page_title","Beranda Admin");

        return "index-admin";
    }

    /**
     * method ini digunakan untuk menampilkan index PMO yang berisi matriks penugasan
     * @param model
     * @param periode
     * @param notification
     * @return
     */
    @GetMapping("/pmo")
    public String indexPMO (Model model,
                            @RequestParam(value = "periode", required = false) String periode,
                            @ModelAttribute("notification") String notification)
    {
        LocalDate periodeNow = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

        LocalDate periodeDate;
        if(periode != null) {
            String[] split = periode.split(" ");
            periodeDate = LocalDate.of(Integer.parseInt(split[1]), Month.valueOf(split[0].toUpperCase()), 1);

            LocalDate prevPeriode = periodeDate.minusMonths(1);
            rekapService.populatePrevRekap(prevPeriode, periodeDate);
        } else { periodeDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1); }

        List<KaryawanProyekModel> karyawanProyekList = karyawanProyekService.getKaryawanProyekByPeriode(periodeDate);
        List<ProyekModel> proyekList = proyekService.getProyekByPeriode(periodeDate);
        List<KaryawanModel> karyawanList = karyawanService.getKaryawanAll();
        List<RekapModel> rekapList = rekapService.getRekapByPeriode(periodeDate);
        List<KaryawanRekapModel> mapping = rekapMappingService.mapRekap(karyawanList, proyekList, karyawanProyekList, rekapList);
        int totalPerc = rekapMappingService.totalPercentage(mapping);
        int[] chartValue = rekapMappingService.chartValue(mapping);
        int chartSize = karyawanList.size();

        String dateToday = rekapMappingService.getCurrentDate();

        if(notification != null) {
            model.addAttribute("notification", notification);
        }

        model.addAttribute("date_today", dateToday);
        model.addAttribute("proyekList", proyekList);
        model.addAttribute("mapping", mapping);
        model.addAttribute("chartValue", chartValue);
        model.addAttribute("chartSize", chartSize);
        model.addAttribute("totalPercentage", totalPerc);
        model.addAttribute("totalGreen", (int) (255 - (2.55*totalPerc)));
        model.addAttribute("totalRed", (int) (2.55*totalPerc));
        model.addAttribute("next", periodeDate.plusMonths(1));
        model.addAttribute("current", periodeDate);
        model.addAttribute("previous", periodeDate.minusMonths(1));
        model.addAttribute("periodeNow", periodeNow);
        return "index-pmo";
    }

    /**
     * method ini digunakan untuk menampilkan index manajer divisi yang berisi daftar anggota di divisinya
     * @param model
     * @param principal
     * @return
     */
    @GetMapping("/mngdivisi")
    public String indexManajerDivisi(Model model, Principal principal) {
        PenggunaModel pengguna = penggunaDAO.getPenggunaLama(principal.getName());

        DivisiModel divisi = divisiService.getDivisiByManajer(pengguna.getId());

        LocalDate periodeDate = LocalDate.now();

        List<KaryawanModel> karyawanList = karyawanService.getKaryawanByDivisi(divisi.getId());

        Map mapKaryawanRating = new HashMap();

        for(KaryawanModel karyawan:karyawanList) {
            List<PenugasanModel> penugasanList = penugasanService.getPenugasanList(karyawan.getId());
            int ratingKaryawan = ratingFeedbackService.getAvgRatingKaryawan(karyawan.getId());
//            int ratingKaryawan = penugasanService.getAverageRating(penugasanList);
            mapKaryawanRating.put(karyawan.getId(), ratingKaryawan);
            System.out.println("size penugasan = " + penugasanList.size() + "rating karyawan = " + ratingKaryawan);
        }

        String dateToday = rekapMappingService.getCurrentDate();

        model.addAttribute("date_today", dateToday);
        model.addAttribute("listKaryawan", karyawanList);
        model.addAttribute("mapping", mapKaryawanRating);
        model.addAttribute("divisi", divisi);
        return "index-manajerdivisi";
    }

}
