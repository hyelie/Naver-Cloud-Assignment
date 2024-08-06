package assignment.simpleboard.schedule.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {
    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @PrePersist
    protected void onCreate() {
        if(id == null) id = UUID.randomUUID().toString();
    }

    @Column(length = 64, nullable = false)
    private String name;

    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime = LocalDateTime.now();
    private LocalDateTime lastUpdateTime = LocalDateTime.now();
    private Boolean finished = false;
    private String memo = "";
    private String location = "";

    @Column(length = 16)
    private String category = "";

    @Builder
    public Schedule(String id, String name, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime lastUpdateTime, Boolean finished, String memo, String location, String category) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastUpdateTime = lastUpdateTime;
        this.finished = finished;
        this.memo = memo;
        this.location = location;
        this.category = category;
    }

    public void updateField(String name, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime lastUpdateTime, Boolean finished, String memo, String location, String category) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lastUpdateTime = lastUpdateTime;
        this.finished = finished;
        this.memo = memo;
        this.location = location;
        this.category = category;
    }
}