package fr.cned.emdsgil.fr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.R;

public class KmActivity extends AppCompatActivity {

    private Integer years;
    private Integer month;
    private Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);
        setTitle("GSB : Frais Km");
        Global.updateDate((DatePicker) findViewById(R.id.datKm), false);
        updateProprieties();
        imageClick();
        commandValidateClick();
        commandAddClick();
        commandLessClick();
        datClick();
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

    private void updateProprieties() {
        years = ((DatePicker) findViewById(R.id.datKm)).getYear();
        month = ((DatePicker) findViewById(R.id.datKm)).getMonth() + 1;
        quantity = 0;
        Integer key = years * 100 + month;
        if (Global.listFraisMois.containsKey(key)) {
            quantity = Global.listFraisMois.get(key).getKm();
        }
        ((EditText) findViewById(R.id.txtKm)).setText(String.format(Locale.FRANCE, "%d", quantity));
    }

    private void imageClick() {
        findViewById(R.id.imgKmReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void commandValidateClick() {
        findViewById(R.id.cmdKmValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, KmActivity.this);
                backDefaultScreen();
            }
        });
    }

    private void commandAddClick() {
        findViewById(R.id.cmdKmPlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity += 10;
                saveQuantity();
            }
        });
    }

    private void commandLessClick() {
        findViewById(R.id.cmdKmMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity = Math.max(0, quantity - 10); // suppression de 10 si possible
                saveQuantity();
            }
        });
    }

    private void datClick() {
        final DatePicker uneDate = (DatePicker) findViewById(R.id.datKm);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                updateProprieties();
            }
        });
    }

    private void saveQuantity() {
        EditText text = ((EditText) findViewById(R.id.txtKm));
        text.setText(String.format(Locale.FRANCE, "%d", quantity));
        Integer key = years * 100 + month;
        if (!Global.listFraisMois.containsKey(key))
            Global.listFraisMois.put(key, new CostsMonth(years, month));
        Global.listFraisMois.get(key).setKm(quantity);
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(KmActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
