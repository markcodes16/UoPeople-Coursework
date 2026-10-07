package com.example.weatherapp;

import com.example.weatherapp.model.*;
import com.example.weatherapp.service.HistoryStore;
import com.example.weatherapp.service.OpenWeatherMapClient;
import com.example.weatherapp.service.OpenWeatherMapClient.ApiException;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WeatherAppController {


   // Root + "phone"
   @FXML private StackPane root;
   @FXML private StackPane phone;
   @FXML private Pane effectsPane;


   // Search + units
   @FXML private TextField cityField;
   @FXML private Button searchButton;
   @FXML private ToggleButton dayToggle;
   @FXML private ToggleButton weekToggle;


   @FXML private RadioButton metricRadio;
   @FXML private RadioButton imperialRadio;


   // Top labels
   @FXML private Label timeLabel;
   @FXML private Label locationLabel;


   // Center ring
   @FXML private Label conditionLabel;
   @FXML private Label tempLabel;
   @FXML private Label hiLoLabel;


   @FXML private Label messageLabel;


   // Forecast containers
   @FXML private HBox hourlyBox;
   @FXML private HBox dailyBox;


   // Drawer (history)
   @FXML private StackPane drawerOverlay;
   @FXML private VBox drawer;
   @FXML private ListView<SearchHistoryEntry> historyList;


   // Status
   @FXML private Label statusLabel;


   private final ObservableList<SearchHistoryEntry> historyItems = FXCollections.observableArrayList();
   private final HistoryStore historyStore = new HistoryStore();


   private Units units = Units.IMPERIAL; // matches the vibe of the sample image
   private String lastQuery;


   private OpenWeatherMapClient client;


   private static final List<String> AFFIRMATIONS = List.of(
           "You totally got this.",
           "You are extraordinary.",
           "You look amazing today.",
           "Keep going. You're doing great.",
           "Today is yours to win."
   );


   @FXML
   private void initialize() {
       // Units ToggleGroup
       ToggleGroup unitsGroup = new ToggleGroup();
       metricRadio.setToggleGroup(unitsGroup);
       imperialRadio.setToggleGroup(unitsGroup);
       imperialRadio.setSelected(true);
       units = Units.IMPERIAL;


       ChangeListener<Boolean> unitListener = (obs, ov, nv) -> {
           if (metricRadio.isSelected()) units = Units.METRIC;
           if (imperialRadio.isSelected()) units = Units.IMPERIAL;
           if (lastQuery != null && !lastQuery.isBlank()) onSearch();
       };
       metricRadio.selectedProperty().addListener(unitListener);
       imperialRadio.selectedProperty().addListener(unitListener);


       // Day/Week segmented toggle group
       ToggleGroup seg = new ToggleGroup();
       dayToggle.setToggleGroup(seg);
       weekToggle.setToggleGroup(seg);
       dayToggle.setSelected(true);


       seg.selectedToggleProperty().addListener((obs, ov, nv) -> {
           updateForecastVisibility();
       });
       updateForecastVisibility();


       // History
       historyItems.setAll(historyStore.load());
       historyList.setItems(historyItems);
       historyList.setCellFactory(lv -> new ListCell<>() {
           @Override protected void updateItem(SearchHistoryEntry item, boolean empty) {
               super.updateItem(item, empty);
               if (empty || item == null) { setText(null); return; }
               setText(item.resolvedLocation());
           }
       });
       historyList.setOnMouseClicked(e -> {
           if (e.getClickCount() == 1) {
               SearchHistoryEntry item = historyList.getSelectionModel().getSelectedItem();
               if (item != null) {
                   cityField.setText(item.queryText());
                   closeDrawer();
                   onSearch();
               }
           }
       });


drawerOverlay.setVisible(false);
       drawer.setTranslateX(-300);


       // Clock (simple local system time)
       timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("H:mm")));


       // Client
       try {
           client = new OpenWeatherMapClient(System.getenv("OPENWEATHER_API_KEY"));
           setStatus("Ready.");
       } catch (IllegalArgumentException e) {
           setStatus("Missing API key. Set OPENWEATHER_API_KEY before running.");
           showError("API Key Required",
                   "OpenWeather API key not found.",
                   "Set environment variable OPENWEATHER_API_KEY, then re-run the app.");
           searchButton.setDisable(true);
       }


       // Starter UI
       locationLabel.setText("—");
       tempLabel.setText("—");
       conditionLabel.setText("—");
       hiLoLabel.setText("H: —   L: —");
       messageLabel.setText(AFFIRMATIONS.get(0));
   }


   @FXML
   public void onSearch() {
       String query = cityField.getText() == null ? "" : cityField.getText().trim();
       if (query.isBlank()) {
           showError("Invalid Input", "Location is required.",
                   "Enter a city name (e.g., \"New York\" or \"Paris, FR\").");
           return;
       }
       lastQuery = query;


       setStatus("Fetching weather for: " + query + " (" + units.apiParam() + ") ...");
       searchButton.setDisable(true);


       new Thread(() -> {
           try {
               WeatherReport report = client.fetchWeatherByCity(query, units);
               Platform.runLater(() -> render(report, query));
           } catch (ApiException e) {
               Platform.runLater(() -> showError("Weather API Error",
                       "The weather service returned an error.",
                       "Code: " + e.status() + "\nMessage: " + e.getMessage()));
           } catch (IOException | InterruptedException e) {
               Platform.runLater(() -> showError("Network Error",
                       "Unable to fetch weather data.",
                       e.getMessage() == null ? "Please check your internet connection and try again." : e.getMessage()));
           } finally {
               Platform.runLater(() -> searchButton.setDisable(false));
           }
       }, "weather-fetch-thread").start();
   }


   private void render(WeatherReport report, String originalQuery) {
       CurrentWeather c = report.current();


       // Mobile style header
       locationLabel.setText(report.location().shortLabel());
       conditionLabel.setText(capitalize(c.conditionMain()));
       tempLabel.setText(String.format("%.0f%s", c.temperature(), units.tempSymbol()));
       hiLoLabel.setText(String.format("H:%.0f%s  L:%.0f%s", c.tempMax(), units.tempSymbol(), c.tempMin(), units.tempSymbol()));
       messageLabel.setText(pickAffirmation(c.conditionMain()));


       applyTheme(c, report.location());


       // Forecast data
       List<ForecastItem> all = report.forecast3h();
       List<ForecastItem> next24h = all.stream().limit(8).collect(Collectors.toList());
       List<DailyForecast> next5d = toDailyForecast(all).stream().limit(5).collect(Collectors.toList());


       renderHourly(next24h);
       renderDaily(next5d);
       updateForecastVisibility();


       // Save history (newest first)
       SearchHistoryEntry entry = HistoryStore.entry(originalQuery, report.location().displayName());
       historyItems.removeIf(h -> h.queryText().equalsIgnoreCase(originalQuery));
       historyItems.add(0, entry);
       historyStore.save(historyItems);
       historyList.getSelectionModel().clearSelection();


       setStatus("Updated: " + report.location().displayName());
   }


   private void renderHourly(List<ForecastItem> items) {
       hourlyBox.getChildren().clear();


       for (ForecastItem it : items) {
           VBox pill = new VBox(6);
           pill.getStyleClass().add("pill");


           ZoneOffset off = ZoneOffset.ofTotalSeconds(it.timezoneOffsetSeconds());
           String t = LocalDateTime.ofInstant(it.forecastTimeUtc(), off).format(DateTimeFormatter.ofPattern("ha")).toLowerCase();


           Label time = new Label(t.replace("am"," AM").replace("pm"," PM"));
           time.getStyleClass().add("pill-time");


           ImageView icon = new ImageView(iconImage(it.iconCode()));
           icon.setFitWidth(26);
           icon.setFitHeight(26);
           icon.setPreserveRatio(true);


           Label temp = new Label(String.format("%.0f%s", it.temperature(), units.tempSymbol()));
           temp.getStyleClass().add("pill-temp");


           pill.getChildren().addAll(time, icon, temp);
           hourlyBox.getChildren().add(pill);
       }
   }


   private void renderDaily(List<DailyForecast> days) {
       dailyBox.getChildren().clear();
       for (DailyForecast d : days) {
           VBox pill = new VBox(6);
           pill.getStyleClass().addAll("pill", "pill-day");


           String day = d.localDate().getDayOfWeek().toString().substring(0,3);
           Label dayLabel = new Label(day);
           dayLabel.getStyleClass().add("pill-time");


           ImageView icon = new ImageView(iconImage(d.iconCode()));
           icon.setFitWidth(26);
           icon.setFitHeight(26);
           icon.setPreserveRatio(true);


           Label mm = new Label(String.format("%.0f/%.0f%s", d.maxTemp(), d.minTemp(), units.tempSymbol()));
           mm.getStyleClass().add("pill-temp");


           pill.getChildren().addAll(dayLabel, icon, mm);
           dailyBox.getChildren().add(pill);
       }
   }


   private void updateForecastVisibility() {
       boolean day = dayToggle.isSelected();
       hourlyBox.setManaged(day);
       hourlyBox.setVisible(day);


       dailyBox.setManaged(!day);
       dailyBox.setVisible(!day);
   }


   private List<DailyForecast> toDailyForecast(List<ForecastItem> items) {
       if (items == null || items.isEmpty()) return List.of();


       int tz = items.get(0).timezoneOffsetSeconds();
       ZoneOffset off = ZoneOffset.ofTotalSeconds(tz);


       Map<LocalDate, List<ForecastItem>> byDate = new LinkedHashMap<>();
       for (ForecastItem it : items) {
           LocalDate d = LocalDateTime.ofInstant(it.forecastTimeUtc(), off).toLocalDate();
           byDate.computeIfAbsent(d, k -> new ArrayList<>()).add(it);
       }


       List<DailyForecast> out = new ArrayList<>();
       for (Map.Entry<LocalDate, List<ForecastItem>> e : byDate.entrySet()) {
           double min = e.getValue().stream().mapToDouble(ForecastItem::temperature).min().orElse(Double.NaN);
           double max = e.getValue().stream().mapToDouble(ForecastItem::temperature).max().orElse(Double.NaN);


           // Pick an icon near midday if possible; otherwise first item.
           ForecastItem pick = e.getValue().get(0);
           for (ForecastItem it : e.getValue()) {
               int hour = LocalDateTime.ofInstant(it.forecastTimeUtc(), off).getHour();
               if (hour >= 11 && hour <= 14) { pick = it; break; }
           }


           out.add(new DailyForecast(e.getKey(), tz, min, max, pick.iconCode(), pick.conditionDesc()));
       }
       return out;
   }


   private void applyTheme(CurrentWeather c, GeoLocation loc) {
       // Theme class by condition and time of day
       phone.getStyleClass().removeAll("theme-clear", "theme-clouds", "theme-rain", "theme-snow", "theme-night");
       effectsPane.getChildren().clear();


       String cond = (c.conditionMain() == null ? "" : c.conditionMain()).toLowerCase(Locale.ROOT);


       boolean night = isNight(c);
       if (night) phone.getStyleClass().add("theme-night");


       if (cond.contains("rain") || cond.contains("drizzle") || cond.contains("thunder")) {
           phone.getStyleClass().add("theme-rain");
           addRainEffect();
       } else if (cond.contains("cloud") || cond.contains("mist") || cond.contains("fog") || cond.contains("haze")) {
           phone.getStyleClass().add("theme-clouds");
           addCloudEffect();
       } else if (cond.contains("snow")) {
           phone.getStyleClass().add("theme-snow");
           addSnowEffect();
       } else {
           phone.getStyleClass().add("theme-clear");
           addSunGlow();
       }
   }


   private boolean isNight(CurrentWeather c) {
       long now = c.measuredAtUtc().getEpochSecond();
       return now < c.sunriseUtcSeconds() || now > c.sunsetUtcSeconds();
   }


   private void addRainEffect() {
       // Static diagonal rain lines (simple, lightweight)
       for (int i = 0; i < 42; i++) {
           Line l = new Line(0, 0, 0, 60);
           l.setStroke(Color.rgb(255, 255, 255, 0.22));
           l.setStrokeWidth(2);
           l.setRotate(12);
           l.setTranslateX((i % 14) * 42 - 260);
           l.setTranslateY((i / 14) * 120 - 120);
           effectsPane.getChildren().add(l);
       }
   }


   private void addCloudEffect() {
       // Soft translucent circles to suggest clouds
       for (int i = 0; i < 7; i++) {
           Circle c = new Circle(90 + i * 4);
           c.setFill(Color.rgb(255, 255, 255, 0.10));
           c.setTranslateX(-160 + i * 60);
           c.setTranslateY(140 - i * 10);
           effectsPane.getChildren().add(c);
       }
   }


   private void addSnowEffect() {
       for (int i = 0; i < 40; i++) {
           Circle flake = new Circle(2 + (i % 3));
           flake.setFill(Color.rgb(255, 255, 255, 0.55));
           flake.setTranslateX((i % 10) * 55 - 240);
           flake.setTranslateY((i / 10) * 85 - 180);
           effectsPane.getChildren().add(flake);
       }
   }


   private void addSunGlow() {
       Circle glow = new Circle(120);
       glow.setFill(Color.rgb(255, 255, 255, 0.18));
       glow.setTranslateX(210);
       glow.setTranslateY(-110);
       effectsPane.getChildren().add(glow);
   }


   private String pickAffirmation(String conditionMain) {
       // A small bit of personality without being "random noise"
       if (conditionMain == null) return AFFIRMATIONS.get(0);
       String c = conditionMain.toLowerCase(Locale.ROOT);
       if (c.contains("rain") || c.contains("thunder")) return "Stay cozy. You've got this.";
       if (c.contains("snow")) return "Warm up—today can still be great.";
       if (c.contains("cloud")) return "Even cloudy days count.";
       return AFFIRMATIONS.get(Math.abs(conditionMain.hashCode()) % AFFIRMATIONS.size());
   }


       @FXML
   private void consumeClick(MouseEvent event) {
       // Prevent clicks inside the drawer from bubbling to the overlay (which closes the drawer).
       event.consume();
   }


@FXML
   public void openDrawer() {
       historyList.getSelectionModel().clearSelection();
drawerOverlay.setVisible(true);
       TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(220), drawer);
       tt.setFromX(-300);
       tt.setToX(0);
       tt.play();
   }


   @FXML
   public void closeDrawer() {
       TranslateTransition tt = new TranslateTransition(javafx.util.Duration.millis(220), drawer);
       tt.setFromX(drawer.getTranslateX());
       tt.setToX(-300);
       tt.setOnFinished(e -> drawerOverlay.setVisible(false));
       tt.play();
   }


   private Image iconImage(String iconCode) {
       String safe = (iconCode == null || iconCode.isBlank()) ? "01d" : iconCode;
       String url = "https://openweathermap.org/img/wn/" + safe + "@2x.png";
       return new Image(url, true);
   }


   private void setStatus(String msg) {
       statusLabel.setText(msg == null ? "" : msg);
   }


   private static String capitalize(String s) {
       if (s == null || s.isBlank()) return "";
       return s.substring(0, 1).toUpperCase() + s.substring(1);
   }


   private void showError(String title, String header, String content) {
       Alert alert = new Alert(Alert.AlertType.ERROR);
       alert.setTitle(title);
       alert.setHeaderText(header);
       alert.setContentText(content);
       alert.showAndWait();
   }
}
