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

public class StateActivity extends AppCompatActivity {

    private Integer years;
    private Integer month;
    private Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);
        setTitle("GSB : Frais Etapes");
        Global.updateDate((DatePicker) findViewById(R.id.datEtape), false);
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
        years = ((DatePicker) findViewById(R.id.datEtape)).getYear();
        month = ((DatePicker) findViewById(R.id.datEtape)).getMonth() + 1;
        quantity = 0;
        Integer key = years * 100 + month;
        if (Global.listFraisMois.containsKey(key)) {
            quantity = Objects.requireNonNull(Global.listFraisMois.get(key)).getState();
        }
        ((EditText) findViewById(R.id.txtEtape)).setText(String.format(Locale.FRANCE, "%d", quantity));
    }

    private void imageClick() {
        findViewById(R.id.imgEtapeReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void commandValidateClick() {
        findViewById(R.id.cmdEtapeValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, StateActivity.this);
                backDefaultScreen();
            }
        });
    }

    private void commandAddClick() {
        findViewById(R.id.cmdEtapePlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity += 1;
                saveQuantity();
            }
        });
    }

    private void commandLessClick() {
        findViewById(R.id.cmdEtapeMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                quantity = Math.max(0, quantity - 1);
                saveQuantity();
            }
        });
    }

    private void datClick() {
        DatePicker uneDate = findViewById(R.id.datEtape);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                updateProprieties();
            }
        });
    }

    private void saveQuantity() {
        EditText editText = findViewById(R.id.txtEtape);
        editText.setText(String.format(Locale.FRANCE, "%d", quantity));
        Integer key = years * 100 + month;
        if (!Global.listFraisMois.containsKey(key)) {
            Global.listFraisMois.put(key, new CostsMonth(years, month));
        }
        CostsMonth costsMonth = Global.listFraisMois.get(key);
        if (costsMonth != null)
            costsMonth.setState(quantity);
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(StateActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
