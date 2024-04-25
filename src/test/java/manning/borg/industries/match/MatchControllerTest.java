package manning.borg.industries.match;

import manning.borg.industries.config.TestWebConfig;
import manning.borg.industries.web.MatchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.model.IStandaloneElementTag;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static manning.borg.industries.config.TestWebConfig.SEASON_STR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestWebConfig.class})
public class MatchControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private MatchController controller;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void find_shouldAddSeasonToModelAndRenderSeasonReportView() throws Exception{

        //SEASON_STR = "1910-1911"
        this.mockMvc.perform(get("/reports/season-report?seasonSelected=" + SEASON_STR))
                .andExpect(status().isOk())
                .andExpect(view().name("reports/SeasonReport"))
                .andExpect(forwardedUrl("reports/SeasonReport"))
                .andExpect(model().attribute("season", hasProperty("season", is(SEASON_STR))));
    }

}
