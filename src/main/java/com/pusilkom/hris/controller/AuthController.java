package com.pusilkom.hris.controller;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.pusilkom.hris.service.AuthItemService;
import com.pusilkom.hris.validator.cmd.AuthItemChildCmdValidator;
import com.pusilkom.hris.dto.form.cmd.AuthItemChildCmd;
import com.pusilkom.hris.dto.form.cmd.AuthItemCmd;
import com.pusilkom.hris.dto.form.search.AuthItemSearchForm;
import com.pusilkom.hris.dto.table.AuthItemItem;
import com.pusilkom.hris.model.AuthItem;
import com.pusilkom.hris.service.AuthItemService;
import com.pusilkom.hris.util.DebugUtil;
import com.pusilkom.hris.validator.cmd.AuthItemChildCmdValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class AuthController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DebugUtil d;

    @Autowired
    AuthItemService authItemService;

    @Autowired
    AuthItemChildCmdValidator authItemChildCmdValidator;

    @GetMapping("auth/manage")
    public String indexGet(AuthItemSearchForm searchform, BindingResult result, Model uiModel){
        List<AuthItem> roleAuthItemList = authItemService.getRoleAuthItemList();
        List<AuthItem> functionAuthItemList = authItemService.getFunctionAuthItemList();

        if(result.hasErrors()){
             return "auth/manage";
         }

        uiModel.addAttribute("roleAuthItemList",roleAuthItemList);
        uiModel.addAttribute("functionAuthItemList",functionAuthItemList);

        return "auth/manage";
    }


    @RequestMapping(value = "auth/table", method = RequestMethod.POST)
    @ResponseBody
    public DatatablesResponse<AuthItemItem> postTableSearch(AuthItemSearchForm searchForm, HttpServletRequest httpServletRequest) {

        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(httpServletRequest);
        searchForm.setCriterias(criterias);

        DataSet<AuthItemItem> dataSet = null;
        try{
            log.info(">>> searchForm : {}", d.toString(searchForm));

            dataSet = authItemService.getDataSet(searchForm);
        } catch (Exception e){
            log.error("TABLE AUTHITEM :", e);
        }

        return DatatablesResponse.build(dataSet, criterias);
    }

    @GetMapping(value = "auth/create")
    public String getAuthAdd(Model uiModel) {
        List<AuthItem> roleAuthItemList = authItemService.getRoleAuthItemList();
        List<AuthItem> functionAuthItemList = authItemService.getFunctionAuthItemList();
        AuthItemChildCmd authItemChildCmd = new AuthItemChildCmd();

        uiModel.addAttribute("roleAuthItemList",roleAuthItemList);
        uiModel.addAttribute("functionAuthItemList",functionAuthItemList);
        uiModel.addAttribute("authItemChildCmd", authItemChildCmd);

        return "auth/create";
    }

    @PostMapping(value = "auth/create")
    public String postAuthAdd(@Valid AuthItemChildCmd authItemChildCmd, BindingResult result, RedirectAttributes attributes) {

        authItemChildCmdValidator.validate(authItemChildCmd, result);

        if (result.hasErrors()) {
            return "auth/create";
        }

        if (authItemService.saveCmd(authItemChildCmd) > 0 ) {
            attributes.addFlashAttribute("SUCCESS", "Berhasil tambah hak akses");
            return "redirect:/auth/manage";
        }
        else {
            attributes.addFlashAttribute("ERROR", "Gagal tambah hak akses");
            return "redirect:/auth/create";
        }
    }

    @GetMapping(value = "auth/update/{id}")
    public String getAuthEdit(@PathVariable Long id, Model uiModel, RedirectAttributes attributes) {
        List<AuthItem> functionAuthItemList = authItemService.getFunctionAuthItemList();
        AuthItemCmd authItemCmd = authItemService.getAuthItemCmdById(id);

        if(authItemCmd == null){
            attributes.addAttribute("ERROR", "Peran tidak ditemukan");
            return "redirect:/auth/manage";
        }

        uiModel.addAttribute("functionAuthItemList",functionAuthItemList);
        uiModel.addAttribute("authItemCmd", authItemCmd);

        return "auth/edit";
    }

    @PostMapping(value = "auth/edit/{id}")
    public String postAuthEdit(AuthItemCmd authItemCmd, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "auth/edit";
        }

        try {
            authItemService.saveCmd(authItemCmd);
        } catch (Exception e) {
            attributes.addFlashAttribute("ERROR", "Gagal ubah hak akses");
            return "redirect:/auth/edit";
        }

        attributes.addFlashAttribute("SUCCESS", "Berhasil ubah hak akses");
        return "redirect:/auth/manage";
    }

    @RequestMapping(value = "/auth/delete", method = RequestMethod.POST)
    @ResponseBody
    public boolean postUserDeactivate(String id){
        if (authItemService.deleteAuthItemChild(id) > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
