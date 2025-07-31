package es.unex.cum.mdai.vrp.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public final class DistanceMatrixCalculator {

    private static final String API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private static final String API_KEY = "AIzaSyC-OiLfbvtxPNzq6ZcFaUnLJj67zgu2RVY";
    
    private static DistanceMatrixCalculator instance;

    private DistanceMatrixCalculator() {
    }
    
    public static DistanceMatrixCalculator getInstance() {
        if (instance == null) {
            synchronized (DistanceMatrixCalculator.class) {
                if (instance == null) {
                    instance = new DistanceMatrixCalculator();
                }
            }
        }
        return instance;
    }

    public long[][] getDistanceMatrix(List<String> addresses) throws Exception {
        String origins = encodeAddresses(addresses);
        String destinations = encodeAddresses(addresses);

        String urlString = String.format("%s?origins=%s&destinations=%s&key=%s", API_URL, origins, destinations, API_KEY);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        JSONObject jsonResponse = new JSONObject(content.toString());
        JSONArray rows = jsonResponse.getJSONArray("rows");

        long[][] distanceMatrix = new long[addresses.size()][addresses.size()];
        for (int i = 0; i < rows.length(); i++) {
            JSONArray elements = rows.getJSONObject(i).getJSONArray("elements");
            for (int j = 0; j < elements.length(); j++) {
                JSONObject element = elements.getJSONObject(j);
                if (element.getString("status").equals("OK")) {
                    long distance = element.getJSONObject("distance").getLong("value");
                    distanceMatrix[i][j] = distance;
                } else {
                    distanceMatrix[i][j] = -1; // Indicates that the route is not available
                }
            }
        }

        return distanceMatrix;
    }

    private String encodeAddresses(List<String> addresses) throws Exception {
        StringBuilder encodedAddresses = new StringBuilder();
        for (String address : addresses) {
            if (encodedAddresses.length() > 0) {
                encodedAddresses.append('|');
            }
            encodedAddresses.append(URLEncoder.encode(address, StandardCharsets.UTF_8.toString()));
        }
        return encodedAddresses.toString();
    }
}