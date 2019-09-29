package matc89.exercicio3;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Button btnAdicionar;
    private Button btnRemover;
    private TextView editDescricao;
    private TextView editPrioridate;
    private ListView listView;

    private TarefaDB tarefaDB;
    private List<String> listaTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdicionar = (Button) findViewById(R.id.buttonAdicionar);
        btnRemover = (Button) findViewById(R.id.buttonRemover);
        editPrioridate = (TextView) findViewById(R.id.editPrioridade);
        editDescricao = (TextView) findViewById(R.id.editDescricao);
        listView = (ListView) findViewById(R.id.listView);

        tarefaDB = new TarefaDB(this);
        listaTarefas = new ArrayList<>();
        tarefaDB.deleteAll();
        loadTarefas();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listaTarefas.isEmpty()) {
                    loadTarefas();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Elemento deletado: " + listaTarefas.get(i), Toast.LENGTH_LONG)
                            .show();
                    tarefaDB.delete(listaTarefas.get(i));
                    loadTarefas();
                }
            }
        });

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int convertedPrioridate = Integer.valueOf(editPrioridate.getText().toString());

                if (convertedPrioridate > 10 || convertedPrioridate < 1){
                    Toast.makeText(getApplicationContext(), "A prioridade deve estar entre 1 e 10.", Toast.LENGTH_LONG).show();
                } else {
                    if (tarefaDB.select(editDescricao.getText().toString()) == false) {
                        tarefaDB.insertAtiv(editDescricao.getText().toString(), convertedPrioridate);
                        loadTarefas();
                    } else {
                        Toast.makeText(getApplicationContext(), "Tarefa já cadastrada.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaTarefas.isEmpty()){
                    loadTarefas();
                } else {
                    tarefaDB.delete(listaTarefas.get(0));
                    loadTarefas();
                }
            }
        });
    }

    private void loadTarefas() {
        Cursor cursor = tarefaDB.selectAll();
        Map<String, String> tarefaMap = new HashMap<String, String>();
        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        tarefaMap.clear();
        mapList.clear();
        listaTarefas.clear();
        listView.invalidateViews();

        if(cursor.getCount() > 0){
            btnRemover.setEnabled(true);
        } else {
            btnRemover.setEnabled(false);
        }

        if (cursor.moveToFirst()) {

            do {
                Tarefa tarefa = new Tarefa(cursor.getString(0), cursor.getInt(1));
                tarefaMap = new HashMap<String, String>();

                listaTarefas.add(cursor.getString(0));

                tarefaMap.put("descricao", cursor.getString(0));
                tarefaMap.put("prioridade", "Prioridade: "+ String.valueOf(cursor.getInt(1)));
                mapList.add(tarefaMap);

            } while (cursor.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(this, mapList, android.R.layout.simple_list_item_2,
                    new String[] {"descricao", "prioridade"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2,
                    });

            adapter.notifyDataSetChanged();
            listView.refreshDrawableState();
            listView.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "Descrição:" + listaTarefas, Toast.LENGTH_SHORT).show();
        }
    }

}


