package com.example.weatherapp.model;

import java.time.Instant;

public record ForecastItem(
       Instant forecastTimeUtc,
       int timezoneOffsetSeconds,
       double temperature,
       String conditionMain,
       String conditionDesc,
       String iconCode
) {}
