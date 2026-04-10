package ca.bc.gov.nrs.fsp.api.config;

// ... same imports ...

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class FspSecurityConfig {
  // ... same content, no other changes ...
}