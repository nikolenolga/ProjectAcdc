package com.javarush.khmelov.controller;

import com.javarush.khmelov.BaseIT;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FrontControllerIT extends BaseIT {

    @Test
    void whenFrontSendIncorrectMethod_thenGiveException() throws Exception {
        FrontController frontController = new FrontController();
        when(request.getMethod()).thenReturn("IncorrectMethod");
        frontController.init(servletConfig);
        when(request.getRequestURI()).thenReturn("/");
        assertThrows(UnsupportedOperationException.class, () -> frontController.service(request, response));
    }

}