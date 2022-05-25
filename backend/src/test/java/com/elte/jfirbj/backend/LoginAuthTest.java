package com.elte.jfirbj.backend;

import com.elte.jfirbj.backend.models.enums.RoleEnum;
import com.elte.jfirbj.backend.payload.request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LoginAuthTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideDatasForTestLoginWithExpectedStatusMessage")
    void testLoginWithExpectedStatusMessage(String userName, String password, ResultMatcher resultMatcher) throws Exception {
        var body = createLoginObjectMapper(userName, password);

        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(resultMatcher);
    }

    @ParameterizedTest
    @MethodSource("provideDatasForTestLoginWithExpectedRoles")
    void testLoginWithExpectedRoles(String userName, String password, String role) throws Exception {
        var body = createLoginObjectMapper(userName, password);

        mvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(content().string(containsString(role)));
    }

    private String createLoginObjectMapper(String userName, String password) throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(userName);
        loginRequest.setPassword(password);

        return objectMapper.writeValueAsString(loginRequest);
    }
    private static Stream<Arguments> provideDatasForTestLoginWithExpectedRoles() {
        return Stream.of(
                Arguments.of("user", "user", ""),
                Arguments.of("user", "user123", RoleEnum.ROLE_USER.label),
                Arguments.of("admin", "admin123", RoleEnum.ROLE_ADMIN.label)
        );
    }
    private static Stream<Arguments> provideDatasForTestLoginWithExpectedStatusMessage() {
        return Stream.of(
                Arguments.of("user", "user", status().is4xxClientError()),
                Arguments.of("user", "user123", status().is2xxSuccessful())
        );
    }

}
