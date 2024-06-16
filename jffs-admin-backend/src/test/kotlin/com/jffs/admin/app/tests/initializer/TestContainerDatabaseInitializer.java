package com.jffs.admin.app.tests.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static GenericContainer<?> mongoDBContainer;
    private static final String MONGO_USER = "root";
    private static final String MONGO_DB_PASSWORD = "example";
    private static final String MONGO_DB_NAME = "testDB";

    static {
        mongoDBContainer = new GenericContainer(DockerImageName.parse("mongo:7.0"))
                .withEnv("MONGO_INITDB_ROOT_USERNAME", MONGO_USER)
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", MONGO_DB_PASSWORD)
                .withEnv("MONGO_INITDB_DATABASE", MONGO_DB_NAME)
                .withExposedPorts(27017);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        mongoDBContainer.start();

        // Override MySql configuration
        String newDatabaseUserName = "database.username=" + MONGO_USER;
        String newDatabasePassword = "database.password=" + MONGO_DB_PASSWORD;
        String newDatabaseAppName = "database.appName=" + MONGO_DB_NAME;
        String newDatabaseDBName = "database.dbName=" + MONGO_DB_NAME;
        String newDatabaseHost = "database.host=" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getMappedPort(27017);
        String newDatabaseScheme = "database.scheme=mongodb";

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                newDatabaseUserName,
                newDatabasePassword,
                newDatabaseAppName,
                newDatabaseDBName,
                newDatabaseHost,
                newDatabaseScheme);
    }
}