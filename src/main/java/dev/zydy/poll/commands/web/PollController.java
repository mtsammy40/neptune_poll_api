package dev.zydy.poll.commands.web;

import dev.zydy.poll.commands.command.CreatePollCommand;
import dev.zydy.poll.commands.model.Option;
import dev.zydy.poll.commands.web.dto.CreatePollDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/polls")
@RequiredArgsConstructor
public class PollController {
    private final ReactorCommandGateway commandGateway;

    @PostMapping("/")
    Mono<String> createPoll(@RequestBody() CreatePollDto createPollRequest) {
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
        );
    }
}
