package com.pusilkom.hris.service;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.pusilkom.hris.dto.form.cmd.AuthItemChildCmd;
import com.pusilkom.hris.dto.form.cmd.AuthItemCmd;
import com.pusilkom.hris.dto.form.search.AuthItemSearchForm;
import com.pusilkom.hris.dto.table.AuthItemItem;
import com.pusilkom.hris.model.AuthItem;
import com.pusilkom.hris.model.AuthItemChildExample;
import com.pusilkom.hris.model.AuthItemChildKey;
import com.pusilkom.hris.model.AuthItemExample;
import com.pusilkom.hris.model.mapper.AuthItemChildMapper;
import com.pusilkom.hris.model.mapper.AuthItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthItemService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${auth.item.role.id}")
    private Integer roleId;

    @Value("${auth.item.function.id}")
    private Integer functionId;

    @Autowired
    AuthItemMapper authItemMapper;

    @Autowired
    AuthItemChildMapper childMapper;

    public List<AuthItem> getListAuthItem(){
        return authItemMapper.selectByExample(new AuthItemExample());
    }

    public List<AuthItem> getRoleAuthItemList() {
        AuthItemExample example = new AuthItemExample();
        example.createCriteria().andTypeEqualTo(roleId);
        example.setOrderByClause("id");

        return authItemMapper.selectByExample(example);
    }

    public List<AuthItem> getFunctionAuthItemList() {
        AuthItemExample example = new AuthItemExample();
        example.createCriteria().andTypeEqualTo(functionId);
        example.setOrderByClause("id");

        return authItemMapper.selectByExample(example);
    }

    public AuthItemCmd getAuthItemCmdById(Long id) {
        AuthItem authItem = getAuthItemById(id);
        AuthItemCmd authItemCmd = new AuthItemCmd();

        authItemCmd.setId(authItem.getId());
        authItemCmd.setName(authItem.getName());
        authItemCmd.setType(authItem.getType());
        authItemCmd.setDescription(authItem.getDescription());

        if(!StringUtils.isEmpty(authItem.getName())){
            List<String> authItemChild = getAllChildNameByParentId(authItem.getName());
            authItemCmd.setChildList(authItemChild);
        }

        return authItemCmd;
    }

    @Transactional(readOnly = false)
    public int save(AuthItemChildKey authItemChild){
        return childMapper.insert(authItemChild);
    }

    @Transactional(readOnly = false)
    public AuthItem save(AuthItem item) {

        if(item.getId() == null) {
            item.setUserCreate("system");
            authItemMapper.insertWithTimestamp(item);
            logger.debug("New authItem == "+item.getName());
        }
        else {
            item.setUserUpdate("system");
            authItemMapper.updateWithTimestamp(item);
            logger.debug("Update authItem == "+item.getName());
        }

        return item;
    }

    @Transactional(readOnly = false)
    public void delete(String name){
        AuthItemChildExample example = new AuthItemChildExample();
        example.createCriteria().andParentEqualTo(name);

        childMapper.deleteByExample(example);
    }

    @Transactional(readOnly = false)
    public int saveCmd(AuthItemChildCmd authItemChildCmd){
        AuthItemChildKey key = new AuthItemChildKey();
        key.setParent(authItemChildCmd.getParent());
        key.setChild(authItemChildCmd.getChild());

        return this.save(key);
    }

    @Transactional(readOnly = false)
    public void saveCmd(AuthItemCmd authItemCmd){
        AuthItem authItem = new AuthItem();
        AuthItemChildKey authItemChild = new AuthItemChildKey();
        List<String> childItems = null;

        if(authItemCmd.getId() != null){
            authItem = getAuthItemById(authItemCmd.getId());
            childItems = authItemCmd.getChildList();
        }

        authItem.setName(authItemCmd.getName());
        authItem.setType(authItemCmd.getType());
        authItem.setDescription(authItemCmd.getDescription());

        this.save(authItem);

        if(childItems != null){

            this.delete(authItem.getName());

            for(String a : childItems){
                AuthItemChildExample example = new AuthItemChildExample();
                example.createCriteria()
                        .andParentEqualTo(authItemCmd.getName())
                        .andChildEqualTo(a);
                List<AuthItemChildKey> listAuthItemChild = childMapper.selectByExample(example);

                if(listAuthItemChild.size() == 0){
                    authItemChild.setParent(authItemCmd.getName());
                    authItemChild.setChild(a);
                    this.save(authItemChild);
                }
            }
        }
    }

    public AuthItem getAuthItemById(Long id){
        return authItemMapper.selectByPrimaryKey(id);
    }

    public AuthItem getAuthItemByName(String name) {
        AuthItemExample itemExample = new AuthItemExample();
        itemExample.createCriteria()
                .andNameEqualTo(name);

        List<AuthItem> authItems = authItemMapper.selectByExample(itemExample);
        return authItems.size() > 0 ? authItems.get(0) : null;
    }

    @Transactional(readOnly = false)
    public AuthItem createItem(AuthItem item) {
        item.setType(1);
        return save(item);
    }

    @Transactional(readOnly = false)
    public AuthItem createRole(AuthItem item) {
        item.setType(2);
        return save(item);
    }

    @Transactional(readOnly = false)
    public int clearItem() {
        AuthItemExample itemExample = new AuthItemExample();
        itemExample.createCriteria()
                .andTypeEqualTo(Integer.valueOf(1));

        return authItemMapper.deleteByExample(itemExample);
    }

    public List<String> getAllChildNameByParentId(String parent) {

        List<String> authItems = new ArrayList<>();

        AuthItemChildExample childExample = new AuthItemChildExample();
        childExample.createCriteria()
                .andParentEqualTo(parent);
        List<AuthItemChildKey> childKeys = childMapper.selectByExample(childExample);
        if(childKeys.size() > 0) {
            for (AuthItemChildKey child : childKeys) {
                authItems.add(child.getChild());
            }
        }

        return authItems;
    }

//    public DataSet<AuthItemChildItem> getDataSet(AuthItemChildSearchForm searchForm){
//        List<AuthItemChildItem> listItem = childMapper.getListAuthItemChildItemBySearchForm(searchForm);
//        Long totalItemFiltered = childMapper.getTotalAuthItemChildItemBySearchForm(searchForm);
//        Long totalItem = totalItemFiltered;
//
//        return new DataSet<>(listItem, totalItem, totalItemFiltered);
//    }

    public DataSet<AuthItemItem> getDataSet(AuthItemSearchForm searchForm){
        searchForm.setType(roleId);

        List<AuthItemItem> listItem = authItemMapper.getListAuthItemItemBySearchForm(searchForm);
        Long totalItemFiltered = authItemMapper.getTotalAuthItemItemBySearchForm(searchForm);
        Long totalItem = totalItemFiltered;

        return new DataSet<>(listItem, totalItem, totalItemFiltered);
    }

    @Transactional(readOnly = false)
    public int deleteAuthItemChild(String id){

        String[] criteria = id.split("[|]");

        AuthItemChildExample example = new AuthItemChildExample();
        example.createCriteria().andParentEqualTo(criteria[0]).andChildEqualTo(criteria[1]);
        return childMapper.deleteByExample(example);
    }
}
