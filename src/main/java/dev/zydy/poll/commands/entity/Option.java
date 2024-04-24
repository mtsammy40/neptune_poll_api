package dev.zydy.poll.commands.entity;

import dev.zydy.poll.commands.commons.entity.PersistenceEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Table(name = "options")
public final class Option extends PersistenceEntity {
    @Column("poll_id")
    private UUID pollId;
    @Column("display_name")
    private String displayName;
}
