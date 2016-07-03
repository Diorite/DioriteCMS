package org.diorite.web.cms.core;

import javax.persistence.Entity;

import java.io.File;
import java.util.Properties;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DioriteCms
{
    private static DioriteCms INSTANCE;
    private final Logger logger = Logger.getLogger("DioriteCMS");
    private SessionFactory hibernateSessionFactory;

    public DioriteCms()
    {
    }

    private void run()
    {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.hbm2ddl.import_files", "classpath:/default_data.sql");

        final Configuration configuration = new Configuration();
        final File hibernateConfig = new File("hibernate.cfg.xml");
        if (hibernateConfig.exists())
        {
            this.logger.info("Importing Hibernate configuration from: " + hibernateConfig);
            configuration.configure(hibernateConfig);
        }
        else if (Thread.currentThread().getContextClassLoader().getResource("hibernate.cfg.xml") != null)
        {
            this.logger.info("Importing Hibernate configuration from classpath: hibernate.cfg.xml");
            configuration.configure("hibernate.cfg.xml");
        }
        else
        {
            throw new IllegalStateException("Not found hibernate.cfg.xml! Create a new one in your application directory.");
        }

        configuration.addProperties(properties);
        configuration.setPhysicalNamingStrategy(new DioriteNamingStrategy());

        new Reflections("org.diorite.web.cms").getTypesAnnotatedWith(Entity.class).forEach(configuration::addAnnotatedClass);

        this.hibernateSessionFactory = configuration.buildSessionFactory();
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public SessionFactory getHibernate()
    {
        return this.hibernateSessionFactory;
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(DioriteCms.class, args);
        DioriteCms.INSTANCE = new DioriteCms();
        DioriteCms.INSTANCE.run();
    }

    public static DioriteCms getInstance()
    {
        return INSTANCE;
    }
}