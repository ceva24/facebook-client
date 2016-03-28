package uk.co.ceva24.facebook.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class FacebookClientTests
{
    private static final String userName = "ceva24";
    private static final String pageName = "cocacola";

    private FacebookClient client;
    private JsonParser parser;

    public FacebookClientTests() {}

    @Before
    public void setUp()
    {
        try
        {
            client = new FacebookClient();
        }
        catch (NoSuchAlgorithmException | KeyManagementException e)
        {
            fail("Failed to create FacebookClient: " + e.getMessage());
        }

        parser = new JsonParser();
    }

    @Test
    public void testAuthenticate()
    {
        client.authenticate();

        assertTrue("Failed to authenticate", client.isAuthenticated());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetObjectLikesUnauthenticated()
    {
        client.getObjectLikes(pageName);
    }

    @Test
    public void testGetObjectLikes()
    {
        client.authenticate();

        assertTrue("Failed to authenticate", client.isAuthenticated());

        String result = client.getObjectLikes(pageName);
        JsonObject jsonResult = (JsonObject)parser.parse(result);
        JsonArray pageData = jsonResult.get("data").getAsJsonArray();

        assertTrue("No data returned", pageData.size() > 0);
    }
}