package geostat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeostatPPPIDataService {

    public static void main(String[] args) {
        SpringApplication.run(GeostatPPPIDataService.class, args);
    }
}
