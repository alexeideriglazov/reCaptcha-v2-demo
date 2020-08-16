package com.recaptchademo;

import lombok.NonNull;
import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
import ro.isdc.wro.model.factory.WroModelFactory;
import ro.isdc.wro.model.factory.XmlModelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Wro4jCustomXmlModelManagerFactory extends ConfigurableWroManagerFactory {
    final private Properties props;

    public Wro4jCustomXmlModelManagerFactory(@NonNull Properties props) {
        this.props = props;
    }

    @Override
    protected Properties newConfigProperties() {
        return props;
    }

    @Override
    protected WroModelFactory newModelFactory() {
        return new XmlModelFactory() {
            @Override
            protected InputStream getModelResourceAsStream() throws IOException {
                String resourceLocation = "/wro.xml";
                InputStream stream = getClass().getResourceAsStream(resourceLocation);
                if (stream == null) {
                    throw new IOException("Invalid resource requested" + resourceLocation);
                }
                return stream;
            }
        };
    }
}


