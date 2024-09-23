package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.SearchPlaceResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.PlaceRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.gbsb.tripmate.enums.ErrorCode.FAIL_ENCODING;

@Service
@Transactional
public class PlaceService {
    @Value("${kakaomap.key}")
    private String apiKey;
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getPlaceWithKeywordFromApi(String placeName) {
        // 장소 검색 api
        String placeDataWithKeyword = getPlaceWithKeywordData(placeName, 1, 15);
        SearchPlaceResponse searchPlaceResponse = parsePlaceWithKeyword(placeDataWithKeyword, 1);
        while (!searchPlaceResponse.isEnd()) {
            if (searchPlaceResponse.getPage() >= 1 && searchPlaceResponse.getPage() <= 45) {
                placeDataWithKeyword = getPlaceWithKeywordData(placeName, searchPlaceResponse.getPage() + 1, 15);
                searchPlaceResponse = parsePlaceWithKeyword(placeDataWithKeyword, searchPlaceResponse.getPage() + 1);
            }
        }
        return placeRepository.findAllByPlaceNameContaining(placeName);
    }

    private SearchPlaceResponse parsePlaceWithKeyword(String jsonString, int page) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new MeetingException(FAIL_ENCODING);
        }

        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Long totalCount = (Long) meta.get("total_count");
        Long pageableCount = (Long) meta.get("pageable_count");
        boolean isEnd = (Boolean) meta.get("is_end");

        JSONArray documents = (JSONArray) jsonObject.get("documents");
        List<Place> places = new ArrayList<>();

        if (documents != null && !documents.isEmpty()) {
            for (Object docObj : documents) {
                JSONObject document = (JSONObject) docObj;

                String placeId = (String) document.get("id");
                String addressName = (String) document.get("address_name");
                String roadAddressName = (String) document.get("road_address_name");
                String placeName = (String) document.get("place_name");
                String phone = (String) document.get("phone");
                String placeUrl = (String) document.get("place_url");
                String latitude = (String) document.get("y");
                String longitude = (String) document.get("x");

                Optional<Place> findPlace = placeRepository.findById(Long.parseLong(placeId));
                if (findPlace.isEmpty()) {
                    Place place = Place.builder()
                            .placeId(Long.parseLong(placeId))
                            .addressName(addressName)
                            .roadAddressName(roadAddressName)
                            .placeName(placeName)
                            .phone(phone)
                            .placeUrl(placeUrl)
                            .x(new BigDecimal(longitude))
                            .y(new BigDecimal(latitude))
                            .build();
                    placeRepository.save(place);
                } else System.out.println("findPlace = " + findPlace.get().getPlaceName());
            }
        } else {
            throw new MeetingException(ErrorCode.INVALID_ADDRESS);
        }

        return new SearchPlaceResponse(places, page, isEnd);
    }

    private String getPlaceWithKeywordData(String placeName, int page, int size) {
        // 키워드로 장소 검색하기 api
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
                    + URLEncoder.encode(placeName, "UTF-8")
                    + "&page=" + page
                    + "&size=" + size;
            // 페이징 처
            return getData(apiUrl);

        } catch (UnsupportedEncodingException e) {
            throw new MeetingException(FAIL_ENCODING);
        }
    }

    private String getData(String apiUrl) {
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
