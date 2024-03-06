package com.example.aplicativodehqs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CadastroHQActivity extends AppCompatActivity {
    EditText edtNome;
    EditText edtAno;
    EditText edtEditora;
    EditText edtGenero;
    EditText edtNumero;
    Button botao;
    Button btn;
    Spinner spinnerColecao;
    SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_hqactivity);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtAno = (EditText) findViewById(R.id.edtAno);
        edtAno.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            char currentChar = source.charAt(i);
                            if (!Character.isDigit(currentChar) && currentChar != '/') {
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        edtEditora = (EditText) findViewById(R.id.edtEditora);
        edtGenero = (EditText) findViewById(R.id.edtSino);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtNumero.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {

                        for (int i = start; i < end; i++) {
                            if (!Character.isDigit(source.charAt(i))) {
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        btn = (Button) findViewById(R.id.btnVoltar);
        botao = (Button) findViewById(R.id.btnAlterar);
        spinnerColecao = (Spinner) findViewById(R.id.spinnerColecao);


        ArrayAdapter<String> colecaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaDeColecoes());
        colecaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColecao.setAdapter(colecaoAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTela();
            }
        });
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar() {
        String nome = edtNome.getText().toString();
        String ano = edtAno.getText().toString();
        String editora = edtEditora.getText().toString();
        String genero = edtGenero.getText().toString();
        String numero = edtNumero.getText().toString();
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(ano) || TextUtils.isEmpty(editora) ||
                TextUtils.isEmpty(genero) || TextUtils.isEmpty(numero)) {
            Toast.makeText(this, "Não é possível cadastrar uma HQ sem preencher todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
         try {
             bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO HQ (titulo, data, editora, genero, numero, idColecao) VALUES (?, ?, ?, ?, ?, ?)";
                    SQLiteStatement stmt = bancoDados.compileStatement(sql);
                    stmt.bindString(1, edtNome.getText().toString());
                    stmt.bindString(2, edtAno.getText().toString());
                    stmt.bindString(3, edtEditora.getText().toString());
                    stmt.bindString(4, edtGenero.getText().toString());
                    stmt.bindString(5, edtNumero.getText().toString());
                    int colecaoId = spinnerColecao.getSelectedItemPosition() + 1;
                    stmt.bindLong(6, colecaoId);
                    stmt.executeInsert();
                    bancoDados.close();
                    finish();
                    abrirTela();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



    private ArrayList<String> listaDeColecoes() {
        ArrayList<String> colecoes = new ArrayList<>();
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT nome FROM colecao", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                colecoes.add(cursor.getString(0));
                cursor.moveToNext();
            }
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colecoes;
    }
    public void abrirTela() {
        Intent intent = new Intent(this, TelaActivity.class);
        startActivity(intent);
    }
}

