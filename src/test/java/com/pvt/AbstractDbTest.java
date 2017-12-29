package com.pvt;

import com.pvt.config.AppConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ContextConfiguration(classes = {AbstractDbTest.TestConfig.class})
@ActiveProfiles("test")
public abstract class AbstractDbTest extends AbstractJUnit4SpringContextTests {

    @Configuration
    @EnableTransactionManagement
    @PropertySource("classpath:jdbc_test.properties")
    @EnableJpaRepositories(basePackages = "com.pvt.dao.repository")
    @ComponentScan("com.pvt")
    public static class TestConfig {

    }
}
