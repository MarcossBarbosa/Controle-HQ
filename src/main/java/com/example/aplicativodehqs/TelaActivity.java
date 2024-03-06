package com.example.aplicativodehqs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TelaActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ListView listViewColecao;
    private Button botao;
    private Button botaoHQ;
    private ArrayList<Integer> arrayIds;
    private Integer idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewColecao = findViewById(R.id.listViewColecao);
        botaoHQ = findViewById(R.id.btnCadastrarColecao);
        botaoHQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastroColecao();
            }
        });
        botao = findViewById(R.id.btnPesquisarHQ);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        });

        listViewColecao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirTelaHQs(idSelecionado);
            }
        });

        listViewColecao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirTelaAlterarColecao(idSelecionado);
                return true;
            }
        });

        criarBancoDados();
        listarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS colecao(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", nome VARCHAR" + ", sinopse VARCHAR" +
                    ", descricao VARCHAR" + ", data DATE" +
                    " )");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome FROM colecao", null);
            ArrayList<String> linhas = new ArrayList<>();
            ArrayAdapter<String> meuAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewColecao.setAdapter(meuAdapter);
            meuCursor.moveToFirst();
            while (meuCursor != null) {
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirTelaCadastro() {
        Intent intent = new Intent(this, CadastroHQActivity.class);
        startActivity(intent);
    }

    public void abrirTelaHQs(int idSelecionado) {
        Intent intent = new Intent(this, HQActivity.class);
        intent.putExtra("colecao_id", idSelecionado);
        startActivity(intent);
    }

    public void abrirTelaAlterarColecao(int colecaoId){
        Intent intent = new Intent(this, AlterarColecaoActivity.class);
        intent.putExtra("colecao_id", colecaoId);
        startActivity(intent);
    }

    public void abrirTelaCadastroColecao() {
        Intent intent = new Intent(this, CadastroColecaoActivity.class);
        startActivity(intent);
    }
}

