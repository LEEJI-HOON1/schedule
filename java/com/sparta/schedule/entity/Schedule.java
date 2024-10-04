package com.sparta.schedule.entity;

import com.sparta.schedule.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    private Long id;
    private String username;
    private String contents;
    private String passward;
    private String title;

    public Schedule(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.passward = requestDto.getPassward();
        this.title = requestDto.getTitle();
    }

    public void update(ScheduleRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.passward = requestDto.getPassward();
        this.title = requestDto.getTitle();
    }
}