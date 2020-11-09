package mapas.pratica03;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Menu extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String menu [] = new String[] {"Minha casa na cidade natal", "Minha casa em Viçosa",
        "Meu departamento", "Fechar aplicação"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_text, menu);
        setListAdapter(arrayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        Intent it = new Intent(getBaseContext(), Mapas.class);
        String aux = l.getItemAtPosition(position).toString();

        Bundle params = new Bundle();

        params.putInt("position", position);

        it.putExtras(params);

        switch (position){
            case 0:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_LONG).show();
                startActivity(it);
                break;
            case 1:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
                startActivity(it);
                break;
            case 2:
                Toast.makeText(getBaseContext(), aux, Toast.LENGTH_SHORT).show();
                startActivity(it);
                break;
            case 3:
                finish();
        }
    }
}
