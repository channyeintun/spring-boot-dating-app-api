package com.pledge.app.config.db;

import com.pledge.app.annotation.ReadOnlyRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.pledge.app",
        includeFilters = @ComponentScan.Filter(ReadOnlyRepository.class),
        entityManagerFactoryRef = "readOnlyEntityManagerFactory",
        transactionManagerRef = "readOnlyTransactionManager"
)
public class ReadOnlyDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("app.datasource.secondary")
    public DataSourceProperties readOnlyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.secondary.configuration")
    public DataSource readOnlyDataSource() {
        return readOnlyDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }

    @Bean(name = "readOnlyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean readOnlyEntityManagerFactory(
            @Qualifier("readOnlyDataSource") DataSource readOnlyDataSource,
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(readOnlyDataSource)
                .packages("com.pledge.app")
                .build();
    }

    @Bean
    public PlatformTransactionManager readOnlyTransactionManager(
            final @Qualifier("readOnlyEntityManagerFactory") LocalContainerEntityManagerFactoryBean readOnlyEntityManagerFactory) {
        return new JpaTransactionManager(readOnlyEntityManagerFactory.getObject());
    }
}
