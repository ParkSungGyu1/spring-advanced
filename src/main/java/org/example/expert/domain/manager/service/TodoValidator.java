package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.springframework.util.ObjectUtils;

public class TodoValidator {

    public static void validateOwnership(User user, Todo todo) {
        if (todo.getUser() == null || !ObjectUtils.nullSafeEquals(user.getId(), todo.getUser().getId())) {
            throw new InvalidRequestException("해당 일정을 만든 유저가 유효하지 않습니다.");
        }
    }
    
    public static void validateTodoExists(Todo todo) {
        if (todo == null) {
            throw new InvalidRequestException("Todo not found");
        }
    }
}
