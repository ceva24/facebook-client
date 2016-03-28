package uk.co.ceva24.facebook.model;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public class FacebookClient
{
    private static final String APP_ID = "";
    private static final String APP_SECRET = "";

    private static final String PARAM_ACCESS_TOKEN = "access_token";

    private final WebResource graphBase;
    private String authToken;

    public FacebookClient() throws NoSuchAlgorithmException, 
            KeyManagementException
    {
        // Client and SSL configuration.
        final ClientConfig config = new DefaultClientConfig();
        final SSLContext ctx = SSLContext.getInstance("SSL");
        authToken = null;

        // Trust certificate issuers defined in the default implementation.
        ctx.init(null, null, null); 
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, 
                new HTTPSProperties(null, ctx));

        final Client client = Client.create(config);

        // Create base url resource.
        graphBase = client.resource("https://graph.facebook.com/");
    }

    public void authenticate()
    {
        WebResource oauth = graphBase.path("oauth/access_token");

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("client_id", APP_ID);
        params.add("client_secret", APP_SECRET);
        params.add("grant_type", "client_credentials");

        oauth = oauth.queryParams(params);
        WebResource.Builder oauthBuilder = 
                oauth.type(MediaType.APPLICATION_JSON_TYPE);

        // HTTP GET.
        ClientResponse response = oauthBuilder.get(ClientResponse.class);

        authToken = response.getEntity(String.class).split("=")[1];
    }

    public String getObjectProperties(String objectName)
    {
        // Create user timeline resource and encode the user name.
        final WebResource profile = graphBase.path(objectName);
        WebResource.Builder profileBuilder = 
                profile.type(MediaType.APPLICATION_JSON_TYPE);

        // HTTP GET.
        ClientResponse response = profileBuilder.get(ClientResponse.class);

        return response.getEntity(String.class);
    }

    public String getObjectLikes(String objectName)
    {
        // Create likes resource and encode the access token.
        WebResource likes = graphBase.path(objectName + "/likes");
        likes = likes.queryParam(PARAM_ACCESS_TOKEN, authToken);
        WebResource.Builder likesBuilder = 
                likes.type(MediaType.APPLICATION_JSON_TYPE);

        // HTTP GET.
        ClientResponse response = likesBuilder.get(ClientResponse.class);

        return response.getEntity(String.class);
    }

    public final boolean isAuthenticated()
    {
        return (authToken != null);
    }
}