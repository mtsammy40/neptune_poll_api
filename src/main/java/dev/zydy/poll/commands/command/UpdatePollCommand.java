package dev.zydy.poll.commands.command;

import dev.zydy.poll.commands.model.Option;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;

public record UpdatePollCommand(@TargetAggregateIdentifier String id, String title, String description, List<Option> options) {
}
