package com.javarush.khmelov.filter;

import com.javarush.khmelov.entity.Role;
import com.javarush.khmelov.entity.User;
import com.javarush.khmelov.util.Go;
import com.javarush.khmelov.util.RequestHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {
        Go.INDEX, Go.HOME,
        Go.SIGNUP, Go.LOGIN, Go.LOGOUT,
        Go.LIST_USER, Go.PROFILE, Go.EDIT_USER,
        Go.CREATE_QUEST, Go.QUEST,
        Go.PLAY_GAME,
        Go.STATISTICS
})
public class AuthorizationFilter extends HttpFilter {

    private final Map<Role, List<String>> uriMap = Map.of(
            Role.GUEST, List.of(
                    Go.HOME, Go.INDEX, Go.LOGIN, Go.SIGNUP, Go.STATISTICS
            ),
            Role.USER, List.of(
                    Go.HOME, Go.INDEX, Go.LOGIN, Go.SIGNUP, Go.STATISTICS,
                    Go.PROFILE, Go.LOGOUT, Go.EDIT_USER, Go.PLAY_GAME, Go.QUEST
            ),
            Role.ADMIN, List.of(
                    Go.HOME, Go.INDEX, Go.LOGIN, Go.SIGNUP, Go.STATISTICS,
                    Go.PROFILE, Go.LOGOUT, Go.EDIT_USER, Go.PLAY_GAME, Go.QUEST,
                    Go.LIST_USER, Go.CREATE_QUEST)
    );

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Optional<User> user = RequestHelper.getUser(req.getSession());
        Role role = user.isEmpty()
                ? Role.GUEST
                : user.get().getRole();
        String cmdUri = RequestHelper.getCommand(req);
        if (uriMap.get(role).contains(cmdUri)) {
            chain.doFilter(req, res);
        } else {
            RequestHelper.setError(req, "Недостаточно прав для этой операции. Текущая роль: " + role);
            res.sendRedirect(Go.LOGIN);
        }
    }

}
