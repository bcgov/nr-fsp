package ca.bc.gov.nrs.fsp.api.support;


import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class MockConfiguration {

  @Bean
  @Primary
  public Connection connection() {
    return Mockito.mock(Connection.class);
  }
  
}
