package manning.borg.industries.config;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.java.Log;
import manning.borg.industries.domain.Match;
import manning.borg.industries.repository.MatchRepository;
import manning.borg.industries.domain.MatchResultDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Log
@Component
public class MatchLoader implements CommandLineRunner {

//    private static final Logger log = LoggerFactory.getLogger(MatchLoader.class);

    @Value("${testdata.loadfile}")
    private String loadfileResourceName;
    @Value("${testdata.season}")
    private String loadFileSeason;

    private final MatchRepository repository;
    private final ApplicationContext appContext;

    @Autowired
    public MatchLoader(MatchRepository repository, ApplicationContext ctx) {
        this.repository = repository;
        this.appContext = ctx;
    }

    @Override
    public void run(String... args) throws Exception{

//        System.out.println("MatchLoader class called");

        // load test data if specified
        if (StringUtils.isEmpty(loadFileSeason)) return;
        // if we get here, we load test data
        Resource resource =
                appContext.getResource(loadfileResourceName);

//        System.out.println("Loaded resource: " + resource);

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Match.Result.class, new MatchResultDeserializer());
        objectMapper.registerModule(module);
        int newCount = 0;
        int alreadyCount = 0;
        try {
            List<Match> list = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Match>>() {
            });
            //        list.stream().map(m -> repository.save(m));
            for (Match m : list) {
                if (repository.findByHomeTeamAndAwayTeamAndGameDate(m.getHomeTeam(), m.getAwayTeam(), m.getGameDate()).isEmpty()) {
                    m.setSeason(loadFileSeason);
                    repository.save(m);
                    newCount++;
                    log.info("Adding new record: " + m.getHomeTeam() + " v " + m.getAwayTeam());
                } else {
                    alreadyCount++;
                    log.info("Skipping previous record: " + m.getHomeTeam() + " v " + m.getAwayTeam());
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Error initializing database: ", ioe);
        }
        log.info("Repo size: "+repository.count());
        log.info("  new records: "+newCount);
        log.info("  old records: "+alreadyCount);

    }


}



