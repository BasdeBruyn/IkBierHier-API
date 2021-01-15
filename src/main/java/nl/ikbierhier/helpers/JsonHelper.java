package nl.ikbierhier.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JsonHelper {
    private static final ObjectMapper mapper;

    static {
        mapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
    public static <T> T toObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(jsonString, clazz);
    }
}
