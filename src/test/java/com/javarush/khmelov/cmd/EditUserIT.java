package com.javarush.khmelov.cmd;

import com.javarush.khmelov.BaseIT;
import com.javarush.khmelov.config.Winter;
import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.UserService;
import com.javarush.khmelov.util.Key;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EditUserIT extends BaseIT {


    private final EditUser editUser = Winter.find(EditUser.class);
    private final UserService userService = Winter.find(UserService.class);

    @Test
    void whenOpenPage_thenCommandReturnJspPage() {
        User user = userService.getAll().stream().findFirst().orElseThrow();
        when(request.getParameter(Key.ID)).thenReturn(user.getId().toString());

        String jspView = editUser.doGet(request);
        assertEquals("WEB-INF/edit-user.jsp", jspView);
        verify(request).setAttribute(eq(Key.USER), eq(user));
    }

    @Test
    void whenUpdateUser_thenGetPageByUserId() throws Exception {
        when(request.getParameter(Key.LOGIN)).thenReturn("TestName");
        when(request.getParameter(Key.PASSWORD)).thenReturn("TestPassword");
        when(request.getParameter(Key.ROLE)).thenReturn(Role.GUEST.toString());
        final String ID = "1";
        when(request.getParameter(Key.ID)).thenReturn(ID);
        String redirectUri = editUser.doPost(request);

        assertTrue(redirectUri.endsWith("?id=" + ID));
    }
}