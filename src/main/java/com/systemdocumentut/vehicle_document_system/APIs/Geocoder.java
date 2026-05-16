package com.systemdocumentut.vehicle_document_system.APIs;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Geocoder {
    // Usamos el endpoint de Geocoding que es más preciso para direcciones
    private static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String API_KEY = "AIzaSyD3otHIjATVlrMFn32WOapLNblx3HdWI0w";

    public String getLatLng(String address) throws IOException, InterruptedException {
        if (address == null || address.isEmpty()) return "0,0";

        HttpClient client = HttpClient.newHttpClient();
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEOCODING_URL + encodedAddress + "&key=" + API_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        
        // Google devuelve los resultados en un array llamado "results"
        if (root.has("results") && root.get("results").size() > 0) {
            JsonNode location = root.get("results").get(0).get("geometry").get("location");
            return location.get("lat").asText() + "," + location.get("lng").asText();
        }
        
        return "0,0"; // Si no encuentra nada
    }
}