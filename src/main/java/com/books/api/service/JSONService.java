package com.books.api.service;

import com.books.api.domain.BookDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class JSONService {

    ObjectMapper objectMapper = new ObjectMapper();

    public List<BookDto> parseJson(String json) throws JSONException {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject object = new JSONObject(tokener);
        JSONArray booksJson = object.getJSONArray("items");

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<BookDto> books = new ArrayList<>();
        booksJson.forEach(book -> {
            try {
                JSONObject jsonObject = (JSONObject) book;
                JSONObject volumeInfo = jsonObject.getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

                String id = null;
                for (int i = 0; i < industryIdentifiers.length(); i++) {
                    JSONObject identifier = industryIdentifiers.getJSONObject(i);
                    id = identifier.getString("type").equals("ISBN_13") &&
                            !identifier.getString("identifier").isEmpty() ?
                            identifier.getString("identifier") : id;
                }
                id = id != null ? id : jsonObject.getString("id");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                Set<String> authors = new HashSet<>();
                Set<String> categories = new HashSet<>();
                if (authorsArray != null) {
                    authorsArray.forEach(b -> authors.add(b.toString()));
                }
                if (categoriesArray != null) {
                    categoriesArray.forEach(c -> categories.add(c.toString().toLowerCase()));
                }

                BookDto bookDto = new BookDto(
                        id,
                        volumeInfo.optString("title"),
                        volumeInfo.optString("subtitle"),
                        volumeInfo.optString("publisher"),
                        volumeInfo.optLong("publishedDate"),
                        volumeInfo.optString("description"),
                        volumeInfo.optInt("pageCount"),
                        imageLinks.optString("thumbnail"),
                        volumeInfo.optString("language"),
                        volumeInfo.optString("previewLink"),
                        volumeInfo.optDouble("averageRating"),
                        authors,
                        categories
                );

                books.add(bookDto);
            } catch (Exception e) {
                System.out.println("Could not parse JSON to Java object");
                e.printStackTrace();
            }
        });
        return books;
    }
}
