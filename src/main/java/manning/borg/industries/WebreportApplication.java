package manning.borg.industries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import manning.borg.industries.domain.Match;
import manning.borg.industries.service.MatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class WebreportApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebreportApplication.class, args);
	}

	/*@Bean
	CommandLineRunner runner(MatchService matchService){
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Match>> typeReference = new TypeReference<List<Match>>() {};
			InputStream inputStream = TypeReference.class.getResourceAsStream("epl2019-2020.json");
			try{
				List<Match> matches = mapper.readValue(inputStream,typeReference);
				matchService.save((Match) matches);
				System.out.println("Matches saved!");
			} catch (IOException e){
				System.out.println("Unable to save matches: " + e.getMessage());
			}
		};
	}*/



}
