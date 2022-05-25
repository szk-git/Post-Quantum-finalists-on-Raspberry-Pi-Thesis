package com.elte.jfirbj.backend;

import com.elte.jfirbj.backend.payload.request.SignupRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterAuthTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideDatasForTestRegisterWithExpectedStatusMessage")
    void testRegisterWithExpectedStatusMessage(String username, String password, String email,
                                            String firstName, String lastName, ResultMatcher resultMatcher) throws Exception {
        var body = createRegisterObjectMapper(username, password, email, firstName, lastName);

        mvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(resultMatcher);
    }

    private String createRegisterObjectMapper(String username, String password, String email,
                                           String firstName, String lastName) throws JsonProcessingException {
        SignupRequest registerRequest = new SignupRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setEmail(email);
        registerRequest.setFirstName(firstName);
        registerRequest.setLastName(lastName);

        return objectMapper.writeValueAsString(registerRequest);
    }

    private static Stream<Arguments> provideDatasForTestRegisterWithExpectedStatusMessage() {
        return Stream.of(
                Arguments.of("admin", "admin321", "aisdos007@gmail.com", "AdminF", "AdminS", status().is4xxClientError()),
                Arguments.of("newAdmin", "admin321", "aisdos007@gmail.com", "AdminF", "AdminS", status().is4xxClientError()),
                Arguments.of("aisdos", "admin123", "admin@gmail.com", "Kristóf", "Szabó", status().is2xxSuccessful())
        );
    }

}
