package com.example.weatherapp.service;

import com.example.weatherapp.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapClient {


   private static final String GEO_BASE = "https://api.openweathermap.org/geo/1.0/direct";
   private static final String CURRENT_BASE = "https://api.openweathermap.org/data/2.5/weather";
   private static final String FORECAST_BASE = "https://api.openweathermap.org/data/2.5/forecast";


   private final HttpClient http = HttpClient.newBuilder().build();
   private final Gson gson = new Gson();
   private final String apiKey;


   public OpenWeatherMapClient(String apiKey) {
       if (apiKey == null || apiKey.isBlank()) {
           throw new IllegalArgumentException("Missing OpenWeather API key. Set OPENWEATHER_API_KEY env var.");
       }
       this.apiKey = apiKey.trim();
   }


   public WeatherReport fetchWeatherByCity(String cityQuery, Units units) throws IOException, InterruptedException, ApiException {
       GeoLocation loc = geocode(cityQuery);
       CurrentWeather current = getCurrent(loc, units);
       List<ForecastItem> forecast = getForecast3h(loc, units);
       return new WeatherReport(loc, current, forecast);
   }


   private GeoLocation geocode(String query) throws IOException, InterruptedException, ApiException {
       String q = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
       URI uri = URI.create(GEO_BASE + "?q=" + q + "&limit=1&appid=" + apiKey);
       JsonArray arr = getJsonArray(uri);
       if (arr == null || arr.isEmpty()) {
           throw new ApiException(404, "Location not found. Try a more specific city (e.g., \"Springfield, US\").");
       }


       JsonObject o = arr.get(0).getAsJsonObject();
       String name = optString(o, "name");
       String state = optString(o, "state");
       String country = optString(o, "country");
       double lat = o.get("lat").getAsDouble();
       double lon = o.get("lon").getAsDouble();
       return new GeoLocation(name, state, country, lat, lon);
   }


   private CurrentWeather getCurrent(GeoLocation loc, Units units) throws IOException, InterruptedException, ApiException {
       URI uri = URI.create(CURRENT_BASE
               + "?lat=" + loc.lat()
               + "&lon=" + loc.lon()
               + "&appid=" + apiKey
               + "&units=" + units.apiParam());


       JsonObject root = getJsonObject(uri);


       JsonObject main = root.getAsJsonObject("main");
       JsonObject wind = root.getAsJsonObject("wind");
       JsonObject sys = root.getAsJsonObject("sys");
       JsonArray weatherArr = root.getAsJsonArray("weather");
       JsonObject w0 = weatherArr.get(0).getAsJsonObject();


       double temp = main.get("temp").getAsDouble();
       double feelsLike = main.get("feels_like").getAsDouble();
       double tempMin = main.get("temp_min").getAsDouble();
       double tempMax = main.get("temp_max").getAsDouble();


       int humidity = main.get("humidity").getAsInt();
       int pressure = main.get("pressure").getAsInt();


       double windSpeed = wind.get("speed").getAsDouble();
       String conditionMain = optString(w0, "main");
       String conditionDesc = optString(w0, "description");
       String icon = optString(w0, "icon");


       long dt = root.get("dt").getAsLong();
       int tz = root.get("timezone").getAsInt();
       long sunrise = sys.get("sunrise").getAsLong();
       long sunset = sys.get("sunset").getAsLong();


       return new CurrentWeather(
               loc.displayName(),
               temp,
               feelsLike,
               tempMin,
               tempMax,
               humidity,
               pressure,
               windSpeed,
               conditionMain,
               conditionDesc,
               icon,
               Instant.ofEpochSecond(dt),
               tz,
               sunrise,
               sunset
       );
   }


   private List<ForecastItem> getForecast3h(GeoLocation loc, Units units) throws IOException, InterruptedException, ApiException {
       // cnt=40 => ~5 days in 3-hour blocks
       URI uri = URI.create(FORECAST_BASE
               + "?lat=" + loc.lat()
               + "&lon=" + loc.lon()
               + "&appid=" + apiKey
               + "&units=" + units.apiParam()
               + "&cnt=40");


       JsonObject root = getJsonObject(uri);
       int tz = root.getAsJsonObject("city").get("timezone").getAsInt();


       JsonArray list = root.getAsJsonArray("list");
       List<ForecastItem> items = new ArrayList<>();
       for (int i = 0; i < list.size(); i++) {
           JsonObject it = list.get(i).getAsJsonObject();
           long dt = it.get("dt").getAsLong();
           double temp = it.getAsJsonObject("main").get("temp").getAsDouble();
           JsonObject w0 = it.getAsJsonArray("weather").get(0).getAsJsonObject();
           String main = optString(w0, "main");
           String desc = optString(w0, "description");
           String icon = optString(w0, "icon");
           items.add(new ForecastItem(Instant.ofEpochSecond(dt), tz, temp, main, desc, icon));
       }
       return items;
   }


   private JsonObject getJsonObject(URI uri) throws IOException, InterruptedException, ApiException {
       HttpResponse<String> resp = send(uri);
       String body = resp.body() == null ? "" : resp.body();
       if (resp.statusCode() != 200) throw parseApiError(resp.statusCode(), body);


       JsonObject obj = gson.fromJson(body, JsonObject.class);
       if (obj != null && obj.has("cod")) {
           String cod = obj.get("cod").getAsString();
           if (!"200".equals(cod)) {
               String message = obj.has("message") ? obj.get("message").getAsString() : "API error";
               throw new ApiException(Integer.parseInt(cod), message);
           }
       }
       return obj;
   }


   private JsonArray getJsonArray(URI uri) throws IOException, InterruptedException, ApiException {
       HttpResponse<String> resp = send(uri);
       String body = resp.body() == null ? "" : resp.body();
       if (resp.statusCode() != 200) throw parseApiError(resp.statusCode(), body);
       return gson.fromJson(body, JsonArray.class);
   }


   private HttpResponse<String> send(URI uri) throws IOException, InterruptedException {
       HttpRequest req = HttpRequest.newBuilder()
               .uri(uri)
               .GET()
               .header("Accept", "application/json")
               .build();
       return http.send(req, HttpResponse.BodyHandlers.ofString());
   }


   private ApiException parseApiError(int httpStatus, String body) {
       try {
           JsonObject obj = gson.fromJson(body, JsonObject.class);
           String msg = (obj != null && obj.has("message")) ? obj.get("message").getAsString() : "API request failed";
           return new ApiException(httpStatus, msg);
       } catch (Exception ignored) {
           return new ApiException(httpStatus, "API request failed");
       }
   }


   private static String optString(JsonObject obj, String key) {
       return (obj != null && obj.has(key) && !obj.get(key).isJsonNull()) ? obj.get(key).getAsString() : "";
   }


   public static class ApiException extends Exception {
       private final int status;
       public ApiException(int status, String message) { super(message); this.status = status; }
       public int status() { return status; }
   }
}
