package manning.borg.industries.match;

import manning.borg.industries.domain.Match;
import manning.borg.industries.domain.Season;
import manning.borg.industries.repository.MatchRepository;
import manning.borg.industries.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    public static final String SEASON_STR = "1910-1911";
    public static final String MICHEL_PLATINI = "Michel Platini";
    private final MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
    private MatchService matchService;

    @BeforeEach
    public void setup(){
        matchService = new MatchService(matchRepository);
        Match theGame = Match.builder()
                .id(1L)
                .awayTeam("Watford")
                .homeTeam("Liverpool")
                .fullTimeAwayGoals(0)
                .fullTimeHomeGoals(7)
                .halfTimeAwayGoals(0)
                .halfTimeHomeGoals(4)
                .referee(MICHEL_PLATINI)
                .fullTimeResult(Match.Result.HOME_WIN)
                .season(SEASON_STR)
                .build();
        when(matchRepository.findBySeason(SEASON_STR)).thenReturn(Collections.singletonList(theGame));
    }

    @Test
    public void test_aggregateSeason(){
        Season season = matchService.aggregateSeason(SEASON_STR);
        assertAll(
                () -> assertEquals(0,season.getAwayWins()));
        assertThat(season.getHomeWins()).isEqualTo(1);
        assertThat(season.getRefereeResults().size()).isEqualTo(1);
        Season.RefereeResults refereeResults = (Season.RefereeResults) season.getRefereeResults().toArray()[0];
        assertThat(refereeResults.getName()).isEqualTo(MICHEL_PLATINI);
        assertThat(refereeResults.getHomeWins()).isEqualTo(1);
        assertThat(refereeResults.getAwayWins()).isEqualTo(0);
    }
}
