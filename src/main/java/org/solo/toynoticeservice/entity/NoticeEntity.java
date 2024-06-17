package org.solo.toynoticeservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;


@Data
@Table(name = "Notice")
public class NoticeEntity {
    @Id
    private Long id;
    private String username;
    private String message;
    private LocalDateTime eventsDate;
    private boolean isRead;
}
