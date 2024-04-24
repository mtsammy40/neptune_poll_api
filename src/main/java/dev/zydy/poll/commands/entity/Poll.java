package dev.zydy.poll.commands.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zydy.poll.commands.commons.entity.PersistenceEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table("polls")
@Slf4j
public final class Poll extends PersistenceEntity {
    private String title;
    private String description;
    @Builder.Default
    private Json configuration = Json.of("{}");

    public Configuration getConfiguration() {
        try {
            return new ObjectMapper().readValue(configuration.asString(), Configuration.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse configuration", e);
            return new Configuration();
        }
    }

    public void setConfiguration(Configuration configuration) {
        try {
            var configurationJson = new ObjectMapper()
                    .writeValueAsString(configuration != null ? configuration : new Configuration());
            this.configuration = Json.of(configurationJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Configuration {
        @JsonProperty("max_votes")
        private LocalDateTime maxVotes;
    }
}
