package org.solo.toynoticeservice.controller;

import org.solo.toynoticeservice.entity.NoticeEntity;
import org.solo.toynoticeservice.repository.NoticeRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class NoticeController {

    private final Sinks.Many<NoticeEntity> sink;
    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
        this.noticeRepository = noticeRepository;
    }

    public Sinks.Many<NoticeEntity> getSink() {
        return sink;
    }

    @GetMapping("/{username}")
    public Flux<ServerSentEvent<NoticeEntity>> getData(@PathVariable String username) {

        noticeRepository.findByUsername(username).subscribe(sink::tryEmitNext);

        return sink
                .asFlux()
                .filter(entity -> entity.getUsername().equals(username))
                .map(entity -> ServerSentEvent.builder(entity).build())
                .doOnCancel(() -> sink.asFlux().blockLast());
    }

    @PatchMapping("/{id}")
    public void readNotice(@PathVariable Long id) {

        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setId(id);
        noticeEntity.setRead(true);

        noticeRepository.updateIsRead(id).subscribe();
    }
}
