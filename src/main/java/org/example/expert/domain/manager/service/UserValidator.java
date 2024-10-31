package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.entity.User;
import org.springframework.util.ObjectUtils;

public class UserValidator {

    public static void validateUserExists(User user) {
        if (user == null) {
            throw new InvalidRequestException("User not found");
        }
    }

    public static void validateUserIsNotTheCreator(User user, User managerUser) {
        if (ObjectUtils.nullSafeEquals(user.getId(), managerUser.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }
    }
}
