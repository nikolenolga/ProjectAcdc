package com.javarush.nikolenko.controller;

import com.javarush.nikolenko.config.NanoSpring;
import com.javarush.nikolenko.service.ImageService;
import com.javarush.nikolenko.utils.UrlHelper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@MultipartConfig
@WebServlet(value = {UrlHelper.IMAGES_X, UrlHelper.UPLOAD_IMAGE})
public class ImageServlet extends HttpServlet {
    private ImageService imageService;

    @SneakyThrows
    @Override
    public void init(ServletConfig config) throws ServletException {
        imageService = NanoSpring.find(ImageService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String target = req.getContextPath() + UrlHelper.IMAGES;
        String nameImage = requestURI.replace(target, "");

        Path path = imageService.getImagePath(nameImage);
        Files.copy(path, resp.getOutputStream());
    }
}
