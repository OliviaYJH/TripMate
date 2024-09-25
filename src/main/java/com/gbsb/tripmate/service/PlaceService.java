package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.PlacePageResponse;
import com.gbsb.tripmate.dto.SearchPlaceWithKeywordResponse;
import com.gbsb.tripmate.entity.Place;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.PlaceRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Collections;
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

    public PlacePageResponse searchPlace(String placeName, int page, int size) {
        // 장소 검색 api
        getPlaceWithCategoryFromApi(placeName);

        Pageable pageable = PageRequest.of(page -1, size);
        Page<Place> placePage = placeRepository.findAllByPlaceNameContainingOrAddressNameContainingOrRoadAddressNameContaining(
                placeName, placeName, placeName, pageable
        );

        List<Place> places = placePage.getContent();
        int currentPage = placePage.getNumber() + 1;
        int totalPages = placePage.getTotalPages();
        long totalElements = placePage.getTotalElements();
        boolean hasNext = placePage.hasNext();

        return new PlacePageResponse(places, currentPage, totalPages, totalElements, hasNext);
    }

    private void getPlaceWithCategoryFromApi(String placeName) {
        String placeDataWithKeyword = "";
        SearchPlaceWithKeywordResponse searchPlaceWithKeywordResponse = new SearchPlaceWithKeywordResponse(Collections.emptyList(), 1, true);
        do {
            placeDataWithKeyword = getPlaceWithKeywordData(placeName, searchPlaceWithKeywordResponse.getPage() + 1);
            searchPlaceWithKeywordResponse = parsePlaceWithKeyword(placeDataWithKeyword, searchPlaceWithKeywordResponse.getPage() + 1);
        } while (!searchPlaceWithKeywordResponse.isEnd() && searchPlaceWithKeywordResponse.getPage() >= 1 && searchPlaceWithKeywordResponse.getPage() <= 45);
    }

    private SearchPlaceWithKeywordResponse parsePlaceWithKeyword(String jsonString, int page) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new MeetingException(FAIL_ENCODING);
        }

        JSONObject meta = (JSONObject) jsonObject.get("meta");
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
                }
            }
        } else {
            throw new MeetingException(ErrorCode.INVALID_ADDRESS);
        }

        return new SearchPlaceWithKeywordResponse(places, page, isEnd);
    }

    private String getPlaceWithKeywordData(String placeName, int page) {
        // 키워드로 장소 검색하기 api
        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
                    + URLEncoder.encode(placeName, "UTF-8")
                    + "&page=" + page
                    + "&size=" + 15;
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
