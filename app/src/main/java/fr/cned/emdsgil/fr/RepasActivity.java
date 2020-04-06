package fr.cned.emdsgil.fr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Objects;

import fr.cned.emdsgil.suividevosfrais.R;

public class RepasActivity extends AppCompatActivity {

    // informations affichées dans l'activité
    private Integer years;
    private Integer month;
    private Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        setTitle("GSB : Frais Repas");
        Global.updateDate((DatePicker) findViewById(R.id.datRepas), false);
        updateProprieties();
        imageClick();
        commandValidateClick();
        commandAddClick();
        commandLessClick();
        datClick();
    }

    private void imageClick() {
        findViewById(R.id.imgRepasReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void commandValidateClick() {
        findViewById(R.id.cmdRepasValider2).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, RepasActivity.this);
                backDefaultScreen();
            }
        });
    }

    private void commandAddClick() {
        findViewById(R.id.cmdRepasPlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity += 1;
                saveQuantity();
            }
        });
    }

    private void commandLessClick() {
        findViewById(R.id.cmdRepasMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity = Math.max(0, quantity - 1); // suppression de 1 si possible
                saveQuantity();
            }
        });
    }

    private void datClick() {
        final DatePicker uneDate = findViewById(R.id.datRepas);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                updateProprieties();
            }
        });
    }

    @SuppressLint("CutPasteId")
    private void updateProprieties() {
        years = ((DatePicker) findViewById(R.id.datRepas)).getYear();
        month = ((DatePicker) findViewById(R.id.datRepas)).getMonth() + 1;
        quantity = 0;
        int key = years * 100 + month;
        if (Global.listFraisMois.containsKey(key)) {
            quantity = Objects.requireNonNull(Global.listFraisMois.get(key)).getRepas();
        }
        ((EditText) findViewById(R.id.txtRepas)).setText(String.format(Locale.FRANCE, "%d", quantity));
    }

    private void saveQuantity() {
        EditText editText = findViewById(R.id.txtRepas);
        editText.setText(String.format(Locale.FRANCE, "%d", quantity));
        Integer key = years * 100 + month;
        if (!Global.listFraisMois.containsKey(key))
            Global.listFraisMois.put(key, new CostsMonth(years, month));
        Objects.requireNonNull(Global.listFraisMois.get(key)).setRepas(quantity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil)))
            backDefaultScreen();
        return super.onOptionsItemSelected(item);
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(RepasActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
