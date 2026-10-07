package com.example.weatherapp.model;

import java.time.Instant;

public record SearchHistoryEntry(
       String queryText,
       String resolvedLocation,
       Instant searchedAtUtc
) {}
