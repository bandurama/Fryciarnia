package pl.fryciarnia.backend;

import java.lang.reflect.Field;

public class JSON
{
    private String contents;

    public JSON ()
    {
        this.contents = "{";
    }

    public JSON add (String fieldName, String value, boolean isStringBased)
    {
        if (isStringBased)
            this.contents += String.format("\"%s\":\"%s\",", fieldName, value);
        else
            this.contents += String.format("\"%s\":%s,", fieldName, value);
        return this;
    }

    public String ret ()
    {
        return this.contents.substring(0, this.contents.length() - 1) + "}";
    }


    public static String convertObjectToJSON (Object o)
    {
        JSON json = new JSON();

        for (Field f : o.getClass().getDeclaredFields())
        {

        }

        return json.ret();
    }
}
