package fr.cned.emdsgil.fr;

import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

import fr.cned.emdsgil.suividevosfrais.R;

public class HfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        setTitle("GSB : Frais HF");
        Global.updateDate((DatePicker) findViewById(R.id.datHf), true);
        EditText editText = ((EditText) findViewById(R.id.txtHf));
        editText.setText("0");
        imageClick();
        commandAddClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            backDefaultScreen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void imageClick() {
        super.findViewById(R.id.imgHfReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void commandAddClick() {
        super.findViewById(R.id.cmdHfAjouter).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                saveList();
                Serializer.serialize(Global.listFraisMois, HfActivity.this);
                backDefaultScreen();
            }
        });
    }

    private void saveList() {
        int years = ((DatePicker) findViewById(R.id.datHf)).getYear();
        int month = ((DatePicker) findViewById(R.id.datHf)).getMonth() + 1;
        int day = ((DatePicker) findViewById(R.id.datHf)).getDayOfMonth();
        float price = Float.parseFloat((((EditText) findViewById(R.id.txtHf)).getText().toString()));
        String motif = ((EditText) findViewById(R.id.txtHfMotif)).getText().toString();
        int key = years * 100 + month;
        if (!Global.listFraisMois.containsKey(key))
            Global.listFraisMois.put(key, new CostsMonth(years, month));
        Objects.requireNonNull(Global.listFraisMois.get(key)).addCosts(price, motif, day);
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(HfActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
