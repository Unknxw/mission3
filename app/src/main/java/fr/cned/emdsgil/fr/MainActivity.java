package fr.cned.emdsgil.fr;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import fr.cned.emdsgil.suividevosfrais.R;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        setTitle("GSB : Suivi des frais");
        loadData();
        commandMenuClick(((ImageButton) findViewById(R.id.cmdKm)), KmActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdHf)), HfActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdRepas)), RepasActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdNuitee)), NuitActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdEtape)), StateActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdHfRecap)), HfRecapActivity.class);
        commandMenuClick(((ImageButton) findViewById(R.id.cmdTransfert)), AuthActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    private void loadData() {
        Map<?, ?> monHash = (Map<?, ?>) Serializer.deSerialize(MainActivity.this);
        if (monHash != null) {
            Hashtable<Integer, CostsMonth> monHashCast = new Hashtable<>();
            for (Hashtable.Entry<?, ?> entry : monHash.entrySet())
                monHashCast.put((Integer) entry.getKey(), (CostsMonth) entry.getValue());
            Global.listFraisMois = monHashCast;
        }
        if (Global.listFraisMois == null)
            Global.listFraisMois = new HashMap<>();
    }

    private void commandMenuClick(ImageButton button, final Class classy) {
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, classy);
                startActivity(intent);
            }
        });
    }

}
