package com.jd.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class HazelcastConfig {

    @Value("${hazelcast.port:5701}")
    private int hazelcastPort;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jd.hazelcast"))
                .build();
    }

    @Bean
    public Config hzConfig(EurekaClient eurekaClient) {

        Config config = new Config();

        config.getNetworkConfig().setPort(hazelcastPort);
        config.getProperties().setProperty("hazelcast.discovery.enabled", "true");

        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);

        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
        EurekaOneDiscoveryStrategyFactory discoveryStrategyFactory = new EurekaOneDiscoveryStrategyFactory();

        Map<String, Comparable> properties = new HashMap<String, Comparable>();
        properties.put("self-registration", "true");
        properties.put("namespace", "hazelcast");
        properties.put("use-metadata-for-host-and-port", "true");

        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(discoveryStrategyFactory, properties);
        joinConfig.getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);

        return config;
    }

    @Bean
    public HazelcastInstance hzInstance(Config hzConfig) {
        return Hazelcast.newHazelcastInstance(hzConfig);
    }
}
