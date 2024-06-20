package org.solo.toynoticeservice.kafka;

import org.solo.toynoticeservice.controller.NoticeController;
import org.solo.toynoticeservice.entity.NoticeEntity;
import org.solo.toynoticeservice.repository.NoticeRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;

@Service
public class KafkaConsumerService {

    private final Sinks.Many<NoticeEntity> sink;
    private final NoticeRepository noticeRepository;

    public KafkaConsumerService(NoticeController noticeController, NoticeRepository noticeRepository) {
        this.sink = noticeController.getSink();
        this.noticeRepository = noticeRepository;
    }

    @KafkaListener(topics = {"comment-add-events", "report-my-board-events"}, groupId = "notice")
    public void listenNotice(String message){
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setUsername(message.split("::")[0]);
        noticeEntity.setMessage(message.split("::")[1]);
        noticeEntity.setEventsDate(LocalDateTime.now());
        noticeRepository.save(noticeEntity).doOnNext(sink::tryEmitNext).subscribe();
    }
}
