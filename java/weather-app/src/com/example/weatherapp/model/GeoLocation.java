package com.example.weatherapp.model;

public record GeoLocation(
       String name,
       String state,
       String country,
       double lat,
       double lon
) {
   public String displayName() {
       StringBuilder sb = new StringBuilder();
       sb.append(name);
       if (state != null && !state.isBlank()) sb.append(", ").append(state);
       if (country != null && !country.isBlank()) sb.append(", ").append(country);
       return sb.toString();
   }


   public String shortLabel() {
       // Mimic mobile style: CITY (uppercase)
       return (name == null ? "" : name).toUpperCase();
   }
}
