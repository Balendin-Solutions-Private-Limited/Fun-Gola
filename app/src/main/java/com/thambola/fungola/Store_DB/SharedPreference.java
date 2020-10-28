package com.thambola.fungola.Store_DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {
    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FUNGOLA = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveGames(Context context, List<Store_GameResponse> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonGames = gson.toJson(favorites);

        editor.putString(FUNGOLA, jsonGames);

        editor.commit();
    }

    public void addFavorite(Context context, Store_GameResponse product) {
        List<Store_GameResponse> favorites = getGames(context);
        if (favorites == null)
            favorites = new ArrayList<Store_GameResponse>();
        favorites.add(product);
        saveGames(context, favorites);
    }

    public void removeGame(Context context, Store_GameResponse product) {
        ArrayList<Store_GameResponse> favorites = getGames(context);

        if (favorites != null) {
            for(int i=0;i<favorites.size();i++)
            {
                System.out.println("rrrrrrrrrrrrrrr "+favorites.get(i));
                System.out.println("rrrrrrrrrrrrrrr product "+product);
            }
            favorites.remove(product);

            System.out.println("rrrrrrrrrrrr removeproduct getgameresponse.size() " +favorites.size());
            saveGames(context, favorites);
        }
    }

    public void removeItempos(Context context, int product) {
        ArrayList<Store_GameResponse> favorites = getGames(context);

        if (favorites != null) {

            favorites.remove(product);
            saveGames(context, favorites);
        }
    }

    public ArrayList<Store_GameResponse> getGames(Context context) {
        SharedPreferences settings;
        List<Store_GameResponse> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FUNGOLA)) {
            String jsonGames = settings.getString(FUNGOLA, null);
            Gson gson = new Gson();
            Store_GameResponse[] favoriteItems = gson.fromJson(jsonGames,
                    Store_GameResponse[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Store_GameResponse>(favorites);
        } else
            return null;

        return (ArrayList<Store_GameResponse>) favorites;
    }
}
