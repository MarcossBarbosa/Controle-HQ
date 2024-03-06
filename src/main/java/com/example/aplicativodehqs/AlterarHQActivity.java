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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlterarHQActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    public Button buttonAlterar;
    public Button btnExcluir;
    public EditText edtNome;
    public EditText edtAno;
    public EditText edtEditora;
    public EditText edtGenero;
    public EditText edtNumero;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_hqactivity);

        buttonAlterar = (Button) findViewById(R.id.btnAlterar);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtAno = (EditText) findViewById(R.id.edtAno);
        edtAno.setFilters(new InputFilter[]{
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
        edtEditora = (EditText) findViewById(R.id.edtEditora);
        edtGenero = (EditText) findViewById(R.id.edtSino);
        edtNumero = (EditText) findViewById(R.id.edtNumero);
        edtNumero.setFilters(new InputFilter[]{
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
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        Intent intent = getIntent();
        id = intent.getIntExtra("hq_id", 0);

        carregarDados();

        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterar();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluir();
            }
        });
    }

    public void carregarDados() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT titulo, data, editora, genero, numero FROM HQ WHERE id = " + id.toString(), null);
            if (cursor.moveToFirst()) {
                edtNome.setText(cursor.getString(0));
                edtAno.setText(cursor.getString(1));
                edtEditora.setText(cursor.getString(2));
                edtGenero.setText(cursor.getString(3));
                edtNumero.setText(cursor.getString(4));
            } else {
                Toast.makeText(this, "HQ não encontrada.", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor.close();
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterar() {
        String valueNome = edtNome.getText().toString();
        String valueAno = edtAno.getText().toString();
        String valueEditora = edtEditora.getText().toString();
        String valueGenero = edtGenero.getText().toString();
        String valueNumero = edtNumero.getText().toString();
        if (TextUtils.isEmpty(valueNome) || TextUtils.isEmpty(valueAno) || TextUtils.isEmpty(valueEditora) ||
                TextUtils.isEmpty(valueGenero) || TextUtils.isEmpty(valueNumero)) {
            showToast("Não é possível alterar uma HQ sem preencher todos os campos");
            return;
        }

        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "UPDATE HQ SET titulo=?, data=?, editora=?, genero=?, numero=? WHERE id=?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindString(1, valueNome);
            stmt.bindString(2, valueAno);
            stmt.bindString(3, valueEditora);
            stmt.bindString(4, valueGenero);
            stmt.bindString(5, valueNumero);
            stmt.bindLong(6, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
            Toast.makeText(this, "HQ atualizada com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir() {
        try {
            bancoDados = openOrCreateDatabase("crudapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM HQ WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, id);
            stmt.executeUpdateDelete();
            bancoDados.close();
            Toast.makeText(this, "HQ excluída com sucesso.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showToast(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}

