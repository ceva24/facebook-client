package uk.co.ceva24.facebook;

import uk.co.ceva24.facebook.model.FacebookObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Main
{
    private static Facebook facebook;

    public static void main(String[] args)
    {
        try
        {
            facebook = new Facebook();

            Main.runNameQuery();
            Main.runLikesQuery();
            Main.runDegreesQuery();
        }
        catch (KeyManagementException | NoSuchAlgorithmException e)
        {
            System.err.println("Failed to create Facebook client. Cause: " + e.getMessage());
        }
    }

    private static void runNameQuery()
    {
        System.out.println(facebook.getNameForId("ceva24"));
    }

    private static void runLikesQuery()
    {
        for (FacebookObject like : facebook.getLikes("cocacola"))
            System.out.println(like);
    }

    private static void runDegreesQuery()
    {
        FacebookObject[] degrees = facebook.getSixDegrees("cocacola");

        for (int i = 0; i < 6; i++)
        {
            FacebookObject o = degrees[i]; 

            if (o == null)
            {
                String suffix;
                switch (i)
                {
                    case 1  : suffix = "st"; break;
                    case 2  : suffix = "nd"; break;
                    case 3  : suffix = "rd"; break;
                    default : suffix = "th"; break;
                }

                System.out.println("You only reached the " + (i) + suffix + " degree! Better luck next time!");
                break;
            }
            else
            {
                System.out.println(o);
            }
        }
    }
}