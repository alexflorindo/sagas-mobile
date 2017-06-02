package br.gov.rj.riodasostras.sagas.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.rj.riodasostras.sagas.R;
import br.gov.rj.riodasostras.sagas.util.ServicoHTTP;

public class InformacoesBaActivity extends AppCompatActivity {
    private final static String FORMATO_DATA_DB = "yyyy-MM-dd HH:mm:ss";
    private final static String FORMATO_DATA = "dd/MM/yyyy - HH:mm:ss";
    private JSONObject mJSONObject;
    private int[] mInfosBA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_ba);

        mInfosBA = getIntent().getIntArrayExtra("infosBA");
        new InformacoesBaActivity.getWebservice()
                .execute("http://200.222.101.117:85/sagas/data.php?nr_manut="+ mInfosBA[0] +"&ano="+ mInfosBA[1], null);
    }

    private class getWebservice extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... parametros) {
            ServicoHTTP servicoHTTP = new ServicoHTTP();
            try {
                JSONObject json = servicoHTTP.getWebService(parametros[0], parametros[1]);
                mJSONObject = json.getJSONArray("body").getJSONObject(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            try {
                /* Preenche as informações do BA */
                TextView tvNumeroBA = (TextView) findViewById(R.id.tv_numero_ba);
                tvNumeroBA.setText( "BA: "+ mInfosBA[0] +"/"+ mInfosBA[1] );
                tvNumeroBA.setBackgroundResource(mInfosBA[2]);

                String status = mJSONObject.getString("status").equalsIgnoreCase("P") ? "Pendente" : "Fechado";
                TextView tvStatus = (TextView)findViewById(R.id.tv_status);
                tvStatus.setText( preparaLinha("Status: ", status) );

                TextView tvUserOper = (TextView)findViewById(R.id.tv_user_oper);
                tvUserOper.setText( preparaLinha("BA gerado por: ", mJSONObject.getString("cd_user_operador")) );

                SimpleDateFormat fmtDB = new SimpleDateFormat(FORMATO_DATA_DB);
                SimpleDateFormat fmt = new SimpleDateFormat(FORMATO_DATA);
                Date data = fmtDB.parse( mJSONObject.getString("dt_solici").replace(" - ", " ") );
                TextView tvDataSoli = (TextView)findViewById(R.id.tv_data_soli);
                tvDataSoli.setText( preparaLinha("Data/Hora: ", fmt.format(data)) );

                TextView tvNomeOrgao = (TextView)findViewById(R.id.tv_nome_orgao);
                tvNomeOrgao.setText( preparaLinha("Orgão/Depto: ", mJSONObject.getString("nm_orgao")) );
                if (mJSONObject.getString("nm_orgao").equalsIgnoreCase("")) tvNomeOrgao.setVisibility(View.GONE);

                TextView tvNome = (TextView)findViewById(R.id.tv_nome);
                tvNome.setText( preparaLinha("Solicitante: ", mJSONObject.getString("nome")) );
                if (mJSONObject.getString("nome").equalsIgnoreCase("")) tvNome.setVisibility(View.GONE);

                TextView tvTelUsuario = (TextView)findViewById(R.id.tv_tel_usuario);
                tvTelUsuario.setText( preparaLinha("Telefone e Local Usuário: ", mJSONObject.getString("tel_usuario")) );
                if (mJSONObject.getString("tel_usuario").equalsIgnoreCase("")) tvTelUsuario.setVisibility(View.GONE);

                TextView tvTipoServ = (TextView)findViewById(R.id.tv_tipo_serv);
                tvTipoServ.setText( preparaLinha("Tipo de Serviço: ", mJSONObject.getString("tp_servic")) );
                if (mJSONObject.getString("tp_servic").equalsIgnoreCase("")) tvTipoServ.setVisibility(View.GONE);

                TextView tvNomeTieq = (TextView)findViewById(R.id.tv_nome_tieq);
                tvNomeTieq.setText( preparaLinha("Tipo Equipamento / Serviço: ", mJSONObject.getString("nm_tipo_equip")) );
                if (mJSONObject.getString("nm_tipo_equip").equalsIgnoreCase("")) tvNomeTieq.setVisibility(View.GONE);

                TextView tvNomeEqau = (TextView)findViewById(R.id.tv_nome_eqau);
                tvNomeEqau.setText( preparaLinha("Equipamento a ser realizado serviço: ", mJSONObject.getString("nm_equip_aux")) );
                if (mJSONObject.getString("nm_equip_aux").equalsIgnoreCase("")) tvNomeEqau.setVisibility(View.GONE);

                TextView tvObservacao = (TextView)findViewById(R.id.tv_observacao);
                tvObservacao.setText( preparaLinha("Observação do Equipamento / Serviço: ", mJSONObject.getString("obs")) );
                if (mJSONObject.getString("obs").equalsIgnoreCase("")) tvObservacao.setVisibility(View.GONE);

                TextView tvPatrimPmro = (TextView)findViewById(R.id.tv_patrim_pmro);
                tvPatrimPmro.setText( preparaLinha("Nr. PMRO - Nr. SEMAD/COTINF: ", mJSONObject.getString("nr_patrim_pmro")) );
                if (mJSONObject.getString("nr_patrim_pmro").equalsIgnoreCase("")) tvPatrimPmro.setVisibility(View.GONE);

                TextView tvDescProblema = (TextView)findViewById(R.id.tv_desc_problema);
                tvDescProblema.setText( preparaLinha("Descrição Solicitação: ", mJSONObject.getString("ds_proble")) );
                if (mJSONObject.getString("ds_proble").equalsIgnoreCase("")) tvDescProblema.setVisibility(View.GONE);

                TextView tvDescObs = (TextView)findViewById(R.id.tv_desc_obs);
                tvDescObs.setText( preparaLinha("Observação do BA: ", mJSONObject.getString("ds_obs")) );
                if (mJSONObject.getString("ds_obs").equalsIgnoreCase("")) tvDescObs.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        @SuppressWarnings("deprecation")
        private Spanned preparaLinha(String pLabel, String pInfo) {
            if (Build.VERSION.SDK_INT > 24) {
                return Html.fromHtml("<b>"+ pLabel +"</b>" + pInfo, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml("<b>"+ pLabel +"</b>" + pInfo);
            }
        }
    }
}
