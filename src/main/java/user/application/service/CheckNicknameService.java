package user.application.service;

import user.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import user.application.port.in.query.CheckNicknameQuery;
import user.application.port.out.LoadUserPort;
import user.domain.User;

import javax.transaction.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional
public class CheckNicknameService implements CheckNicknameQuery {

    private final LoadUserPort loadUserPort;

    @Override
    @Transactional
    public User checkNickname(String nickname) {
        return loadUserPort.findByNickname(nickname);
    }

}
