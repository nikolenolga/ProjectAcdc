package com.javarush.khmelov.cmd;

import com.javarush.khmelov.config.Winter;
import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.service.UserService;
import com.javarush.khmelov.util.Key;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EditUserIT extends BaseIT {

    private final EditUser editUser = Winter.find(EditUser.class);
    private final UserService userService = Winter.find(UserService.class);

    @Test
    void whenOpenPage_thenCommandReturnJspPage() {
        User user = userService.getAll().stream().findFirst().orElseThrow();
        Mockito.when(request.getParameter(Key.ID)).thenReturn(user.getId().toString());
        String jsp = editUser.doGet(request, response);
        assertEquals("WEB-INF/edit-user.jsp", jsp);
        verify(request).setAttribute(eq(Key.USER), eq(user));
    }

    @Test
    void whenUpdateUser_thenGetPageByUserId() throws Exception {
        Mockito.when(request.getParameter(Key.LOGIN)).thenReturn("TestName");
        Mockito.when(request.getParameter(Key.PASSWORD)).thenReturn("TestPassword");
        Mockito.when(request.getParameter(Key.ROLE)).thenReturn(Role.GUEST.toString());
        Mockito.when(request.getParameter(Key.ID)).thenReturn("1");
        String page = editUser.doPost(request, response);
        assertTrue(page.endsWith("?id=1"));
    }
}