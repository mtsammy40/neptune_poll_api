package dev.zydy.poll.commands.web;

import dev.zydy.poll.commands.command.CreatePollCommand;
import dev.zydy.poll.commands.command.UpdatePollCommand;
import dev.zydy.poll.commands.model.Option;
import dev.zydy.poll.commands.web.dto.CreateUpdatePollDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/polls")
@RequiredArgsConstructor
public class PollController {
    private final ReactorCommandGateway commandGateway;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    Mono<Map<String, String>> createPoll(@RequestBody() CreateUpdatePollDto createPollRequest) {
        return commandGateway.send(
                new CreatePollCommand(
                        UUID.randomUUID().toString(),
                        createPollRequest.title(),
                        createPollRequest.description(),
                        createPollRequest.options()
                                .stream()
                                .map(option -> new Option(null, option))
                                .toList()
                )
        )
                .cast(UUID.class)
                .map(uuid -> Map.of("id", uuid.toString()));
    }

    @PutMapping("/{pollId}")
    Mono<Map<String, String>> updatePoll(@PathVariable String pollId, @RequestBody() CreateUpdatePollDto updatePollRequest) {
        return commandGateway.send(
                new UpdatePollCommand(
                        pollId,
                        updatePollRequest.title(),
                        updatePollRequest.description(),
                        updatePollRequest.options()
                                .stream()
                                .map(option -> new Option(null, option))
                                .toList()
                )
        )
                .cast(UUID.class)
                .map(uuid -> Map.of("id", uuid.toString()));
    }
}
