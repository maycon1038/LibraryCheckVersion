package br.com.msm.checkversion;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;



public class Tradutor {

    Context ctx;
    String texto;

    public Tradutor(Context ctx, String texto) {
        this.ctx = ctx;
        this.texto = texto;
    }

    public void setCallback(final Translation callback) {
        Locale ling = Locale.getDefault();
        String idioma = ling.getLanguage();
        String query = null;
        try {
            query = URLEncoder.encode(texto, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://api.mymemory.translated.net/get?q=" + query + "&langpair=en|" + idioma;

        Ion.with(ctx)
                .load(url)
                .setTimeout(10000) // 10 segundos
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {

                    try {
                        callback.textTranslation(result.get("responseData").getAsJsonObject().get("translatedText").getAsString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        callback.textTranslation(null);
                    }
                } else {

                    callback.textTranslation(null);

                }

            }
        });

    }

}
