package com.sparta.schedule.controller;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getSchedule() {
        // DB 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {

                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                String passward = rs.getString("passward");
                String title = rs.getString("title");
                return new ScheduleResponseDto (id, username, contents,passward,title);
            }
        });
    }
    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule= new Schedule(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (username, contents , passward , title ) VALUES (?, ? ,? ,?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getUsername());
                    preparedStatement.setString(2, schedule.getContents());
                    preparedStatement.setString(3, schedule.getPassward());
                    preparedStatement.setString(4, schedule.getTitle());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);

        return scheduleResponseDto;
    }

    @PutMapping("/schedule/{id}")
    public Long updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {

        Schedule schedule = findById(id);
        if(schedule != null) {

            String sql = "UPDATE schedule SET username = ?, contents = ? , passward = ? , title = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), requestDto.getPassward(),requestDto.getTitle(),id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/schedule/{id}")
    public Long deleteSchedule(@PathVariable Long id) {

        Schedule schedule = findById(id);
        if(schedule != null) {

            String sql = "DELETE FROM schedule WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 일정은 존재하지 않습니다.");
        }
    }

    private Schedule findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Schedule schedule = new Schedule();
                schedule.setUsername(resultSet.getString("username"));
                schedule.setContents(resultSet.getString("contents"));
                schedule.setContents(resultSet.getString("passward"));
                schedule.setContents(resultSet.getString("title"));
                return schedule;
            } else {
                return null;
            }
        }, id);
    }
}