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

public class NuitActivity extends AppCompatActivity {

    private Integer years;
    private Integer month;
    private Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuit);
        setTitle("GSB : Frais Nuités");
        Global.updateDate((DatePicker) findViewById(R.id.datNuit), false);
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

    @SuppressLint("CutPasteId")
    private void updateProprieties() {
        years = ((DatePicker) findViewById(R.id.datNuit)).getYear();
        month = ((DatePicker) findViewById(R.id.datNuit)).getMonth() + 1;
        quantity = 0;
        Integer key = years * 100 + month;
        if (Global.listFraisMois.containsKey(key)) {
            quantity = Objects.requireNonNull(Global.listFraisMois.get(key)).getOverNight();
        }
        ((EditText) findViewById(R.id.txtNuit)).setText(String.format(Locale.FRANCE, "%d", quantity));
    }

    private void imageClick() {
        findViewById(R.id.imgNuitReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void commandValidateClick() {
        findViewById(R.id.cmdNuitValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, NuitActivity.this);
                backDefaultScreen();
            }
        });
    }

    private void commandAddClick() {
        findViewById(R.id.cmdNuitPlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity += 1;
                saveQuantity();
            }
        });
    }

    private void commandLessClick() {
        findViewById(R.id.cmdNuitMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity = Math.max(0, quantity - 1); // suppression de 1 si possible
                saveQuantity();
            }
        });
    }

    private void datClick() {
        final DatePicker uneDate = findViewById(R.id.datNuit);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                updateProprieties();
            }
        });
    }

    private void saveQuantity() {
        EditText editText = findViewById(R.id.txtNuit);
        editText.setText(String.format(Locale.FRANCE, "%d", quantity));
        Integer key = years * 100 + month;
        if (!Global.listFraisMois.containsKey(key))
            Global.listFraisMois.put(key, new CostsMonth(years, month));
        Objects.requireNonNull(Global.listFraisMois.get(key)).setOverNight(quantity);
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(NuitActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
