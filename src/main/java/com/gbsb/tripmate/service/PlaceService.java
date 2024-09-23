package com.gbsb.tripmate.service;

import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import jakarta.json.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.gbsb.tripmate.enums.ErrorCode.FAIL_ENCODING;

@Service
@Transactional
public class PlaceService {
    @Value("${kakaomap.key}")
    private String apiKey;

    public Place getPlaceFromApi(String address) {
        String placeData = getPlaceData(address);
        return parsePlace(placeData);
    }

    private Place parsePlace(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new MeetingException(FAIL_ENCODING);
        }

        JSONArray documents = (JSONArray) jsonObject.get("documents");
        if (documents != null && !documents.isEmpty()) {
            JSONObject firstDocument = (JSONObject) documents.get(0);

            String addressName = (String) firstDocument.get("address_name");
            JSONObject roadAddress = (JSONObject) firstDocument.get("road_address");
            String roadAddressName = (String) roadAddress.get("address_name");
            String latitude = (String) firstDocument.get("y");
            String longitude = (String) firstDocument.get("x");

            Place place = Place.builder()
                    .addressName(addressName)
                    .roadAddressName(roadAddressName)
                    .x(new BigDecimal(latitude))
                    .y(new BigDecimal(longitude))
                    .build();

            return place;
        } else {
            throw new MeetingException(ErrorCode.INVALID_ADDRESS);
        }
    }

    private String getPlaceData(String address) {
        String apiUrl = null; // 주소 검색하기 api
        try {
            apiUrl = "https://dapi.kakao.com/v2/local/search/address?query=" + URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MeetingException(FAIL_ENCODING);
        }

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "KakaoAK " + apiKey);
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            throw new MeetingException(ErrorCode.INVALID_ADDRESS);
        }

    }
}
