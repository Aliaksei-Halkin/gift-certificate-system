package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * The configuration for repository layer
 *
 * @author Aliaksei Halkin
 */
@Configuration
@ComponentScan("com.epam.esm")
@ConfigurationProperties("spring.c3p0")
public class RepositoryConfig {
    private static final String PACKAGE_TO_SCAN = "com.epam.esm.entity";
    private String driverClass;
    private String jdbcUrl;
    private String user;
    private String password;
    private int initialPoolSize;
    private int maxPoolSize;

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        this.initialPoolSize = initialPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * The first configuration- a dev, for development
     */
    @Bean
    @Profile("dev")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource developmentDataSource() {
        DataSource dataSource = DataSourceBuilder.create().type(DriverManagerDataSource.class).build();
        return dataSource;
    }

    /**
     * The second configuration- a prod, for production.We use c3p0  for create pool connection
     */
    @Bean
    @Profile("prod")
    public DataSource productionDataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driverClass);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setInitialPoolSize(initialPoolSize);
            dataSource.setMaxPoolSize(maxPoolSize);
            return dataSource;
        } catch (PropertyVetoException e) {
            throw new CannotGetJdbcConnectionException("Error while get connection to database");
        }
    }

    @Profile(value = {"dev", "prod"})
    @Bean
    public LocalContainerEntityManagerFactoryBean managerFactory(DataSource dataSource,
                                                                 EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return entityManagerFactoryBuilder.dataSource(dataSource).packages(PACKAGE_TO_SCAN).build();
    }
}
