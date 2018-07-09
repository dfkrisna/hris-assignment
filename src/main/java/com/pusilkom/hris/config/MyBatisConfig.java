package com.pusilkom.hris.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class MyBatisConfig {
    @Bean
    public static SqlSessionFactory sqlSessionFactoryContainer(@Autowired DataSource dataSourceContainer) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();

        sqlSessionFactory.setDataSource(dataSourceContainer);
        sqlSessionFactory.setTypeAliasesPackage("com.pusilkom.hris.model");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:com/pusilkom/hris/model/mapper/**/*.xml"));
        return (SqlSessionFactory) sqlSessionFactory.getObject();
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurerContainer() throws Exception{
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.pusilkom.hris.model.mapper");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryContainer");

        return mapperScannerConfigurer;
    }
}
