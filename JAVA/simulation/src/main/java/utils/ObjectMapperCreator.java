package utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperCreator {

    @Getter
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}
