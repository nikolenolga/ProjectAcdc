package com.javarush.nikolenko.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.io.IOException;

public interface QuestEditOperation {
    void execute(HttpServletRequest req) throws ServletException, IOException;
}
