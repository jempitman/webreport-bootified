package manning.borg.industries.web;

import manning.borg.industries.domain.MatchDTO;
import manning.borg.industries.service.MatchService;
import manning.borg.industries.domain.Season;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reports")
public class MatchController {

    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {this.matchService = matchService;}

    @GetMapping("season-report")
    @ResponseStatus(value = HttpStatus.OK)
    public String seasonStatistics(@ModelAttribute MatchDTO matchDTO, Model model) {
        model.addAttribute("seasons", matchService.getSeason());
        if(matchDTO.getSeasonSelected() != null){
            model.addAttribute("season", matchService.aggregateSeason(matchDTO.getSeasonSelected()));
        }
        return "reports/SeasonReport";
    }
}
