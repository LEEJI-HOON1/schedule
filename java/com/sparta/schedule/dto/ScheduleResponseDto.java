package com.sparta.schedule.dto;


import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private String username;
    private String contents;
    private String passward;
    private String title;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUsername();
        this.contents = schedule.getContents();
        this.passward = schedule.getPassward();
        this.title = schedule.getTitle();
    }

    public ScheduleResponseDto(Long id, String username, String contents, String passward, String title) {
        this.id =id;
        this.username = username;
        this.contents = contents;
        this.passward = passward;
        this.title = title;
    }
}