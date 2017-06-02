package br.gov.rj.riodasostras.sagas.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.gov.rj.riodasostras.sagas.R;
import br.gov.rj.riodasostras.sagas.adapters.ListaBAsAdapter;
import br.gov.rj.riodasostras.sagas.interfaces.RViewOnClickListener;
import br.gov.rj.riodasostras.sagas.util.ServicoHTTP;

public class ListaBAsActivity extends AppCompatActivity implements RViewOnClickListener {
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipe;
    private JSONArray mJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bas);

        mRecycler = (RecyclerView)findViewById(R.id.rv_bas);
        mRecycler.setHasFixedSize( true );
        mRecycler.setLayoutManager( new LinearLayoutManager(this) );
        mRecycler.setItemAnimator( new DefaultItemAnimator());

        mSwipe = (SwipeRefreshLayout) findViewById(R.id.sr_bas);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // recarrega o recyclerView
                        new ListaBAsActivity.getWebservice().execute("http://200.222.101.117:85/sagas/data.php?nom_usuario=aflorindo", null);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipe.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });

        new ListaBAsActivity.getWebservice().execute("http://200.222.101.117:85/sagas/data.php?nom_usuario=aflorindo", null);
    }

    @Override
    public void onClickListener(View pView, int pPosition) {
        int[] infosBA = ( (ListaBAsAdapter)mRecycler.getAdapter() ).getInfosBASelecionado(pPosition);
        Intent intent = new Intent(ListaBAsActivity.this, InformacoesBaActivity.class);
        intent.putExtra("infosBA", infosBA);
        startActivity(intent);
    }

    private class getWebservice extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... parametros) {
            ServicoHTTP servicoHTTP = new ServicoHTTP();
            try {
                JSONObject json = servicoHTTP.getWebService(parametros[0], parametros[1]);
                mJSONArray = json.getJSONArray("body");
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
            ListaBAsAdapter adapter = new ListaBAsAdapter(ListaBAsActivity.this, mJSONArray);
            adapter.setClickListener(ListaBAsActivity.this);
            mRecycler.setAdapter(adapter);
        }
    }
}
