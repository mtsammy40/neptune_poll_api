package dev.zydy.poll.commands.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
public class PersistenceEntity implements Persistable<UUID> {
    @Transient
    @Builder.Default
    private boolean isPersisted = true;

    @Id
    private UUID id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Override
    public boolean isNew() {
        log.debug("Checking if entity is new: {} - ID : {} - Class : {}", !isPersisted, id, getClass().getSimpleName());
        return !isPersisted;
    }
}




