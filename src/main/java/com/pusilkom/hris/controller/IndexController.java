package com.pusilkom.hris.controller;

import com.pusilkom.hris.model.*;
import com.pusilkom.hris.service.KaryawanProyekService;
import com.pusilkom.hris.service.KaryawanService;
import com.pusilkom.hris.service.ProyekService;
import com.pusilkom.hris.service.RekapService;
import com.pusilkom.hris.util.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class IndexController
{
    @Autowired
    ProyekService proyekDAO;

    @Autowired
    KaryawanProyekService karyawanProyekDAO;

    @Autowired
    KaryawanService karyawanDAO;

    @Autowired
    RekapService rekapDAO;


    @RequestMapping("/")
    public String index ()
    {
        return "index";
    }

    //Placeholder untuk testing sebelum ada roles
    @RequestMapping("/eksekutif")
    public String indexEksekutif (Model model)
    {
        List<ProyekModel> proyekList = proyekDAO.getProyekByPeriode(3);
        List<KaryawanModel> karyawanList = karyawanDAO.getKaryawanAll();
        List<KaryawanProyekModel> karyawanProyekList = karyawanProyekDAO.getKaryawanProyekByPeriode(3);
        List<RekapModel> rekapList = rekapDAO.getRekapByPeriode(3);

        List<KaryawanMappingModel> mapping = MappingUtil.mapRekap(karyawanList, proyekList, karyawanProyekList, rekapList);
        Collections.sort(mapping);
        int[] chartValue = MappingUtil.chartValue(mapping);
        int chartSize = karyawanList.size();

        model.addAttribute("proyekList", proyekList);
        model.addAttribute("mapping", mapping);
        model.addAttribute("chartValue", chartValue);
        model.addAttribute("chartSize", chartSize);
        return "index-eksekutif";
    }


}
