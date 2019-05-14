package nefu.snow;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : pc
 * @date : 2019/05/06
 * @since : Java 8
 */

@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@RestController
public class SnowcleanAqpplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SnowcleanAqpplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SnowcleanAqpplication.class);
    }


}
