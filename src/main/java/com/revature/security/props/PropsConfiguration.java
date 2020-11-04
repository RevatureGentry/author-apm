package com.revature.security.props;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author William Gentry
 */
@Configuration
@EnableConfigurationProperties({ CorsConfigurationProps.class })
public class PropsConfiguration {
}
