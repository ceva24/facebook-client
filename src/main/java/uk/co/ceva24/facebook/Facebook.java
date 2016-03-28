package uk.co.ceva24.facebook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import uk.co.ceva24.facebook.model.FacebookClient;
import uk.co.ceva24.facebook.model.FacebookObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Facebook
{
    private static final String MEMBER_NAME = "name";
    private static final String MEMBER_ID = "id";
    private static final String MEMBER_DATA = "data";

    private FacebookClient client;
    private JsonParser parser;

    public Facebook() throws KeyManagementException, NoSuchAlgorithmException
    {
        try
        {
            client = new FacebookClient();
            parser = new JsonParser();
        }
        catch (KeyManagementException | NoSuchAlgorithmException e)
        {
            client = null;
            parser = null;

            throw e;
        }
    }

    public List<FacebookObject> getLikes(String id)
    {
        final List<FacebookObject> result = new ArrayList<>();

        // client.getObjectLikes requires authentication.
        if (!client.isAuthenticated())
            client.authenticate();

        // Perform the API request and parse the data.
        String data = client.getObjectLikes(id);
        JsonObject json = (JsonObject)parser.parse(data);
        JsonArray dataArray = json.getAsJsonArray(MEMBER_DATA);

        // Loop through each Facebook object and add its name to the list.
        for (JsonElement e : dataArray)
        {
            JsonObject o = e.getAsJsonObject();

            // Confirm that the members exist.
            String objectId = ((o.has(MEMBER_ID)) ? o.get(MEMBER_ID).getAsString() : "");
            String objectName = ((o.has(MEMBER_NAME)) ? o.get(MEMBER_NAME).getAsString() : "");

            result.add(new FacebookObject(objectId, objectName));
        }

        return result;
    }

    public FacebookObject[] getSixDegrees(String id)
    {
        final FacebookObject[] result = new FacebookObject[6];
        final Random randomGenerator = new Random();

        // Loop through up to 6 FacebookObjects, tracking their id for the next iteration.
        for (int i = 0; i < 6; i++)
        {
            List<FacebookObject> likes = this.getLikes(id);

            // Quit out if a dead end is reached.
            if (likes.isEmpty()) 
                break;

            // Get a random like, if there is more than one.
            FacebookObject degree = ((likes.size() == 1) ? likes.get(0) : likes.get(randomGenerator.nextInt(likes.size() - 1))); // Passing 0 to nextInt() is not allowed.

            id = degree.getId(); // Prepare for the next loop.
            result[i] = degree;
        }

        return result;
    }
}