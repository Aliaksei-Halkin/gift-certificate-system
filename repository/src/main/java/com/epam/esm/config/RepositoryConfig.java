package com.epam.esm.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * The configuration for repository layer
 *
 * @author Aliaksei Halkin
 */
@Configuration
@ComponentScan("com.epam.esm")
public class RepositoryConfig {
    private static final String PACKAGE_TO_SCAN = "com.epam.esm.entity";

    /**
     * The first configuration- a dev, for development
     */
    @Bean
    @Profile("dev")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource developmentDataSource() {
      //  return DataSourceBuilder.create().type(DriverManagerDataSource.class).build();
        //  todo: don't connect to  @ConfigurationProperties(prefix = "spring.datasource")

        DataSource dataSource = DataSourceBuilder
                .create()
                .username("root")
                .password("root")
                .url("jdbc:mysql://localhost:3306/certificatesdb?useUnicode=true&serverTimezone=UTC")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
        return dataSource;
    }

    /**
     * The second configuration- a prod, for production.We use c3p0  for create pool connection
     *
     * @param driverName driver for connection
     * @param url        url for connection
     * @param username   username for connection
     * @param password   password for connection
     * @return {@link DataSource} A factory for connections to the physical data source that this DataSource object represents.
     */
    @Bean
    @Profile("prod")
    @ConfigurationProperties("spring.c3p0")
    public DataSource productionDataSource(@Value("${spring.c3p0.driverClass}") String driverName,
                                           @Value("${spring.c3p0.jdbcUrl}") String url,
                                           @Value("${spring.c3p0.username}") String username,
                                           @Value("${spring.c3p0.password}") String password,
                                           @Value("${spring.c3p0.initial_pool_size}") int initialPoolSize,
                                           @Value("${spring.c3p0.max_pool_size}") int maxPoolSize) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driverName);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setInitialPoolSize(initialPoolSize);
            dataSource.setMaxPoolSize(maxPoolSize);
            return dataSource;
        } catch (PropertyVetoException e) {
            throw new CannotGetJdbcConnectionException("Error while get connection to database");
        }
    }

//    @Profile("test")
//    @Bean
//    public DataSource testDataSource() {
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:schema.sql").addScript("classpath:test-data.sql").setScriptEncoding("UTF-8").build();
//    }

    @Profile(value = {"dev", "prod"})
    @Bean
    public LocalContainerEntityManagerFactoryBean managerFactory(DataSource dataSource,
                                                                 EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        return entityManagerFactoryBuilder.dataSource(dataSource).packages(PACKAGE_TO_SCAN).build();
    }

//    @Profile("test")
//    @Bean
//    public LocalContainerEntityManagerFactoryBean testManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
//        entityManager.setDataSource(dataSource);
//        entityManager.setPackagesToScan(PACKAGE_TO_SCAN);
//        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return entityManager;
//    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
