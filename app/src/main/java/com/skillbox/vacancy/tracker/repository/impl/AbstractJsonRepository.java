package com.skillbox.vacancy.tracker.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class AbstractJsonRepository {

    private static final String PATH_PREFIX = "data";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected boolean exists(String filename) {
        return Files.exists(Path.of(PATH_PREFIX, filename));
    }
    protected void save(String filename, Object object) {
        File file;
        try {
            Files.createDirectories(Path.of(PATH_PREFIX, getDirectoryPath()));
            file = new File(PATH_PREFIX, filename);
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("Не удалось создать файл: " + filename);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8)) {
            GSON.toJson(object, writer);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи вакансий в файл", e);
        }

    }

    protected <T> Optional<T> load(String filename, Type type) {
        File file = new File(PATH_PREFIX, filename);

        if (!file.exists() || !file.isFile()) {
            return Optional.empty();
        }

        try {
            if (Files.size(file.toPath()) == 0) {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении размера файла: " + filename, e);
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return Optional.of(GSON.fromJson(reader, type));
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Ошибка парсинга JSON в файле: " + filename, e);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filename, e);
        }
    }

    protected void delete(String filename) {
        try {
            Files.deleteIfExists(Path.of(PATH_PREFIX, filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected abstract String getDirectoryPath();
}
