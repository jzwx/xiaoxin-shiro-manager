package com.xiaoxin.manager.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author:jzwx
 * @Desicription: ShiroManagerDatasourceConfiguration
 * @Date:Created in 2018-11-13 15:37
 * @Modified By:
 */
@Configuration
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource.shiromanager")
@MapperScan(basePackages = "com.xiaoxin.manage.dao", sqlSessionTemplateRef = "shiromanagerSqlSessionTemplate")
public class ShiroManagerDatasourceConfiguration {
    @Autowired
    private ShiroManagerPagehelperConfig     pagehelperConfig;

    @Autowired
    private ShiroManagerDataSourcePropConfig dataSourcePropConfig;

    @Autowired
    private ShiroManagerMybatisPropConfig    mybatisPropConfig;

    /**
     * 获取数据源
     * @return
     * @throws Exception
     */
    @Bean(name = "shiromanagerDataSource")
    public DataSource getShiroManagerDataSource() throws Exception {
        Properties props = new Properties();
        props.put("driverClassName", dataSourcePropConfig.getDriverClassName());
        props.put("url", dataSourcePropConfig.getUrl());
        props.put("username", dataSourcePropConfig.getUsername());
        props.put("password", dataSourcePropConfig.getPassword());
        props.put("type", dataSourcePropConfig.getType());

        props.put("initialSize", dataSourcePropConfig.getInitialSize());
        props.put("minIdle", dataSourcePropConfig.getMinIdle());
        props.put("maxActive", dataSourcePropConfig.getMaxActive());

        props.put("maxWait", dataSourcePropConfig.getMaxWait());

        props.put("validationQuery", dataSourcePropConfig.getValidationQuery());
        props.put("testWhileIdle", dataSourcePropConfig.getTestWhileIdle());
        props.put("testOnBorrow", dataSourcePropConfig.getTestOnBorrow());
        props.put("testOnReturn", dataSourcePropConfig.getTestOnReturn());

        //打开PSCache，并且指定每个连接上PSCache的大小
        props.put("poolPreparedStatements", dataSourcePropConfig.getPoolPreparedStatements());
        props.put("maxPoolPreparedStatementPerConnectionSize",
            dataSourcePropConfig.getMaxPoolPreparedStatementPerConnectionSize());
        return DruidDataSourceFactory.createDataSource(props);
    }

    /**
     * 配置事物管理器
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "shiromanagerTransactionManager")
    public PlatformTransactionManager shiromanagerTransactionManager(@Qualifier("shiromanagerDataSource") DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shiromanagerSqlSessionFactory")
    public SqlSessionFactory shiromanagerSqlSessionFactory(@Qualifier("shiromanagerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
//         RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(env, "mybatis");
        //指定基包
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisPropConfig.getTypeAliasesPackage());
        //指定xml文件位置
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mybatisPropConfig.getMapperLocations()));

        //mybatis分页
        sqlSessionFactoryBean.setPlugins(new Interceptor[] { getPageHelper() });
        return sqlSessionFactoryBean.getObject();

    }

    /**
     * 配置session模板
     *
     * @param sqlSessionFactory
     * @return SqlSessionTemplate
     */
    @Bean(name = "shiromanagerSqlSessionTemplate")
    public SqlSessionTemplate shiromanagerSqlSessionTemplate(@Qualifier("shiromanagerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 构建pageHelper
     *
     * @return
     */
    private PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("dialect", pagehelperConfig.getDialect());
        props.setProperty("params", pagehelperConfig.getParams());
        props.setProperty("offsetAsPageNum", pagehelperConfig.getOffsetAsPageNum());
        props.setProperty("rowBoundsWithCount", pagehelperConfig.getRowBoundsWithCount());
        props.setProperty("pageSizeZero", pagehelperConfig.getPageSizeZero());
        props.setProperty("reasonable", pagehelperConfig.getReasonable());
        props.setProperty("supportMethodsArguments", pagehelperConfig.getSupportMethodsArguments());
        props.setProperty("returnPageInfo", pagehelperConfig.getReturnPageInfo());
        pageHelper.setProperties(props);
        return pageHelper;
    }
}
