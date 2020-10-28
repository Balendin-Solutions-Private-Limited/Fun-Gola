package com.thambola.fungola.Store_DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference_TapedClaim {
    public static String PREFS_NAME = "PRODUCT_APP_TapedClaim";
    public static String FUNGOLA = "Product_Tapedclaim";

    public SharedPreference_TapedClaim() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveTapedCoins(Context context, List<TicketTapedClaim> favorites, String id) {

        FUNGOLA="Product_Tapedclaim"+id;
        PREFS_NAME = "PRODUCT_APP_TapedClaim"+id;

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

    public void addTapedCoins(Context context, TicketTapedClaim product, String id) {

        FUNGOLA="Product_Tapedclaim"+id;
        PREFS_NAME = "PRODUCT_APP_TapedClaim"+id;
        List<TicketTapedClaim> favorites = getTapedClaim(context,id);
        if (favorites == null)
            favorites = new ArrayList<TicketTapedClaim>();
        favorites.add(product);
        saveTapedCoins(context, favorites,id);
    }

    public void removeGame(Context context, TicketTapedClaim product, String id) {
        ArrayList<TicketTapedClaim> favorites = getTapedClaim(context,id);

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
        ArrayList<TicketTapedClaim> favorites = getTapedClaim(context,id);

        if (favorites != null) {

            favorites.remove(product);
            saveTapedCoins(context, favorites, id);
        }
    }

    public void removeTapedGame(Context context, ArrayList<TicketTapedClaim> favorites, String id) {
       // ArrayList<TicketTapedClaim> favorites = getTapedClaim(context,id);

        if (favorites != null) {

            favorites.clear();
            System.out.println("rrrrrrrrr  TicketTapedClaim size"+favorites.size());
           // favorites.remove(product);
            saveTapedCoins(context, favorites, id);
        }
    }

    public ArrayList<TicketTapedClaim> getTapedClaim(Context context, String id) {

        FUNGOLA="Product_Tapedclaim"+id;
        PREFS_NAME = "PRODUCT_APP_TapedClaim"+id;
        SharedPreferences settings;
        List<TicketTapedClaim> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FUNGOLA)) {
            String jsonGames = settings.getString(FUNGOLA, null);
            Gson gson = new Gson();
            TicketTapedClaim[] favoriteItems = gson.fromJson(jsonGames,
                    TicketTapedClaim[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<TicketTapedClaim>(favorites);
        } else
            return null;

        return (ArrayList<TicketTapedClaim>) favorites;
    }
}
