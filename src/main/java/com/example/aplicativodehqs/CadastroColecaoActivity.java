package com.example.aplicativodehqs;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class CadastroColecaoActivity extends AppCompatActivity {
    EditText edtNome;
    EditText edtDesc;
    EditText edtSino;
    EditText edtData;
    Button btnSalvar;
    SQLiteDatabase bancoDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_colecao);

        edtNome = (EditText) findViewById(R.id.edtNome);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        edtSino = (EditText) findViewById(R.id.edtSinopse);
        edtData = (EditText) findViewById(R.id.edtData);
        edtData.setFilters(new InputFilter[] {
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
        btnSalvar = (Button) findViewById(R.id.btnAlterar);


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public void cadastrar() {
        String nome = edtNome.getText().toString();
        String data = edtData.getText().toString();
        String descricao = edtDesc.getText().toString();
        String sinopse = edtSino.getText().toString();
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(data) || TextUtils.isEmpty(descricao) ||
                TextUtils.isEmpty(sinopse)) {
            Toast.makeText(this, "Não é possível cadastrar uma Colecao sem preencher todos os campos", Toast.LENGTH_SHORT).show();
            return;
        } {
            try {
                bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO colecao (nome, sinopse, descricao, data) VALUES (?,?,?,?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, edtNome.getText().toString());
                stmt.bindString(2, edtSino.getText().toString());
                stmt.bindString(3, edtDesc.getText().toString());
                stmt.bindString(4, edtData.getText().toString());
                stmt.executeInsert();
                bancoDados.close();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}



