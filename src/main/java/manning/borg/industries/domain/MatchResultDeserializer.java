package manning.borg.industries.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class MatchResultDeserializer extends StdDeserializer<Match.Result> {

    public MatchResultDeserializer() {
        this(null);
    }

    public MatchResultDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Match.Result deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String resultStr = node.asText();
        if (resultStr.length() != 1) return Match.Result.UNK;
        return Match.Result.parse(resultStr.charAt(0));
    }

}
