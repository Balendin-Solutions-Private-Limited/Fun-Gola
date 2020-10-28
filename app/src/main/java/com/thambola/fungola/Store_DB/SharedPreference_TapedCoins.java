package com.thambola.fungola.Store_DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference_TapedCoins {
    public static String PREFS_NAME = "PRODUCT_APP_TapedCoins";
    public static String FUNGOLA = "Product_TapedCoins";

    public SharedPreference_TapedCoins() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveTapedCoins(Context context, List<TicketTapedNumbers> favorites, String id) {

        FUNGOLA="Product_TapedCoins"+id;
        PREFS_NAME = "PRODUCT_APP_TapedCoins"+id;

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

    public void addTapedCoins(Context context, TicketTapedNumbers product, String id) {

        FUNGOLA="Product_TapedCoins"+id;
        PREFS_NAME = "PRODUCT_APP_TapedCoins"+id;
        List<TicketTapedNumbers> favorites = getTapedCoins(context,id);
        if (favorites == null)
            favorites = new ArrayList<TicketTapedNumbers>();
        favorites.add(product);
        saveTapedCoins(context, favorites,id);
    }

    public void removeGame(Context context, TicketTapedNumbers product, String id) {
        ArrayList<TicketTapedNumbers> favorites = getTapedCoins(context,id);

        if (favorites != null) {
            for(int i=0;i<favorites.size();i++)
            {
                System.out.println("rrrrrrrrrrrrrrr "+favorites.get(i));
                System.out.println("rrrrrrrrrrrrrrr product "+product);
            }
            favorites.remove(product);

            System.out.println("rrrrrrrrrrrr removeproduct getgameresponse.size() " +favorites.size());
            saveTapedCoins(context, favorites, id);
        }
    }

    public void removeItempos(Context context, int product, String id) {
        ArrayList<TicketTapedNumbers> favorites = getTapedCoins(context,id);

        if (favorites != null) {

            favorites.remove(product);
            saveTapedCoins(context, favorites, id);
        }
    }

    public void removeTapedGame(Context context, ArrayList<TicketTapedNumbers> favorites, String id) {
       // ArrayList<TicketTapedNumbers> favorites = getTapedCoins(context,id);

        if (favorites != null) {

            favorites.clear();
            System.out.println("rrrrrrrrr TicketTapedNumbers size "+favorites.size());
           // favorites.remove(product);
            saveTapedCoins(context, favorites, id);
        }
    }

    public ArrayList<TicketTapedNumbers> getTapedCoins(Context context, String id) {

        FUNGOLA="Product_TapedCoins"+id;
        PREFS_NAME = "PRODUCT_APP_TapedCoins"+id;
        SharedPreferences settings;
        List<TicketTapedNumbers> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FUNGOLA)) {
            String jsonGames = settings.getString(FUNGOLA, null);
            Gson gson = new Gson();
            TicketTapedNumbers[] favoriteItems = gson.fromJson(jsonGames,
                    TicketTapedNumbers[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<TicketTapedNumbers>(favorites);
        } else
            return null;

        return (ArrayList<TicketTapedNumbers>) favorites;
    }
}
