package me.crystal.runescape;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.knockturnmc.api.util.RestClient;

/**
 * @author Sven Olderaan (admin@heaven-craft.net)
 */
public class GrandExchangeClient extends RestClient {

    private static final String URL_GE_DETAIL = "http://services.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item=";
    private static final int[] POWERS_OF_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    protected GrandExchangeClient() {
        super("application/json");
    }

    private static int parsePrice(String price) {
        String last = price.substring(price.length() - 1);

        //Get type of cash pile
        int multiplier;
        switch (last) {
            case "b":
                multiplier = 1000000000;
                break;
            case "m":
                multiplier = 1000000;
                break;
            case "k":
                multiplier = 1000;
                break;
            default:
                multiplier = 1;
                price = price.replace(",", "");
        }

        String priceDigits = (multiplier != 1) ? price.substring(0, price.length() - 1) : price;
        String priceParts[] = priceDigits.split("\\.");

        //Value before the .
        int mostSig = Integer.parseInt(priceParts[0]) * multiplier;
        //Value after the .
        int leastSig = 0;

        if (priceParts.length == 2) {
            leastSig = Integer.parseInt(priceParts[1]) * (multiplier / POWERS_OF_10[priceParts[1].length()]);
        }

        return mostSig + leastSig;
    }

    public int getItemPrice(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        JsonElement details = getDetails(item);
        String price = details.getAsJsonObject().get("item")
                .getAsJsonObject().get("current")
                .getAsJsonObject().get("price")
                .getAsString();
        return parsePrice(price);
    }

    private JsonElement getDetails(Item item) {
        try {
            String data = doGet(URL_GE_DETAIL + item.getId());
            return new JsonParser().parse(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
