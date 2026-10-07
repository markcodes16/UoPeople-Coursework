package com.example.weatherapp.model;

import java.time.LocalDate;

public record DailyForecast(
       LocalDate localDate,
       int timezoneOffsetSeconds,
       double minTemp,
       double maxTemp,
       String iconCode,
       String conditionDesc
) {}
