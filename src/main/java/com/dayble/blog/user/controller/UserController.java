package com.dayble.blog.user.controller;

import com.dayble.blog.global.ResponseTemplate;
import com.dayble.blog.global.interceptor.CheckAuth;
import com.dayble.blog.global.interceptor.LoginMember;
import com.dayble.blog.user.application.UserService;
import com.dayble.blog.user.controller.dto.request.ProfileCreateRequest;
import com.dayble.blog.user.controller.dto.request.ProfileUpdateRequest;
import com.dayble.blog.user.controller.dto.response.ProfileCreateResponse;
import com.dayble.blog.user.controller.dto.response.ProfileUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @CheckAuth
    @PostMapping()
    public ResponseTemplate<ProfileCreateResponse> createProfile(
            @LoginMember Long id,
            @RequestBody @Valid ProfileCreateRequest request
    ) {
        ProfileCreateResponse response = userService.createProfile(request, id);
        return new ResponseTemplate<>(HttpStatus.OK, "회원 정보 작성이 완료되었습니다.", response);
    }

    @CheckAuth
    @PatchMapping()
    public ResponseTemplate<ProfileUpdateResponse> updateProfile(
            @LoginMember Long id,
            @RequestBody @Valid ProfileUpdateRequest request
    ) {
        ProfileUpdateResponse response = userService.updateProfile(request, id);
        return new ResponseTemplate<>(HttpStatus.OK, "회원 정보 수정이 완료되었습니다.", response);
    }

    @CheckAuth
    @DeleteMapping()
    public ResponseTemplate<Void> deleteUser(@LoginMember Long id) {
        userService.deleteUser(id);
        return new ResponseTemplate<>(HttpStatus.OK, "회원탈퇴가 완료되었습니다.", null);
    }
}
