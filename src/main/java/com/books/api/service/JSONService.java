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
import java.util.List;

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
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

                String id = null;
                for (int j = 0; j < industryIdentifiers.length(); j++) {
                    JSONObject identifier = industryIdentifiers.getJSONObject(j);
                    id = identifier.getString("type").equals("ISBN_13") &&
                            !identifier.getString("identifier").isEmpty() ?
                            identifier.getString("identifier") : id;
                }
                id = id != null ? id : jsonObject.getString("id");
                JSONArray authorsArray = volumeInfo.optJSONArray("authors");
                JSONArray categoriesArray = volumeInfo.optJSONArray("categories");
                List<String> authors = new ArrayList<>();
                List<String> categories = new ArrayList<>();
                if (authorsArray != null) {
                    authorsArray.forEach(b -> authors.add(b.toString()));
                }
                if (categoriesArray != null) {
                    categoriesArray.forEach(c -> categories.add(c.toString().toLowerCase()));
                }
/*
                String authors = (authorsArray == null) ? null : authorsArray.toString();
                String categories = categoriesArray == null ? null : categoriesArray.toString();
*/
                BookDto bookDto = new BookDto(
                        id,
                        volumeInfo.optString("title"),
                        volumeInfo.optString("subtitle"),
                        volumeInfo.optString("publisher"),
                        volumeInfo.optLong("publishedDate"),
                        volumeInfo.optString("description"),
                        volumeInfo.optInt("pageCount"),
                        volumeInfo.optString("thumbnailUrl"),
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