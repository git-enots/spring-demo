package com.handler;

import com.config.JWTUtil;
import com.entity.User;
import com.repository.UserRepository;
import com.validator.UserValidator;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class UserHander {
    private final Validator validator = new UserValidator();
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    public UserHander(JWTUtil jwtUtil,UserRepository userRepository){
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;

    }
    public Mono<ServerResponse> login(ServerRequest request) {
          Mono<User> paramUser=request.bodyToMono(User.class).doOnNext(this::validate);
          Mono<User> userMono= paramUser.flatMap(t->{
              User user=new User();
              user.setUsername(t.getUsername());
              user.setPassword(DigestUtils.md5DigestAsHex(t.getPassword().getBytes()));
              return userRepository.findOne(Example.of(user));
          });
          return userMono.flatMap(t-> ok().header(HttpHeaders.AUTHORIZATION,jwtUtil.generateToken(t)).build())
                  .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build());
    }
    public Mono<ServerResponse> saveUser(ServerRequest request){
        return  ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(request.bodyToMono(User.class).flatMap(user -> {
                    user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                    return userRepository.save(user);}),
                        User.class);
    }

    private void validate(User user) {
        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}
