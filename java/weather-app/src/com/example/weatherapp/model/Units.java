package com.example.weatherapp.model;

public enum Units {
   METRIC("metric", "°C", "m/s"),
   IMPERIAL("imperial", "°F", "mph");


   private final String apiParam;
   private final String tempSymbol;
   private final String windSymbol;


   Units(String apiParam, String tempSymbol, String windSymbol) {
       this.apiParam = apiParam;
       this.tempSymbol = tempSymbol;
       this.windSymbol = windSymbol;
   }


   public String apiParam() { return apiParam; }
   public String tempSymbol() { return tempSymbol; }
   public String windSymbol() { return windSymbol; }
}
