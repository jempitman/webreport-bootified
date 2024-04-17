package manning.borg.industries.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "EPL")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String division;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="dd/MM/yy")
    private Date gameDate;
    private String gameTime;

    private String homeTeam;
    private String awayTeam;
    private Integer fullTimeHomeGoals;
    private Integer fullTimeAwayGoals;
    private Integer fullTimeResult;
    private Integer halfTimeHomeGoals;
    private Integer halfTimeAwayGoals;
    private Integer halfTimeResult;
    private String referee;
    private Integer homeTeamShots;
    private Integer awayTeamShots;
    private Integer homeTeamShotsOnTarget;
    private Integer awayTeamShotsOnTarget;
    private Integer homeTeamFouls;
    private Integer awayTeamFouls;
    private Integer homeTeamCorners;
    private Integer awayTeamCorners;
    private Integer homeTeamYellowCards;
    private Integer awayTeamYellowCards;
    private Integer homeTeamRedCards;
    private Integer awayTeamRedCards;
    private String season;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        if (gameDate == null || homeTeam == null || awayTeam == null) return false;
        return gameDate.equals(match.gameDate) &&
                homeTeam.equals(match.homeTeam) &&
                awayTeam.equals(match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDate, homeTeam, awayTeam);
    }

    public enum Result {
        HOME_WIN('H'), AWAY_WIN('A'), DRAW ('D'), UNK('X');

        //        @JsonValue
        private char value;

        Result(char value) { this.value = value; }

        public char getValue() { return value; }

        public static Result parse(char id) {
            Result result = null; // Default
            for (Result item : Result.values()) {
                if (item.getValue()==id) {
                    result = item;
                    break;
                }
            }
            return result;
        }

    };

}
