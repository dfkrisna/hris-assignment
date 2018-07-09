package com.pusilkom.hris.model.mapper;

import com.pusilkom.hris.dto.form.search.AuthItemChildSearchForm;
import com.pusilkom.hris.dto.table.AuthItemChildItem;
import com.pusilkom.hris.model.AuthItemChildExample;
import com.pusilkom.hris.model.AuthItemChildKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface AuthItemChildMapper extends BaseMapper<AuthItemChildKey, Void, AuthItemChildExample> {

    List<AuthItemChildItem> getListAuthItemChildItemBySearchForm(@Param("searchForm") AuthItemChildSearchForm authItemChildSearchForm);
    Long getTotalAuthItemChildItemBySearchForm(@Param("searchForm") AuthItemChildSearchForm authItemChildSearchForm);
}
