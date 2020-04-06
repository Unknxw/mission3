package fr.cned.emdsgil.fr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.cned.emdsgil.suividevosfrais.R;

public class AuthActivity extends AppCompatActivity {

    private EditText name, password;
    private List<String> infoConnect = new ArrayList<>();
    private JSONArray lesFraisAenvoyer;
    private boolean logged = false;
    private static String api = "https://dev.groupez.xyz/api/api.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        setTitle("GSB : Transfert des données");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        lesFraisAenvoyer = new JSONArray();
        infoConnect = new ArrayList<String>();
        Button button = findViewById(R.id.btn_auth);
        name = findViewById(R.id.txtName);
        password = findViewById(R.id.txtPassword);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                infoConnect.add(0, name.getText().toString());
                infoConnect.add(1, password.getText().toString());
                JSONArray jsonArray = new JSONArray(infoConnect);
                logged = false;

                try {

                    String rep = post(api, jsonArray, "Auth");

                    Toast.makeText(AuthActivity.this, rep, Toast.LENGTH_LONG).show();

                    if (rep.contains("réussie")) {
                        Toast.makeText(AuthActivity.this, "Synchronisation en cours !", Toast.LENGTH_LONG).show();
                        logged = true;
                        Object lesFrais = Serializer.deSerialize(AuthActivity.this);
                        Map<Integer, CostsMonth> frais = (Map<Integer, CostsMonth>) lesFrais;
                        int index = 0;
                        for (Map.Entry<Integer, CostsMonth> entry : frais.entrySet()) {
                            Integer k = entry.getKey();
                            CostsMonth value = entry.getValue();
                            JSONObject add = new JSONObject();
                            try {
                                add.put("UserID", Global.userId);
                                add.put("date", k);
                                add.put("KM", value.getKm());
                                add.put("NUI", value.getOverNight());
                                add.put("REP", value.getRepas());
                                add.put("ETP", value.getState());

                                int nbFraisHF = value.getCosts().size();
                                for (int i = 0; i < nbFraisHF; i++) {
                                    JSONObject HF = new JSONObject();
                                    HF.put("Jour", value.getCosts().get(i).getDay().toString());
                                    HF.put("Motif", value.getCosts().get(i).getMotif());
                                    HF.put("Montant", value.getCosts().get(i).getPrice().toString());
                                    add.put("HF" + i, HF);
                                }

                            } catch (JSONException ignored) {

                            }

                            try {
                                lesFraisAenvoyer.put(index, add);
                                if (!(post(api, lesFraisAenvoyer, "Sync").contains("%error%"))) {
                                    System.out.println("Synchronisation effectuée");
                                }
                            } catch (JSONException ignored) {

                            }
                            index++;
                        }

                    } else
                        Toast.makeText(AuthActivity.this, "Utilisateur ou mot de passe incorrect", Toast.LENGTH_LONG).show();
                    if (logged) {
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
                        Date date = new Date();
                        CostsMonth costs = Global.listFraisMois.get(Integer.parseInt(dateFormat.format(date)));
                        Global.listFraisMois.clear();
                        Global.listFraisMois.put(Integer.parseInt(dateFormat.format(date)), costs);
                        Serializer.serialize(Global.listFraisMois, MainActivity.instance);
                        Toast.makeText(AuthActivity.this, "Synchronisation terminée !", Toast.LENGTH_LONG).show();
                    }
                    backDefaultScreen();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        });

    }

    /**
     *
     * @param url
     * @param jsonArray
     * @param type
     * @return
     * @throws IOException
     */
    public String post(String url, JSONArray jsonArray, String type) throws IOException {

        String response = "";
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        String data = null;

        try {

            URL urlObj = new URL(url);

            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            if (type.equals("Auth")) {
                data += "&" + URLEncoder.encode(type, "UTF-8") + "="
                        + URLEncoder.encode(jsonArray.toString(), "UTF-8");
            } else if (type.equalsIgnoreCase("Sync")) {
                data += "&" + URLEncoder.encode(type, "UTF-8") + "="
                        + URLEncoder.encode(jsonArray.toString(), "UTF-8");

            }

            assert data != null;
            wr.write(data);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");

            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
                conn.disconnect();
            } catch (Exception ignored) {
            }
        }
        System.out.println("La reponse : " + response);
        if (response.contains("réussie%")) {

            int start = response.indexOf('%');
            int end = response.lastIndexOf('%');
            String id = response.substring(start + 1, end);
            Global.userId = Integer.parseInt(id);
            response = "Authentification réussie !";
        } else {
            response = "Echec d'authentification !";
        }
        return response;

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

    private void backDefaultScreen() {
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }
}