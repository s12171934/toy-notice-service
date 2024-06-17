package org.solo.toynoticeservice.repository;

import org.solo.toynoticeservice.entity.NoticeEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoticeRepository extends ReactiveCrudRepository<NoticeEntity, Integer> {
    Flux<NoticeEntity> findByUsername(String username);
    @Modifying
    @Query("UPDATE Notice SET is_read = true WHERE id = :id")
    Mono<NoticeEntity> updateIsRead(@Param("id") Long id);
}
