package dev.zydy.poll.commands.command;

import java.util.List;

public record UpdatePollCommand(String title, String description, List<String> options) {
}
