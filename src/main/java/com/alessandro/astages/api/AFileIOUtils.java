package com.alessandro.astages.api;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParamsAndMethodsReturn
public class AFileIOUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static void createDirectory(File dir) {
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                AStages.LOGGER.error("Impossible to make directory: {} ", dir.getAbsolutePath());
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File getOrCreateFile(File file) {
        if (!file.exists()) {
            try {
                // Create file only if DOESN'T exist
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

    public static <E> List<E> readListOfDefault(File file, Class<E> elementClazz) {
        var content = readList(file, elementClazz);
        return content == null ? new ArrayList<>() : content;
    }

    public static <E> @Nullable List<E> readList(File file, Class<E> elementClazz) {
        return readFileContent(file, TypeToken.getParameterized(List.class, elementClazz).getType());
    }

    public static <K, V> Map<K, V> readMapOrDefault(File file, Class<K> keyClazz, Class<V> valueClazz) {
        var content = readMap(file, keyClazz, valueClazz);
        return content == null ? new HashMap<>() : content;
    }

    public static <K, V> @Nullable Map<K, V> readMap(File file, Class<K> keyClazz, Class<V> valueClazz) {
        return readFileContent(file, TypeToken.getParameterized(Map.class, keyClazz, valueClazz).getType());
    }

    public static <T> @Nullable T readFileContent(File file, Type type) {
        try (var fileReader = new FileReader(file)) {
            return GSON.fromJson(fileReader, type);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        return null;
    }

    public static <T> void writeFileContent(File file, T content) {
        try (var fileWriter = new FileWriter(file)) {
            GSON.toJson(content, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    private static CompoundTag readFileNbt(File file) {
        CompoundTag tag = null;
        try { tag = NbtIo.read(file); } catch (IOException ignoredException) { }
        if (tag == null) { tag = new CompoundTag(); }

        return tag;
    }

    private static void writeFileNbt(CompoundTag tag, File file) {
        try {
            NbtIo.write(tag, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
