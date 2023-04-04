package com.example.ppdm_av2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText nome_animal, data_nascimento;
    CheckBox castra;
    RadioButton fem, mas;
    RadioGroup groupRadio;
    Intent intent;
    TextView pesalt, espalt;
    SharedPreferences informacoesSalvas;

    static final int ACTIVITY_REQUEST_SOMAR = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapeando();
        informacoesSalvas = getSharedPreferences("dadosUsuario", MODE_PRIVATE);
        iniciarDadosUsuario();
    }

    private void iniciarDadosUsuario() {
        // verifica se o dado com o nome está disponível no arquivo
        // de preferências e preenche o componente, caso contrário
        // preenche o componente com um texto padrão
        if (informacoesSalvas.contains("nome")) {
            nome_animal.setText(informacoesSalvas.getString("nome", "nome?"));
        }
        if (informacoesSalvas.contains("animalnascimento")) {
            data_nascimento.setText(informacoesSalvas.getString("animalnascimento", "animalnascimento?"));
        }
        // verifica se o dado com o sexo está disponível no arquivo
        // de preferências e seleciona o componente radioButton adequado,
        // com caso contrário seleciona o componente padrão (feminino)
        if (informacoesSalvas.contains("sexo")) {
            if (informacoesSalvas.getBoolean("sexo", true)) {
        // caso verdadeiro (true), seleciona o radioButton do sexo feminino
                fem.setChecked(true);
            } else {
        // caso false (falso), seleciona o radioButton do sexo masculino
                mas.setChecked(true);
            }
        } else {
            groupRadio.clearCheck();
        }
        if (informacoesSalvas.contains("castrad")) {
            if (informacoesSalvas.getInt("castrad", 0 )==1) {
        // caso verdadeiro (true), seleciona o radioButton do sexo feminino
                castra.setChecked(true);
            } else {
        // caso false (falso), seleciona o radioButton do sexo masculino
                castra.setChecked(false);
            }
        } else {
            castra.setChecked(false);
        }
    }
    protected void mapeando (){
        castra = findViewById(R.id.castrado);
        nome_animal = findViewById(R.id.emon);
        data_nascimento = findViewById(R.id.nascdata);
        groupRadio = findViewById(R.id.radioGroup);
        fem = findViewById(R.id.fem);
        mas = findViewById(R.id.mas);
        espalt = findViewById(R.id.altesp);
        pesalt = findViewById(R.id.altpes);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_SOMAR && resultCode == RESULT_OK) {
            String especieraca = data.getStringExtra("resp1");
            String enviandopeso = data.getStringExtra("resp2");

            espalt.setText(especieraca);
            pesalt.setText(enviandopeso);

            //Coloca o valor da soma no EditText
            //edtResp.setText(String.format("%.2f", soma));
        }
    }

    public void basicodado(View view) {
        String val1 = nome_animal.getText().toString(),
                val4 = data_nascimento.getText().toString();
        Boolean val2 = fem.isChecked();
        Boolean val3 = castra.isChecked();

        intent = new Intent(getApplicationContext(), dados.class);
        intent.putExtra("valor1", val1);
        intent.putExtra("valor2", String.valueOf(val2));
        intent.putExtra("valor3", String.valueOf(val3));
        intent.putExtra("valor4", val4);
        startActivityForResult(intent, ACTIVITY_REQUEST_SOMAR);
    }

    protected void onStop() {
        // abre o arquivo de preferências para edição
        SharedPreferences.Editor prefUsuario = informacoesSalvas.edit();
        // determina os pares identificador o valor de cada dado do arquivo de preferências
        // para texto (String)
        prefUsuario.putString("nome", nome_animal.getText().toString());
        prefUsuario.putString("animalnascimento", data_nascimento.getText().toString());
        // para booleano (boolen)
        prefUsuario.putBoolean("sexo", fem.isChecked());
        if(castra.isChecked()){
            prefUsuario.putInt("castrad", 1);
        } else {
            prefUsuario.putInt("castrad", 0);
        }
        // confirma a alteração
        prefUsuario.apply();
        // mensagem de aviso ao usuário
        Toast.makeText(getApplicationContext(),
                "Suas preferências foram salvas!\nAguarde alguns instantes para sair.",
                Toast.LENGTH_LONG).show();
        super.onStop();
    }

    public void finalizar(View view) {
        finish();
    }
}