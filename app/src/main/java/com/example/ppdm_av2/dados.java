package com.example.ppdm_av2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class dados extends AppCompatActivity {
    Spinner spinner;
    TextView nome, castra, data_nascimento2, sexo;
    List<String> especie = new ArrayList<>();
    EditText raca, peso;
    SharedPreferences informacoesSalvas;

    private static final String TAG = "MinhaAtividade";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);
        mapeando();
        preencherSpinner();
        informacoesSalvas = getSharedPreferences("dadosUsuario", MODE_PRIVATE);
        iniciarDadosUsuario();
    }
    protected void mapeando (){
        spinner = findViewById(R.id.spinner);
        nome = findViewById(R.id.nome2);
        castra = findViewById(R.id.castra2);
        sexo = findViewById(R.id.sexo2);
        data_nascimento2 = findViewById(R.id.data_nascimento2);
        raca = findViewById(R.id.raca);
        peso = findViewById(R.id.peso);
        nome.setText(getIntent().getExtras().getString("valor1"));
        if("true".equals(getIntent().getExtras().getString("valor2")))
            sexo.setText("Sexo: F");
        else
            sexo.setText("Sexo: M");
        if("true".equals(getIntent().getExtras().getString("valor3")))
            castra.setText("Castrado: Sim");
        else
            castra.setText("Castrado: Não");
        data_nascimento2.setText(getIntent().getExtras().getString("valor4"));
    }

    private void iniciarDadosUsuario() {
        // verifica se o dado com o nome está disponível no arquivo
        // de preferências e preenche o componente, caso contrário
        // preenche o componente com um texto padrão
        if (informacoesSalvas.contains("raca")) {
            raca.setText(informacoesSalvas.getString("raca", "nome?"));
        }
        if(informacoesSalvas.contains("peso")){
            peso.setText(String.valueOf(informacoesSalvas.getFloat("peso", 0.0f)));
        }
        if(informacoesSalvas.contains("especie")){
            spinner.setSelection(informacoesSalvas.getInt("especie", 0));
        }
    }

    public void preencherSpinner (){
        especie.add("Escolha a Especie:");
        especie.add("Cachorro");
        especie.add("Gato");
        especie.add("Tartaruga");
        especie.add("Passaro");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especie);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void voltar(View view) {
        String especieraca;
        if(spinner.getSelectedItemId() == 0) {
            especieraca = "Não cadastrado";
        } else {
            especieraca = especie.get((int) (spinner.getSelectedItemId())) + " - " + raca.getText().toString();
        }
        String enviandopeso = "Peso: " + peso.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("resp1", especieraca);
        returnIntent.putExtra("resp2", enviandopeso);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
    protected void onStop() {
        // abre o arquivo de preferências para edição
        SharedPreferences.Editor prefUsuario = informacoesSalvas.edit();
        // determina os pares identificador o valor de cada dado do arquivo de preferências
        // para texto (String)
        prefUsuario.putString("raca", raca.getText().toString());
        prefUsuario.putInt("especie", (int)spinner.getSelectedItemId());
        // para o peso real (float)
        if(peso.getText().toString().isEmpty()){
            prefUsuario.putFloat("peso", 0.0f);
        } else {
            prefUsuario.putFloat("peso", Float.parseFloat(peso.getText().toString()));
        }
        // confirma a alteração
        prefUsuario.commit();
        // mensagem de aviso ao usuário
        Toast.makeText(getApplicationContext(),
                "Suas preferências foram salvas!\nAguarde alguns instantes para sair.",
                Toast.LENGTH_LONG).show();
        super.onStop();
    }
}