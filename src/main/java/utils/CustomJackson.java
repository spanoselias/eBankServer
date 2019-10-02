package utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class CustomJackson {

    public static <T> T deserialize(String json, Class<T> target) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
        return mapper.readValue(json, target);
    }

    public static <T> String serialize(final T obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    public static ObjectMapper mapper =
            new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .enable(SerializationFeature.INDENT_OUTPUT)
                    .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

}
