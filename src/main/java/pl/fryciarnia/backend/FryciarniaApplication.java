package pl.fryciarnia.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.*"})
public class FryciarniaApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(FryciarniaApplication.class, args);
	}

}
