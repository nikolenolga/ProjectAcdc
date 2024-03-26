package com.javarush.khmelov.cmd;

import com.javarush.khmelov.BaseIT;
import com.javarush.khmelov.config.Winter;
import com.javarush.khmelov.util.Key;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListUserIT extends BaseIT {

    ListUser listUser = Winter.find(ListUser.class);

    @Test
    void whenGetListUsers_thenReturnJspPage() throws ServletException, IOException {
        String jspPage = listUser.doGet(request, response);

        assertEquals("WEB-INF/list-user.jsp", jspPage);
        verify(request).setAttribute(eq(Key.USERS), any(Collection.class));
    }
}