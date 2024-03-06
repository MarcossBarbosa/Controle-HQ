package com.example.aplicativodehqs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    DBHelper DB;
    String idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        MaterialButton cadastrobtn = (MaterialButton) findViewById(R.id.cadastrobtn);
        DB = new DBHelper(this);

        cadastrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(MainActivity.this,"Por favor insire todos os campos", Toast.LENGTH_SHORT).show();

                else {
                    if (pass.equals(pass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){
                            Boolean insert = DB.insertData(user, pass);
                            if(insert==true){
                                Toast.makeText(MainActivity.this, "Registro feito com sucesso", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Registro falhou", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, "User ja existente, porfavor logue", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Senha nao coincidem", Toast.LENGTH_SHORT).show();
                    }
                } }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String idUsuario = "1";

                    if(user.equals("")||pass.equals(""))
                        Toast.makeText(MainActivity.this, "Por Favor digite seus dados", Toast.LENGTH_SHORT).show();

                    else{
                        Boolean checkuserpass = DB.checkusernamepassword(user, pass, idUsuario);
                        if(checkuserpass==true){
                            Toast.makeText(MainActivity.this, "Logado com sucesso",  Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), CadastroHQActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"Credenciais invalidas", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
    }
}