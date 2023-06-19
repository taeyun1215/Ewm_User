package user.application.port.in.usecase;

import user.adapter.in.request.RegisterUserRequest;
import user.domain.User;

public interface RegisterUserUseCase {

    User registerUser(RegisterUserRequest registerUserRequest);

}
