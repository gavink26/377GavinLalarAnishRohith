/*
 Represents information about a NWS weather station
*/
public class WeatherStation {
   private String name;
   private String id;
   private String state;
   private double lat;
   private double lng;
   
   WeatherStation(String name, String id, String state, double lat, double lng) {
      this.name = name;
      this.id = id;
      this.lat = lat;
      this.lng = lng;
      this.state = state;   
   }
   
 
   public String getId() { 
      return id;
   }
   
  
   public String getName() { 
      return name;
   }
   
   
   public double getLatitude() {
      return lat;
   }
   
  
   public boolean isLocatedInState(String st) {
      return this.state.equals(st);
   }

  
   public boolean isFurtherSouth(WeatherStation other) {
      return this.lat < other.lat; 
   }
}
