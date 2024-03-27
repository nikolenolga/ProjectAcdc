package com.javarush.khmelov.controller;

import com.javarush.khmelov.BaseIT;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FrontControllerIT extends BaseIT {

    @Test
    void whenFrontSendIncorrectMethod_thenGiveException() {
        FrontController frontController = new FrontController();
        when(request.getMethod()).thenReturn("IncorrectMethod");
        frontController.init(servletConfig);
        when(request.getRequestURI()).thenReturn("/");
        assertThrows(UnsupportedOperationException.class, () -> frontController.service(request, response));
    }

}