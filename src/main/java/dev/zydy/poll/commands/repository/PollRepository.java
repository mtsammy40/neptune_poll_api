package dev.zydy.poll.commands.repository;

import dev.zydy.poll.commands.entity.Poll;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends ReactiveCrudRepository<Poll, String> {
}
