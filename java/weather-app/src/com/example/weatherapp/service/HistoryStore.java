package com.example.weatherapp.service;

import com.example.weatherapp.model.SearchHistoryEntry;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryStore {


   private static final int MAX_ITEMS = 20;


   /**
    * Gson does not support java.time.Instant out of the box, and reflective access is restricted on Java 17+.
    * We serialize Instant as ISO-8601 strings (e.g., "2026-01-07T21:32:10Z").
    */
   private final Gson gson = new GsonBuilder()
           .registerTypeAdapter(Instant.class, new InstantTypeAdapter())
           .create();


   private final Path filePath;


   public HistoryStore() {
       this.filePath = Path.of(System.getProperty("user.home"), ".weather_mobile_style_history.json");
   }


   public List<SearchHistoryEntry> load() {
       try {
           if (!Files.exists(filePath)) return Collections.emptyList();
           String json = Files.readString(filePath, StandardCharsets.UTF_8);
           Type type = new TypeToken<List<SearchHistoryEntry>>() {}.getType();
           List<SearchHistoryEntry> list = gson.fromJson(json, type);
           return list == null ? Collections.emptyList() : list;
       } catch (Exception e) {
           return Collections.emptyList();
       }
   }


   public void save(List<SearchHistoryEntry> entries) {
       try {
           List<SearchHistoryEntry> trimmed = new ArrayList<>(entries);
           if (trimmed.size() > MAX_ITEMS) trimmed = trimmed.subList(0, MAX_ITEMS);
           Files.writeString(filePath, gson.toJson(trimmed), StandardCharsets.UTF_8);
       } catch (IOException ignored) {}
   }


   public static SearchHistoryEntry entry(String query, String resolvedLocation) {
       return new SearchHistoryEntry(query, resolvedLocation, Instant.now());
   }


   private static final class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
       @Override
       public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
           return src == null ? JsonNull.INSTANCE : new JsonPrimitive(src.toString());
       }


       @Override
       public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
           if (json == null || json.isJsonNull()) return Instant.EPOCH;
           return Instant.parse(json.getAsString());
       }
   }
}
