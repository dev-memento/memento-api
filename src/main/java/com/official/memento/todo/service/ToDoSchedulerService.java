package com.official.memento.todo.service;

import com.official.memento.member.domain.MemberPersonalInfo;
import com.official.memento.member.domain.port.MemberPersonalInfoRepository;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoSchedulerService {
    private final ToDoRepository toDoRepository;
    private final MemberPersonalInfoRepository memberPersonalInfoRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void updateIncompleteTodos() {
        List<MemberPersonalInfo> members = memberPersonalInfoRepository.findAll();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        for (MemberPersonalInfo memberPersonalInfo : members){
            LocalDateTime memberLocalTime = now.plusHours(memberPersonalInfo.getTimeZoneOffset());
            if (memberLocalTime.getHour() == 0 && memberLocalTime.getMinute() == 0) {
                LocalDate today = memberLocalTime.toLocalDate();
                List<ToDo> toDos = toDoRepository.findByMemberIdAndEndDateAndIsCompleted(memberPersonalInfo.getId(), today, false);

                for (ToDo toDo : toDos) {
                    toDo.incrementEndDate();
                    toDoRepository.update(toDo);
                }
            }
        }

    }
}
