package com.javarush.nikolenko;

import com.javarush.nikolenko.config.Configurator;
import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.dto.Role;
import com.javarush.nikolenko.dto.UserTo;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseControllerIT extends ContainerIT {
    protected Configurator configurator;
    protected HttpServletRequest requestMock;
    protected HttpServletResponse responseMock;
    protected HttpSession sessionMock;
    protected RequestDispatcher requestDispatcherMock;
    protected ServletConfig servletConfigMock;
    protected ServletContext servletContextMock;
    protected UserTo testUserTo;
    protected UserTo testGuestTo;
    protected UserTo testNotSavedAdminTo;

    protected BaseControllerIT() {
        configurator = NanoSpring.find(Configurator.class);

        servletConfigMock = mock(ServletConfig.class);
        servletContextMock = mock(ServletContext.class);
        when(servletConfigMock.getServletContext()).thenReturn(servletContextMock);

        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        sessionMock = mock(HttpSession.class);
        requestDispatcherMock = mock(RequestDispatcher.class);
        when(requestMock.getSession()).thenReturn(sessionMock);
        when(requestMock.getSession(false)).thenReturn(sessionMock);

        testUserTo = UserTo.builder()
                .id(10L)
                .login("testUser")
                .password("testUserPassword")
                .name("TestUserName")
                .role(Role.THE_USER)
                .build();

        testGuestTo = UserTo.builder()
                .id(10L)
                .login("testGuest")
                .password("testGuestPassword")
                .name("TestGuestName")
                .role(Role.GUEST)
                .build();

        testNotSavedAdminTo = UserTo.builder()
                .login("admin")
                .name("Admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();
    }

}
