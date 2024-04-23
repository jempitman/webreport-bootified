package manning.borg.industries.service;

import lombok.extern.java.Log;
import manning.borg.industries.domain.Match;
import manning.borg.industries.domain.Season;
import manning.borg.industries.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log
@Service
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService (MatchRepository matchRepository){this.matchRepository = matchRepository;}

    public Season aggregateSeason(String seasonStr){
        int homeWins = 0;
        int awayWins = 0;
        int draws = 0;
        int goallessDraws = 0;
        Map<String, Season.RefereeResults> refereeMap = new HashMap<>();
        Map<String, Integer> teamPlayed = new HashMap<>();
        Map<String, Integer> teamPoints = new HashMap<>();
        Map<String, Integer> teamGoalDiff = new HashMap<>();
        log.info("Match count = " + matchRepository.findBySeason(seasonStr).size());

        for (Match match : matchRepository.findBySeason(seasonStr)){

            //get Away results
            if("Liverpool".equals(match.getAwayTeam())){
                log.info(String.format("LIVERPOOL RESULT: %s / Away to %s / %s - %s", match.getGameDate(), match.getHomeTeam(),
                        match.getFullTimeHomeGoals(), match.getFullTimeAwayGoals()));
            }
            addResults(teamPlayed, teamPoints, teamGoalDiff, match.getAwayTeam(),
                    match.getFullTimeAwayGoals() - match.getFullTimeHomeGoals());

            //get Home results
            if("Liverpool".equals(match.getHomeTeam())){
                log.info(String.format("LIVERPOOL RESULT: %s / Home to %s / %s - %s", match.getGameDate(), match.getAwayTeam(),
                        match.getFullTimeHomeGoals(), match.getFullTimeAwayGoals()));
            }

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
            Integer goalDiff = teamGoalDiff.getOrDefault(teamName, -1);
            teamResults.add(new Season.TeamResult(teamName,played,points,goalDiff));
        }
        teamResults.sort(new Season.SortByPointsAndGoalDiff());

        log.info(String.format("EPL played : %s", matchRepository.findAll().size()));

        Season season = new Season();
        season.setSeason(seasonStr);
        season.setTeamResults(teamResults);
        season.setHomeWins(homeWins);
        season.setAwayWins(awayWins);
        season.setDraws(draws);
        season.setGoallessDraws(goallessDraws);
        season.setRefereeResults(refereeMap.values());
        return season;

    }

    private void addResults (Map<String, Integer> played, Map<String, Integer> points,
                             Map<String, Integer> goalDiff, String team, int diff){

        if ("Liverpool".equals(team)){
            log.info("LIVERPOOL update");
        }

        if(!played.containsKey(team)){
            played.put(team, 0);
        }

        if(!points.containsKey(team)){
            points.put(team, 0);
        }

        if(!goalDiff.containsKey(team)){
            goalDiff.put(team, 0);
        }

        played.put(team, 1 + played.get(team));

        if ("Liverpool".equals(team)){
            log.info(String.format("LIVERPOOL played now: %s", played.get(team)));
        }

        if (diff < 0){
            // a loss == no points
        }
        else if(diff > 0){
            // a win = 3 points
            points.put(team, 3 + points.get(team));
        } else {
            // a draw = 1 point
            points.put(team, 1 + points.get(team));
        }

        if ("Liverpool".equals(team)){
            log.info(String.format("LIVERPOOL points now: %s", points.get(team)));
        }
        goalDiff.put(team, diff+goalDiff.get(team));

        if ("Liverpool".equals(team)){
            log.info(String.format("LIVERPOOL goal-diff now: %s", goalDiff.get(team)));
        }


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

    public List<String> getSeason(){
        return matchRepository.findSeason();
    }

}
