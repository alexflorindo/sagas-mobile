package br.gov.rj.riodasostras.sagas.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by alex on 18/01/17.
 */

public class ServicoHTTP {

    public JSONArray retornarJSONArray (JSONTokener pTokener) throws JSONException {
        Object vObject = pTokener.nextValue();

        if (vObject instanceof JSONObject) {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = (JSONObject) vObject;
            if (jsonObject.length() > 0) jsonArray.put(jsonObject);
            return  jsonArray;
        } else if (vObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) vObject;
            return jsonArray;
        } else return null;
    }

    public JSONObject deleteWebService (String pHost, String pCredencial) throws IOException, JSONException {
        URL url = new URL(pHost);
        HttpURLConnection vConexao = (HttpURLConnection) url.openConnection();
        vConexao .setDoOutput(false);

        // Cabeçalho HTTP
        vConexao.setRequestMethod("DELETE");
        vConexao.setRequestProperty("Content-Type", "application/json");
        vConexao.setRequestProperty("Authorization", "Basic "+ pCredencial);
//        vConexao.setRequestProperty("Mobile-Token", FirebaseInstanceId.getInstance().getToken());

        // Leitura da resposta do WS
        InputStream in = vConexao.getInputStream();
        String response = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        in.close();

        // Leitura do cabeçalho do response
        JSONObject vCabecalho = new JSONObject();
        vCabecalho.put("status", vConexao.getHeaderField("status"));
        vCabecalho.put("message", vConexao.getHeaderField("message"));

        // Carrega JSON de retorno do WS
        JSONTokener vCorpo = new JSONTokener(response);

        // Prepara JSON final contendo cabeçalho e corpo
        JSONObject vJson = new JSONObject();
        vJson.put("header", vCabecalho);
        vJson.put("body", vCorpo.nextValue());

        vConexao.disconnect();
        return vJson;
    }

    public JSONObject getWebService (String pHost, String pCredencial) throws IOException, JSONException {
        URL url = new URL(pHost);
        HttpURLConnection vConexao = (HttpURLConnection) url.openConnection();
        vConexao .setDoOutput(false); // false para GET e true para POST

        // Cabeçalho HTTP
        vConexao.setRequestMethod("GET");
        vConexao.setRequestProperty("Content-Type", "application/json");
//        vConexao.setRequestProperty("Authorization", "Basic "+ pCredencial);
//        vConexao.setRequestProperty("Mobile-Token", FirebaseInstanceId.getInstance().getToken());

        // Leitura da resposta do WS
        InputStream in = vConexao.getInputStream();
        String response = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        in.close();

        // Leitura do cabeçalho do response
        JSONObject vCabecalho = new JSONObject();
        vCabecalho.put("status", vConexao.getHeaderField("status"));
        vCabecalho.put("message", vConexao.getHeaderField("message"));

        // Carrega JSON de retorno do WS
        JSONTokener vCorpo = new JSONTokener(response);

        // Prepara JSON final contendo cabeçalho e corpo
        JSONObject vJson = new JSONObject();
        vJson.put("header", vCabecalho);
        vJson.put("body", vCorpo.nextValue());

        vConexao.disconnect();
        return vJson;
    }

    public JSONObject postWebService(String pHost, String pCredencial, JSONObject pBody) throws IOException, JSONException {
        URL vUrl = new URL(pHost);
        HttpURLConnection vConexao = (HttpURLConnection)vUrl.openConnection();
        vConexao.setDoOutput(true);

        // Cabeçalho da requisicao HTTP
        vConexao.setRequestMethod("POST");
        vConexao.setRequestProperty("Content-Type", "application/json");
        vConexao.setRequestProperty("Authorization", "Basic " + pCredencial);
//        vConexao.setRequestProperty("Mobile-Token", FirebaseInstanceId.getInstance().getToken());

        // Conclusão do envio da mensagem
        OutputStream out = vConexao.getOutputStream();
        out.write(pBody.toString().getBytes("UTF-8"));
        out.close();

        // Leitura da resposta do servidor
        InputStream in = vConexao.getInputStream();
        String response = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        in.close();

        // Leitura do cabeçalho do response
        JSONObject vCabecalho = new JSONObject();
        vCabecalho.put("status", vConexao.getHeaderField("status"));
        vCabecalho.put("message", vConexao.getHeaderField("message"));

        // Carrega JSON de retorno do WS
        JSONObject vCorpo = new JSONObject();
        Object vObj = new JSONTokener(response).nextValue();
        if (vObj instanceof JSONObject) {
            vCorpo.put("body", new JSONObject(response));
        } else if (vObj instanceof  JSONArray) {
            vCorpo.put("body", new JSONArray(response));
        }

        // Prepara JSON final contendo cabeçalho e corpo
        JSONObject vJson = new JSONObject();
        vJson.put("header", vCabecalho);
        vJson.put("body", vCorpo);

        vConexao.disconnect();
        return vJson;
    }
}
