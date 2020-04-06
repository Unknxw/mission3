package fr.cned.emdsgil.fr;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.cned.emdsgil.suividevosfrais.R;

public class HfRecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf_recap);
        setTitle("GSB : Récap Frais HF");
        Global.updateDate((DatePicker) findViewById(R.id.datHfRecap), false);
        showList();
        imageClick();
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

    private void showList() {
        int years = ((DatePicker) findViewById(R.id.datHfRecap)).getYear();
        int month = ((DatePicker) findViewById(R.id.datHfRecap)).getMonth() + 1;
        Integer key = years * 100 + month;
        List<FraisHf> list = new ArrayList<>();
        if (Global.listFraisMois.containsKey(key))
            list = Objects.requireNonNull(Global.listFraisMois.get(key)).getCosts();
        ListView listView = findViewById(R.id.lstHfRecap);
        FraisHfAdapter adapter = new FraisHfAdapter(HfRecapActivity.this, list, key);
        listView.setAdapter(adapter);
    }

    private void imageClick() {
        findViewById(R.id.imgHfRecapReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                backDefaultScreen();
            }
        });
    }

    private void datClick() {
        DatePicker uneDate = (DatePicker) findViewById(R.id.datHfRecap);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showList();
            }
        });
    }

    private void backDefaultScreen() {
        Intent intent = new Intent(HfRecapActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
