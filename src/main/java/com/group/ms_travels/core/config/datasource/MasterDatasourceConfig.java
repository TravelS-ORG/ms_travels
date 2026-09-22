package com.group.ms_travels.core.config.datasource;

import com.group.ms_travels.core.config.DynamicRoutingDataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = MasterDatasourceConfig.REPOSITORY_BASE_PACKAGE)
public class MasterDatasourceConfig {

    public static final String REPOSITORY_BASE_PACKAGE = "com.group.ms_travels.module";
    public static final String MASTER_SRC_KEY = "MASTER";
    public static final String REPLICA_SRC_KEY = "REPLICA";

    // -------------------------------------------------------------------------
    // MASTER datasource (write)
    // -------------------------------------------------------------------------
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return masterDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // -------------------------------------------------------------------------
    // REPLICA datasource (read-only)
    // -------------------------------------------------------------------------
    @Bean
    @ConfigurationProperties("spring.datasource.replica")
    public DataSourceProperties replicaDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "replicaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.replica.hikari")
    public DataSource replicaDataSource() {
        return replicaDataSourceProperties().initializeDataSourceBuilder().build();
    }

    // -------------------------------------------------------------------------
    // ROUTING datasource
    // -------------------------------------------------------------------------
    @Bean(name = "routingDataSource")
    @Primary
    public DataSource routingDataSource() {
        DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        Map<Object, Object> targets = new HashMap<>();
        targets.put(MASTER_SRC_KEY, masterDataSource());
        targets.put(REPLICA_SRC_KEY, replicaDataSource());
        routingDataSource.setTargetDataSources(targets);
        return routingDataSource;
    }

    // -------------------------------------------------------------------------
    //  EntityManagerFactory — pointed to routingDataSource
    //    (KHÔNG trỏ vào masterDataSource trực tiếp)!!!!!!
    // -------------------------------------------------------------------------
    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("routingDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder,
            @Value("${spring.jpa.hibernate.naming.physical-strategy}") String naming,
            @Value("${spring.jpa.database-platform}") String dialect
    ) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.naming.physical_strategy", naming);
        properties.put("hibernate.dialect", dialect);
        return builder.dataSource(dataSource)
                .packages("com.group.ms_travels.module")
                .properties(properties)
                .persistenceUnit("master")
                .build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }


}
