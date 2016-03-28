package uk.co.ceva24.facebook;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import uk.co.ceva24.facebook.model.FacebookObject;

public class FacebookTests
{
    private static final String userName = "ceva24";
    private static final String pageName = "cocacola";

    private Facebook facebook;

    public FacebookTests() {}

    @Before
    public void setUp()
    {
        try
        {
            facebook = new Facebook();
        }
        catch (KeyManagementException | NoSuchAlgorithmException e)
        {
            System.err.println("Failed to create Facebook client. Cause: " + e.getMessage());
            System.exit(0);
        }
    }

    @Test
    public void testGetLikes()
    {
        List<FacebookObject> likes = facebook.getLikes(pageName);
        assertTrue("No data returned", likes.size() > 0);
    }

    @Test
    public void testGetSixDegrees()
    {
        FacebookObject[] degrees = facebook.getSixDegrees(pageName);

        assertEquals("Unexpected array length", 6, degrees.length);
        assertFalse("Control object " + pageName + " returned no likes", degrees[0] == null);
    }
}