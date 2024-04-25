package dev.zydy.poll.commands.web.dto;

import dev.zydy.poll.commands.entity.Option;

import java.util.List;

public record CreateUpdatePollDto(
    String title,
    String description,
    List<String> options
) {
}
