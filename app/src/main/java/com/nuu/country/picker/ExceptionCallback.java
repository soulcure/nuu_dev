package com.nuu.country.picker;

        import org.json.JSONException;

        import java.io.IOException;

/**
 * Created by android on 17/10/17.
 */

public abstract class ExceptionCallback {
    abstract void onIOException(IOException e);

    abstract void onJSONException(JSONException e);
}
