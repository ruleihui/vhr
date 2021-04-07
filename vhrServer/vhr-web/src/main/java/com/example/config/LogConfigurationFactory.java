package com.example.config;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Plugin(name = "LogConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Component
@Order(50)
public class LogConfigurationFactory extends ConfigurationFactory {

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        InputStream url = this.getClass().getClassLoader().getResourceAsStream("logger/log4j2.xml");
        ConfigurationSource source = null;
        try {
            source = new ConfigurationSource(new BufferedInputStream(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new XmlConfiguration(loggerContext, source);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }
}
