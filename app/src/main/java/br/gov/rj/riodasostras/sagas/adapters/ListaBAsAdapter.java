package br.gov.rj.riodasostras.sagas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.gov.rj.riodasostras.sagas.R;
import br.gov.rj.riodasostras.sagas.util.Util;

/**
 * Created by alex.florindo on 26/05/2017.
 */

public class ListaBAsAdapter extends RecyclerView.Adapter<ListaBAsAdapter.ViewHolderListaBAs> {
    private final static String FORMATO_DATA = "yyyy-MM-dd HH:mm:ss";
    private JSONArray mJSONArray;
    private Context mContext;

    public ListaBAsAdapter(Context pContext, JSONArray pJSONArray) {
        this.mContext = pContext;
        this.mJSONArray = pJSONArray;
    }

    @Override
    public ViewHolderListaBAs onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("LOG", "onCreateViewHolder");
        Log.d("LOG", "Número BAs: "+ mJSONArray.length());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_bas, null, false);
        view.setLayoutParams( new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT) );
        ViewHolderListaBAs viewHolder = new ViewHolderListaBAs(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderListaBAs holder, int position) {
        Log.d("LOG", "onBindViewHolder - Item: "+ String.valueOf(position));
        try {
            Util util = new Util();
            JSONObject json = this.mJSONArray.getJSONObject(position);

            // Marca prazo BAs
            SimpleDateFormat fmt = new SimpleDateFormat(FORMATO_DATA);
            Date data = fmt.parse( json.getString("dt_encaminhamento")+ " " + json.getString("hr_encaminhamento") );
            String tempoDecorrido = util.getDiferencaDatas(data, new Date());
            if (tempoDecorrido.contains("d")) {
                int dias = Integer.parseInt( tempoDecorrido.replace("d","") );
                if (dias <= 2) holder.mLeftPanel.setBackgroundResource(R.color.prazo_1);
                else if (dias <= 7) holder.mLeftPanel.setBackgroundResource(R.color.prazo_2);
                else if (dias <= 15) holder.mLeftPanel.setBackgroundResource(R.color.prazo_3);
                else if (dias <= 30) holder.mLeftPanel.setBackgroundResource(R.color.prazo_4);
                else holder.mLeftPanel.setBackgroundResource(R.color.prazo_5);
            } else holder.mLeftPanel.setBackgroundResource(R.color.prazo_1);

            // Painel Esquerdo
            holder.mNumeroBA.setText( json.getString("nr_manut") +"/"+ json.getString("ano") );
            holder.mNomeDept.setText( json.getString("nm_depto") );
            holder.mLoginSolic.setText( "("+ json.getString("login_solici") +")" );
            holder.mNomeSolic.setText( json.getString("nome_solici") );
            holder.mDiasAndamento.setText( tempoDecorrido );

            // Painel Direito
            holder.mDescArea.setText( "Área Destino: "+ json.getString("desc_area") );
            holder.mEquipPadrao.setText( json.getString("nm_equip_padrao") );
            holder.mDescProblema.setText( json.getString("ds_proble") );

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() { return mJSONArray.length(); }

    public class ViewHolderListaBAs extends RecyclerView.ViewHolder {
        protected RelativeLayout mLeftPanel;
        protected TextView mNumeroBA;
        protected TextView mNomeDept;
        protected TextView mLoginSolic;
        protected TextView mNomeSolic;
        protected TextView mDiasAndamento;
        protected TextView mDescArea;
        protected TextView mEquipPadrao;
        protected TextView mDescProblema;

        public ViewHolderListaBAs(View itemView) {
            super(itemView);
            this.mLeftPanel = (RelativeLayout)itemView.findViewById(R.id.rl_left_panel);
            this.mNumeroBA = (TextView)itemView.findViewById(R.id.tv_numero_ba);
            this.mNomeDept = (TextView)itemView.findViewById(R.id.tv_nome_dpto);
            this.mLoginSolic = (TextView)itemView.findViewById(R.id.tv_login_solici);
            this.mNomeSolic = (TextView)itemView.findViewById(R.id.tv_nome_solici);
            this.mDiasAndamento = (TextView)itemView.findViewById(R.id.tv_dias_andamento);
            this.mDescArea = (TextView)itemView.findViewById(R.id.tv_desc_area);
            this.mEquipPadrao = (TextView)itemView.findViewById(R.id.tv_equip_padrao);
            this.mDescProblema = (TextView)itemView.findViewById(R.id.tv_desc_problema);
        }
    }
}
