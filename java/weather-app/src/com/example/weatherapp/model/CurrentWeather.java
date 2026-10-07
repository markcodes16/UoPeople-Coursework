package com.example.weatherapp.model;

import java.time.Instant;

public record CurrentWeather(
       String locationLabel,
       double temperature,
       double feelsLike,
       double tempMin,
       double tempMax,
       int humidity,
       int pressure,
       double windSpeed,
       String conditionMain,
       String conditionDesc,
       String iconCode,
       Instant measuredAtUtc,
       int timezoneOffsetSeconds,
       long sunriseUtcSeconds,
       long sunsetUtcSeconds
) {}
