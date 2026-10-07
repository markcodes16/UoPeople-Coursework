package com.example.weatherapp.model;

import java.util.List;

public record WeatherReport(
       GeoLocation location,
       CurrentWeather current,
       List<ForecastItem> forecast3h // up to 40 items
) {}
