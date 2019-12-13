package br.com.msm.checkversion;

import android.content.Context;
import android.content.pm.PackageInfo;


import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.jsoup.Jsoup;

import java.nio.charset.Charset;

import static br.com.msm.checkversion.Util.isOnline;

public class CheckVersion {

    private Context ctx;
    private PackageInfo infor;

    private String error_desconhecido = "Desculpe, algo deu errado. Tente novamente mais tarde.";
    private String search_hint = "Buscar";
    private String error_JsonParseException = "Erro Json. Os dados retornado são inválidos.";
    private String error_TimeoutException = "Tempo limite excedido. Verifique sua conexão com a internet.";
    private String error_internet_sv = "Você não está conectado à Internet";
    private String Inf_atualizar_endereco = "Endereço pendente. Para atualizar os dados, o P1 da unidade deve acessar o Comando Web, selecionar a sua OPM em lotações e atualizar os campos pendentes.";


    public CheckVersion(Context ctx, PackageInfo infor) {
        this.ctx = ctx;
        this.infor = infor;
    }

    public void getVersionPlayStore(final CheckVersionApp check) {


        if (isOnline()) {
            Ion.with(ctx).load(ctx.getString(R.string.linkPlayStore, infor.packageName))
                    .setTimeout(10000) // 15 segundos
                    .asString(Charset.forName("utf-8")).setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {

                    if (e == null) {
                        try {
                            final String news = Jsoup.parse(result).select("div.PHBdkd:nth-child(2) > div.DWPxHb:nth-child(1)  > span:nth-child(1)")
                                    .first().ownText();// + " ok";

                            final String newVersionPlayStore = Jsoup.parse(result).select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                                    .first()
                                    .ownText();

                            final String dateUpdate = Jsoup.parse(result).select("div.hAyfc:nth-child(1) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                                    .first()
                                    .ownText();

                            new Tradutor(ctx, dateUpdate)
                                    .setCallback(new Translation() {
                                        @Override
                                        public void textTranslation(String text) {

                                            if (text != null) {
                                                check.newVersionApp(newVersionPlayStore, text, news, null);
                                            } else {
                                                check.newVersionApp(newVersionPlayStore, dateUpdate, news, null);
                                            }
                                        }
                                    });
                        } catch (Exception el) {
                            check.newVersionApp(null, null, null, el.getMessage());
                        }
                    } else {
                        String erro = null;

                        if (e.toString().contains("JsonParseException")) {
                            erro = error_JsonParseException;

                        } else if (e.toString().contains("TimeoutException")) {
                            erro = error_TimeoutException;

                        } else {
                            if (e.toString().length() > 2) {
                                erro = error_desconhecido;
                            }
                        }

                        check.newVersionApp(null, null, null, erro);
                    }

                }
            });


        } else {
            check.newVersionApp(null, null, null, "Sem conexão");
        }


    }
}
