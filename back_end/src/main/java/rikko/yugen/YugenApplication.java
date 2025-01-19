package rikko.yugen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("rikko.yugen.repository")
@SpringBootApplication
(exclude = {SecurityAutoConfiguration.class})
public class YugenApplication {

	public static void main(String[] args) {
		SpringApplication.run(YugenApplication.class, args);
	}

}
