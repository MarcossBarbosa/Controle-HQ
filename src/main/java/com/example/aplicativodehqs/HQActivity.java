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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class HQActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public ListView listViewDados;
    public Button botao;
    public Button btnPDF;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;
    public Integer idColec;
    private ArrayList<String> linhas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hqactivity);

        Intent intent = getIntent();
        idColec = intent.getIntExtra("colecao_id", 0);

        listViewDados = (ListView) findViewById(R.id.listViewDadosHQ);
        btnPDF = (Button) findViewById(R.id.btnPDF);
        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gerarPDF(linhas);
            }
        });
        botao = (Button) findViewById(R.id.btnPesquisarHQ);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaPesquisarHQ();
            }
        });

        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                idSelecionado = arrayIds.get(i);
                abrirTelaAlterar();
                return true;
            }
        });

        criarBancoDados1();
        listarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listarDados();
    }

    public void criarBancoDados1() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            bancoDados.execSQL("PRAGMA foreign_keys=ON;");
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS HQ(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", titulo VARCHAR " +
                    ", data DATE " +
                    ", editora VARCHAR " +
                    ", genero VARCHAR " +
                    ", numero INTEGER" +
                    ", idColecao INTEGER," +
                    "FOREIGN KEY (idColecao) REFERENCES colecao(id))");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDados() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, titulo, numero FROM HQ WHERE idColecao = "+idColec,  null);
            linhas = new ArrayList<String>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();
            while (!meuCursor.isAfterLast()) {
                String nome = (meuCursor.getString(1));
                String numero = meuCursor.getString(2);
                linhas.add(nome + " - " + numero);
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gerarPDF(ArrayList<String> dados) {
        Document document = new Document();
        try {
            String filename = "hq.pdf";
            File file = new File(getExternalFilesDir(null), filename);
            String filePath = file.getAbsolutePath();

            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            for (String linha : dados) {
                document.add(new Paragraph(linha));

            }
            document.close();

            Toast.makeText(this, "PDF gerado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao gerar PDF :(", Toast.LENGTH_SHORT).show();
        }
    }


    public void abrirTelaAlterar() {
        Intent intent = new Intent(this, AlterarHQActivity.class);
        intent.putExtra("hq_id", idSelecionado);
        startActivity(intent);
    }

    public void abrirTelaPesquisarHQ(){
        Intent intent = new Intent(this, PesquisarHQ.class);
        startActivity(intent);
    }


}

