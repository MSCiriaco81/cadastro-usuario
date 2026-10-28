package com.example.cadastrousuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppUsuarios.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABELA_USUARIOS = "usuarios";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_NOME = "nome";
    public static final String COLUNA_IDADE = "idade";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String criarTabela = "CREATE TABLE " + TABELA_USUARIOS + " (" +
                COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUNA_NOME + " TEXT NOT NULL, " +
                COLUNA_IDADE + " INTEGER NOT NULL);";
        db.execSQL(criarTabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIOS);
        onCreate(db);
    }

    // CREATE
    public boolean inserirUsuario(String nome, int idade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_NOME, nome);
        values.put(COLUNA_IDADE, idade);

        long resultado = db.insert(TABELA_USUARIOS, null, values);
        return resultado != -1;
    }

    // READ (por nome)
    public Cursor consultarUsuarioPorNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABELA_USUARIOS + " WHERE " + COLUNA_NOME + " = ?", new String[]{nome});
    }

    // UPDATE (atualiza idade baseando-se no nome)
    public boolean atualizarIdade(String nome, int novaIdade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUNA_IDADE, novaIdade);

        int linhasAfetadas = db.update(TABELA_USUARIOS, values, COLUNA_NOME + " = ?", new String[]{nome});
        return linhasAfetadas > 0;
    }

    // DELETE (deleta baseando-se no nome)
    public boolean deletarUsuario(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        int linhasAfetadas = db.delete(TABELA_USUARIOS, COLUNA_NOME + " = ?", new String[]{nome});
        return linhasAfetadas > 0;
    }
}