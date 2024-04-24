package dev.zydy.poll.commands.web.dto;

import java.util.List;

public record CreatePollDto(
    String title,
    String description,
    List<String> options
) {
}
