package com.example.cadastrousuario;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText textNome, textIdade;
    private Button btnGravar, btnConsultar, btnAtualizar, btnDeletar;
    private TextView textResultado;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        textNome = findViewById(R.id.textNome);
        textIdade = findViewById(R.id.textIdade);
        btnGravar = findViewById(R.id.btnGravar);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);
        textResultado = findViewById(R.id.textResultado);

        // 1. GRAVAR / CADASTRAR
        btnGravar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();
            String idadeStr = textIdade.getText().toString().trim();

            if (nome.isEmpty() || idadeStr.isEmpty()) {
                Toast.makeText(this, "Preencha nome e idade!", Toast.LENGTH_SHORT).show();
                return;
            }

            int idade = Integer.parseInt(idadeStr);
            boolean sucesso = dbHelper.inserirUsuario(nome, idade);

            if (sucesso) {
                textResultado.setText("Usuário cadastrado com sucesso!");
                limparCampos();
            } else {
                textResultado.setText("Erro ao cadastrar usuário.");
            }
        });

        // 2. CONSULTAR
        btnConsultar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();

            if (nome.isEmpty()) {
                Toast.makeText(this, "Informe o nome para consultar!", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = dbHelper.consultarUsuarioPorNome(nome);
            if (cursor != null && cursor.moveToFirst()) {
                int idade = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUNA_IDADE));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUNA_ID));

                textIdade.setText(String.valueOf(idade));
                textResultado.setText("ID: " + id + "\nNome: " + nome + "\nIdade: " + idade + " anos");
                cursor.close();
            } else {
                textResultado.setText("Usuário '" + nome + "' não encontrado.");
            }
        });

        // 3. ATUALIZAR
        btnAtualizar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();
            String idadeStr = textIdade.getText().toString().trim();

            if (nome.isEmpty() || idadeStr.isEmpty()) {
                Toast.makeText(this, "Preencha o nome e a nova idade!", Toast.LENGTH_SHORT).show();
                return;
            }

            int novaIdade = Integer.parseInt(idadeStr);
            boolean atualizou = dbHelper.atualizarIdade(nome, novaIdade);

            if (atualizou) {
                textResultado.setText("Registro de " + nome + " atualizado para " + novaIdade + " anos.");
                limparCampos();
            } else {
                textResultado.setText("Falha ao atualizar: usuário não localizado.");
            }
        });

        // 4. DELETAR
        btnDeletar.setOnClickListener(v -> {
            String nome = textNome.getText().toString().trim();

            if (nome.isEmpty()) {
                Toast.makeText(this, "Informe o nome a ser excluído!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean deletou = dbHelper.deletarUsuario(nome);

            if (deletou) {
                textResultado.setText("Usuário " + nome + " removido com sucesso.");
                limparCampos();
            } else {
                textResultado.setText("Erro ao deletar: usuário não encontrado.");
            }
        });
    }

    private void limparCampos() {
        textNome.setText("");
        textIdade.setText("");
        textNome.requestFocus();
    }
}