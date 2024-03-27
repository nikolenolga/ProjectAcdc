package com.javarush.khmelov.cmd;

import com.javarush.khmelov.BaseIT;
import com.javarush.khmelov.config.Winter;
import com.javarush.khmelov.util.Key;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HomeIT extends BaseIT {


    @Test
    void whenOpenPage_thenCommandReturnJspPage() {
        Home home = Winter.find(Home.class);
        String jsp = home.doGet(request);

        assertEquals("WEB-INF/home.jsp", jsp);
        verify(request).setAttribute(eq(Key.QUESTS), any(Collection.class));
    }
}