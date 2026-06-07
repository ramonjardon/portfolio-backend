package dev.ramonjardon.portfolio.backend.infrastructure.config;

import java.util.List;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonInclude;

import tools.jackson.core.StreamWriteFeature;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.JsonRecyclerPools;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration(proxyBeanMethods=false)
public class JacksonConfig {

@Bean
@Primary
public JsonMapper jsonMapper(List<JsonMapperBuilderCustomizer> customizers) {

    JsonFactory factory = JsonFactory.builder()
        .recyclerPool(JsonRecyclerPools.nonRecyclingPool())
        .build();

    JsonMapper.Builder builder = JsonMapper.builder(factory);

    builder.enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

    // serializationInclusion() eliminado en Jackson 3
    builder.changeDefaultPropertyInclusion(incl ->
        incl.withValueInclusion(JsonInclude.Include.NON_NULL)
            .withContentInclusion(JsonInclude.Include.NON_NULL)
    );
        // Evita notación científica en BigDecimal: 1234.50 en vez de 1.23450E+3
        // Coste: cero en serialización, solo afecta al formato de salida
        builder.enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN);


    for (JsonMapperBuilderCustomizer customizer : customizers) {
        customizer.customize(builder);
    }

    return builder.build();
}
}
