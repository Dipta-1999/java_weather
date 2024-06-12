import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    private static final String API_KEY = "ba301c30ee79d438a3f3b921655342fc"; // Replace with your API key
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        while (true) {
            System.out.println("Enter city name:");
            String city = sc.next(); // Change this to get the weather for a different city
            if (!city.equals("quit")) {
                String response = getWeatherData(city);
        
                if (response != null) {
                    parseAndDisplayWeather(response);
                } else {
                    System.out.println("Error retrieving weather data.");
                }
                
            }
            else  
            {
                break;
            }
            
        }
        
    }

    private static String getWeatherData(String city) {
        
        StringBuilder result = new StringBuilder();
        //String city = sc.nextLine();
        try {
            String urlStr = BASE_URL + city + "&appid=" + API_KEY;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void parseAndDisplayWeather(String response) {
        try {
            // Manually parse JSON response (simplified)
            String cityName = extractValue(response, "\"name\":\"", "\"");

            String tempStr = extractValue(response, "\"temp\":", ",");
            double temperature = Double.parseDouble(tempStr) - 273.15; // Convert from Kelvin to Celsius
            
            String tempfeeel = extractValue(response, "\"feels_like\":", ",");
            double temperaturefeel = Double.parseDouble(tempfeeel) - 273.15; // Convert from Kelvin to Celsius

            String humidityStr = extractValue(response, "\"humidity\":", "}");
            int humidity = Integer.parseInt(humidityStr);
            
            String description = extractValue(response, "\"description\":\"", "\"");

            System.out.println("City: " + cityName);
            System.out.println("Temperature: " + temperature + "°C");
            System.out.println("Temperature feels like: " + temperaturefeel + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Description: " + description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Helper method to extract values from JSON strings
    private static String extractValue(String json, String startDelimiter, String endDelimiter) {
        int startIndex = json.indexOf(startDelimiter) + startDelimiter.length();
        int endIndex = json.indexOf(endDelimiter, startIndex);
        return json.substring(startIndex, endIndex);
    }
}