package org.diorite.web.cms.core;

import static org.diorite.web.cms.core.DioriteCms.BASE_PACKAGE;


import java.util.logging.Logger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.diorite.web.cms.services.PermissionsService;

@SpringBootApplication(scanBasePackages = BASE_PACKAGE)
@EntityScan(BASE_PACKAGE)
@EnableJpaRepositories(BASE_PACKAGE)
@EnableCaching
public class DioriteCms extends SpringBootServletInitializer
{
    public final static String BASE_PACKAGE = "org.diorite.web.cms.*";
    private static DioriteCms INSTANCE;
    private final Logger logger = Logger.getLogger("DioriteCMS");
    @Autowired
    private PermissionsService permissionsService;

    public DioriteCms()
    {
        DioriteCms.INSTANCE = this;
    }

    private void run()
    {
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public PermissionsService getPermissionsService()
    {
        return this.permissionsService;
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder builder)
    {
        return builder.sources(DioriteCms.class);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }

    public static void main(final String[] args)
    {
        SpringApplication.run(DioriteCms.class, args);
        DioriteCms.INSTANCE.run();
    }

    public static DioriteCms getInstance()
    {
        return INSTANCE;
    }
}
