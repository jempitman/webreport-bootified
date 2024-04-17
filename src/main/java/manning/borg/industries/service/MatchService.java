package manning.borg.industries.service;

import manning.borg.industries.domain.Match;
import manning.borg.industries.domain.Season;
import manning.borg.industries.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MatchService {

    private MatchRepository matchRepository;

    @Autowired
    public MatchService (MatchRepository matchRepository){this.matchRepository = matchRepository;}

    @Transactional
    public Match save(Match match){
        matchRepository.save(match);
        return match;
    }

    public Season aggregateSeason(String seasonStr){
        int homeWins = 0;
        int awayWins = 0;
        int draws = 0;
        int goallessDraws = 0;
        Map<String, Season.RefereeResults> refereeMap = new HashMap<>();
        Map<String, Integer> teamPlayed = new HashMap<>();
        Map<String, Integer> teamPoints = new HashMap<>();
        Map<String, Integer> teamGoalDiff = new HashMap<>();

        for (Match match : matchRepository.findBySeason(seasonStr)){
            addResults(teamPlayed, teamPoints, teamGoalDiff, match.getAwayTeam(),
                    match.getFullTimeAwayGoals() - match.getFullTimeHomeGoals());

            addResults(teamPlayed, teamPoints, teamGoalDiff, match.getHomeTeam(),
                    match.getFullTimeHomeGoals() - match.getFullTimeAwayGoals());

            Integer[] addToReferee = new Integer[] {0,0,0,0};
            if (match.getFullTimeHomeGoals() > match.getFullTimeAwayGoals()){
                homeWins++;
                addToReferee[0]++;
            }
            if (match.getFullTimeHomeGoals() < match.getFullTimeAwayGoals()){
                awayWins++;
                addToReferee[1]++;
            }
            if (Objects.equals(match.getFullTimeHomeGoals(), match.getFullTimeAwayGoals())){
                draws++;
                addToReferee[2]++;
                if(match.getFullTimeHomeGoals() == 0){
                    goallessDraws++;
                    addToReferee[3]++;
                }
            }
            adjustRefereeStatistics(refereeMap, match.getReferee(), addToReferee);
        }

        // figure out results. this is a pain because ties on point go to goal difference
        List<Season.TeamResult> teamResults = new ArrayList<>();
        for (Map.Entry<String, Integer> team : teamPlayed.entrySet()){
            String teamName = team.getKey();
            Integer played = team.getValue();
            Integer points = teamPoints.getOrDefault(teamName, -1);
            Integer goalDiff = teamPoints.getOrDefault(teamName, -1);
            teamResults.add(new Season.TeamResult(teamName,played,points,goalDiff));
        }
        teamResults.sort(new Season.SortByPointsAndGoalDiff());

        // stick stuff in map
        return Season.builder()
                .season(seasonStr)
                .teamResults(teamResults)
                .homeWins(homeWins)
                .awayWins(awayWins)
                .draws(draws)
                .goallessDraws(goallessDraws)
                .refereeResults(refereeMap.values())
                .build();


    }

    private void addResults (Map<String, Integer> played, Map<String, Integer> points,
                             Map<String, Integer> goalDiff, String team, int diff){

    }

    private void adjustRefereeStatistics(Map<String, Season.RefereeResults> refereeMap, String referee, Integer[] newStats) {
        // verify entry for referee exists
        if (!refereeMap.containsKey(referee)) {
            refereeMap.put(referee, new Season.RefereeResults(referee));
        }
        Season.RefereeResults stats = refereeMap.get(referee);
        stats.setHomeWins(stats.getHomeWins()+ newStats[0]);
        stats.setAwayWins(stats.getAwayWins()+ newStats[1]);
        stats.setDraws(stats.getDraws()+ newStats[2]);
        stats.setGoallessDraws(stats.getGoallessDraws()+ newStats[3]);
    }


}
