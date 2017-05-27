package br.gov.rj.riodasostras.sagas.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by alex on 26/03/17.
 */

public class UtilRede {
    public static int CONEXAO_WIFI = 1;
    public static int CONEXAO_MOBILE = 2;
    public static int SEM_CONEXAO = 0;

    public static int getStatusConexao(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return CONEXAO_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return CONEXAO_MOBILE;
        }
        return SEM_CONEXAO;
    }

    public static boolean isConectado(Context pContext) {
        if (getStatusConexao(pContext)==SEM_CONEXAO) return false;
        else return true;
    }
}