package challenge.meli.coupon;

import challenge.meli.coupon.commons.properties.GlobalProperties;
import challenge.meli.coupon.commons.properties.ProviderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableConfigurationProperties({
		GlobalProperties.class,
		ProviderProperties.class,
})
@EnableRetry
@EnableAspectJAutoProxy
@EnableMongoRepositories(basePackages = "challenge.meli.coupon.infrastructure.output.adapter.db.repository")
public class CouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
	}

}
