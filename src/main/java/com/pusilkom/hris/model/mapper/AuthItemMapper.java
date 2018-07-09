package com.pusilkom.hris.model.mapper;

import com.pusilkom.hris.dto.form.search.AuthItemSearchForm;
import com.pusilkom.hris.dto.table.AuthItemItem;
import com.pusilkom.hris.model.AuthItem;
import com.pusilkom.hris.model.AuthItemExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AuthItemMapper extends BaseMapper<AuthItem, Long, AuthItemExample> {

    void insertWithTimestamp(AuthItem record);
    void updateWithTimestamp(AuthItem record);

    List<AuthItemItem> getListAuthItemItemBySearchForm(@Param("searchForm") AuthItemSearchForm authItemSearchForm);
    Long getTotalAuthItemItemBySearchForm(@Param("searchForm") AuthItemSearchForm authItemSearchForm);
}
