package apps.lustven.fuelcalculator.fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.lustven.fuelcalculator.MainActivity;
import apps.lustven.fuelcalculator.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentEfficency#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentEfficency extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    InterstitialAd interstitialAD;

    private ImageView settingsEficiencia,settingsDistancia,settingsPrecio;
    private EditText editTextEficiencia;
    private TextView textViewEficiencia;
    private EditText editTextDistancia;
    private EditText editTextPrecioLitro;
    private EditText editPersona;
    private TextView textViewDistancia;
    private TextView textViewPrecio;
    private TextView calculoLitroPor100km,calculoLitroPor10km,calculoLitroPorKm,calculoMillasPorGalonUS,calculoMillasPorGalonUK,calculoCostoCombustible,calculoCostoCombustiblePorPersona;
    private Button calcular;
    private String guardarDistancia,guardarPrecioCombustible,guardarCombustibleConsumido,guardarNumeroPersonas,guardarTxtEficienciaCombustible,guardarTxtDistancia,guardarTxtPrecioCombustible;
    private String guardarLitros100km,guardarLitros10km,guardarLitroskm,guardarMillasGalonUS,guardarMillasGalonUK,guardarCostoCombustible,guardarCostoCombustiblePersona;
    public fragmentEfficency() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentEfficency.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentEfficency newInstance(String param1, String param2) {
        fragmentEfficency fragment = new fragmentEfficency();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_efficency, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadInterstitialAd();
        SharedPreferences preferences = getContext().getSharedPreferences("datos", Context.MODE_PRIVATE);

        guardarCombustibleConsumido = preferences.getString("eficienciaCombustibleE","");
        guardarDistancia = preferences.getString("distanciaE","");
        guardarPrecioCombustible = preferences.getString("precioCombustibleE","");
        guardarNumeroPersonas = preferences.getString("numeroPersonasE","");
        guardarTxtEficienciaCombustible = preferences.getString("txtEficienciaCombustibleE",getString(R.string.popupLitros));
        guardarTxtDistancia = preferences.getString("txtDistanciaE",getString(R.string.popupKilometros));
        guardarTxtPrecioCombustible = preferences.getString("txtPrecioCombustibleE",getString(R.string.popupPorLitro));

        guardarLitros100km = preferences.getString("litros100km",getString(R.string.litros_por_cada_100_km));
        guardarLitros10km = preferences.getString("litros10km",getString(R.string.litros_por_cada_10_km));
        guardarLitroskm = preferences.getString("litroskm",getString(R.string.litros_por_cada_km));
        guardarMillasGalonUS = preferences.getString("millasGalonUS",getString(R.string.millas_por_gal_n_us));
        guardarMillasGalonUK = preferences.getString("millasGalonUK",getString(R.string.millas_por_gal_n_uk));
        guardarCostoCombustible = preferences.getString("costoCombustibleE",getString(R.string.costo_del_combustibleMin));
        guardarCostoCombustiblePersona = preferences.getString("costoCombustiblePersonaE",getString(R.string.por_persona));

        MainActivity.numAnuncio = preferences.getInt("numAnuncio",0);



        editTextEficiencia = view.findViewById(R.id.editEficienciaE);
        editTextDistancia = view.findViewById(R.id.editDistanciaE);
        editTextPrecioLitro = view.findViewById(R.id.editPrecioE);
        editPersona = view.findViewById(R.id.editPersonaE);
        textViewEficiencia = view.findViewById(R.id.fixedTextEficienciaE);
        textViewDistancia = view.findViewById(R.id.fixedTextDistanciaE);
        textViewPrecio = view.findViewById(R.id.fixedTextPrecioE);

        editTextEficiencia.setText(guardarCombustibleConsumido);
        editTextDistancia.setText(guardarDistancia);
        editTextPrecioLitro.setText(guardarPrecioCombustible);
        editPersona.setText(guardarNumeroPersonas);
        textViewEficiencia.setText(guardarTxtEficienciaCombustible);
        textViewDistancia.setText(guardarTxtDistancia);
        textViewPrecio.setText(guardarTxtPrecioCombustible);

        calculoLitroPor100km = view.findViewById(R.id.calculoLitrosPor100km);
        calculoLitroPor10km = view.findViewById(R.id.calculoLitrosPor10km);
        calculoLitroPorKm = view.findViewById(R.id.calculoLitrosPorkm);
        calculoMillasPorGalonUS = view.findViewById(R.id.calculoMillasPorGalonUS);
        calculoMillasPorGalonUK = view.findViewById(R.id.calculoMillasPorGalonUK);
        calculoCostoCombustible = view.findViewById(R.id.calculoCostoCombustibleE);
        calculoCostoCombustiblePorPersona = view.findViewById(R.id.calculoCostoCombustiblePorPersonaE);

        SpannableString spannableLitrosPor100km = new SpannableString(guardarLitros100km);
        SpannableString spannableLitrosPor10km = new SpannableString(guardarLitros10km);
        SpannableString spannableLitrosPorkm = new SpannableString(guardarLitroskm);
        SpannableString spannableMillasPorGalonUS = new SpannableString(guardarMillasGalonUS);
        SpannableString spannableMillasPorGalonUK = new SpannableString(guardarMillasGalonUK);
        SpannableString spannableCostoCombustible = new SpannableString(guardarCostoCombustible);
        SpannableString spannableCostoCombustiblePersona = new SpannableString(guardarCostoCombustiblePersona);

        // Regex para números con o sin decimales (ej: 7, 7.5, 7.50, 123.45)
        Pattern pattern = Pattern.compile("\\d+([.,]\\d+)?");
        Matcher matcher = pattern.matcher(guardarLitros100km);
        Matcher matcher2 = pattern.matcher(guardarLitros10km);
        Matcher matcher3 = pattern.matcher(guardarLitroskm);
        Matcher matcher4 = pattern.matcher(guardarMillasGalonUS);
        Matcher matcher5 = pattern.matcher(guardarMillasGalonUK);
        Matcher matcher6 = pattern.matcher(guardarCostoCombustible);
        Matcher matcher7 = pattern.matcher(guardarCostoCombustiblePersona);
        boolean primerNumeroFormateado = false;

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();


            String numero = guardarLitros100km.substring(start, end);

            // Evitar colorear " 100 " (como antes)
            boolean es100ConEspacios = false;
            if (numero.equals("100")) {
                int before = start - 1;
                int after = end;
                if (before >= 0 && after < guardarLitros100km.length()) {
                    if (Character.isWhitespace(guardarLitros100km.charAt(before)) && Character.isWhitespace(guardarLitros100km.charAt(after))) {
                        es100ConEspacios = true;
                    }
                }
            }

            if (es100ConEspacios) continue;

            // Color naranja
            spannableLitrosPor100km.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // Negrita
                spannableLitrosPor100km.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        while (matcher2.find()) {
            int start = matcher2.start();
            int end = matcher2.end();

            String numero2 = guardarLitros10km.substring(start, end);

            // Evitar colorear " 100 " (como antes)
            boolean es100ConEspacios = false;
            if (numero2.equals("10")) {
                int before = start - 1;
                int after = end;
                if (before >= 0 && after < guardarLitros10km.length()) {
                    if (Character.isWhitespace(guardarLitros10km.charAt(before)) && Character.isWhitespace(guardarLitros10km.charAt(after))) {
                        es100ConEspacios = true;
                    }
                }
            }

            if (es100ConEspacios) continue;

            // Color naranja
            spannableLitrosPor10km.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableLitrosPor10km.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher3.find()) {
            int start = matcher3.start();
            int end = matcher3.end();

            // Color naranja
            spannableLitrosPorkm.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableLitrosPorkm.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher4.find()) {
            int start = matcher4.start();
            int end = matcher4.end();

            // Color naranja
            spannableMillasPorGalonUS.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableMillasPorGalonUS.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher5.find()) {
            int start = matcher5.start();
            int end = matcher5.end();

            // Color naranja
            spannableMillasPorGalonUK.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableMillasPorGalonUK.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher6.find()) {
            int start = matcher6.start();
            int end = matcher6.end();

            // Color naranja
            spannableCostoCombustible.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableCostoCombustible.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher7.find()) {
            int start = matcher7.start();
            int end = matcher7.end();

            // Color naranja
            spannableCostoCombustiblePersona.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableCostoCombustiblePersona.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        calculoLitroPor100km.setText(spannableLitrosPor100km);
        calculoLitroPor10km.setText(spannableLitrosPor10km);
        calculoLitroPorKm.setText(spannableLitrosPorkm);
        calculoMillasPorGalonUS.setText(spannableMillasPorGalonUS);
        calculoMillasPorGalonUK.setText(spannableMillasPorGalonUK);
        calculoCostoCombustible.setText(spannableCostoCombustible);
        calculoCostoCombustiblePorPersona.setText(spannableCostoCombustiblePersona);

        settingsEficiencia = view.findViewById(R.id.settingsEficienciaE);
        settingsDistancia = view.findViewById(R.id.settingsDistanciaE);
        settingsPrecio = view.findViewById(R.id.settingsPrecioE);
        calcular = view.findViewById(R.id.botonCalcularEE);

        dibujarEficiencia();
        dibujarDistancia();
        dibujarPrecio();


        settingsEficiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), settingsEficiencia);
                popupMenu.getMenuInflater().inflate(R.menu.menu_opc_4, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId() != R.id.option4_1){
                            if(menuItem.getItemId() != R.id.option4_2){
                                if(menuItem.getItemId() != R.id.option4_3){

                                }else{
                                    textViewEficiencia.setText(getString(R.string.popupGalonesUK));
                                    dibujarEficiencia();
                                }
                            }else{
                                textViewEficiencia.setText(getString(R.string.popupGalonesUS));
                                dibujarEficiencia();
                            }
                        }else{
                            textViewEficiencia.setText(getString(R.string.popupLitros));
                            dibujarEficiencia();
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

        settingsDistancia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), settingsDistancia);
                popupMenu.getMenuInflater().inflate(R.menu.menu_opc_2, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() != R.id.option2_1) {
                            if(item.getItemId() != R.id.option2_2){

                            }else{
                                textViewDistancia.setText(getString(R.string.popupMillas));
                                dibujarDistancia();
                            }
                        } else {
                            textViewDistancia.setText(getString(R.string.popupKilometros));
                            dibujarDistancia();
                        }
                        return true;
                    }

                });

                popupMenu.show(); // Muestra el menú


            }
        });

        settingsPrecio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), settingsPrecio);
                popupMenu.getMenuInflater().inflate(R.menu.menu_opc_3, popupMenu.getMenu());

                // Manejar clics en los ítems del menú
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() != R.id.option3_1) {
                            if(item.getItemId() != R.id.option3_2){
                                if(item.getItemId() != R.id.option3_3){

                                }else{
                                    textViewPrecio.setText(getString(R.string.popupPorGalonUK));
                                    dibujarPrecio();
                                }
                            }else{
                                textViewPrecio.setText(getString(R.string.popupPorGalonUS));
                                dibujarPrecio();
                            }
                        } else {
                            textViewPrecio.setText(getString(R.string.popupPorLitro));
                            dibujarPrecio();
                        }
                        return true;
                    }

                });

                popupMenu.show(); // Muestra el menú


            }
        });

        calcular = view.findViewById(R.id.botonCalcularEE);

        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rEficienciaCombustible = 0;
                double rDistancia = 0;
                double rPrecioCombustible = 0;
                int rNumPersonas = 1;

                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                View view2 = requireActivity().getCurrentFocus();
                if (view2 != null) {
                    imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);
                }

                if(!editTextEficiencia.getText().toString().isEmpty()){
                    rEficienciaCombustible = Double.parseDouble(editTextEficiencia.getText().toString());

                }else{
                    editTextEficiencia.setText("0");
                    rEficienciaCombustible = 0;
                }

                if(!editTextDistancia.getText().toString().isEmpty()){
                    rDistancia = Double.parseDouble(editTextDistancia.getText().toString());

                }else{
                    editTextDistancia.setText("0");
                    rDistancia = 0;
                }

                if(!editTextPrecioLitro.getText().toString().isEmpty()){
                    rPrecioCombustible = Double.parseDouble(editTextPrecioLitro.getText().toString());

                }else{
                    editTextPrecioLitro.setText("0");
                    rPrecioCombustible = 0;
                }

                if(!editPersona.getText().toString().equals("0") && !editPersona.getText().toString().isEmpty()){
                    rNumPersonas = Integer.parseInt(editPersona.getText().toString());
                }else{
                    editPersona.setText("1");
                    rNumPersonas = 1;
                }

                String precioLitro = "";
                String distancia = "";
                String eficienciaCombustible = "";
                double resultadoLitrosPor100km = 0;
                double resultadoLitrosPor10km = 0;
                double resultadoLitrosPorkm = 0;
                double resultadoMillasPorGalonUS = 0;
                double resultadoMillasPorGalonUK = 0;
                double resultadoCostoCombustible = 0;
                double resultadoCostoCombustiblePorPersona = 0;
                double pasarAKilometros = 0;
                double pasarAGalonesUS = 0;
                double pasarAGalonesUK = 0;
                double pasarAKilometrosporLitro = 0;
                int opc = 0;
                if(textViewEficiencia.getText().toString().equals(getString(R.string.popupLitros))){
                    eficienciaCombustible = textViewEficiencia.getText().toString();
                    opc = 1;
                    if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            resultadoLitrosPor100km = (rEficienciaCombustible/rDistancia)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/rDistancia)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            resultadoLitrosPor100km = (rEficienciaCombustible/rDistancia)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/rDistancia)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            resultadoLitrosPor100km = (rEficienciaCombustible/rDistancia)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/rDistancia)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (rEficienciaCombustible/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/pasarAKilometros)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/pasarAKilometros;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (rEficienciaCombustible/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/pasarAKilometros)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/pasarAKilometros;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (rEficienciaCombustible/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (rEficienciaCombustible/pasarAKilometros)*10;
                            resultadoLitrosPorkm = rEficienciaCombustible/pasarAKilometros;
                            pasarAGalonesUS = rEficienciaCombustible/3.78541;
                            pasarAGalonesUK = rEficienciaCombustible/4.54609;
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }
                }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupGalonesUS))){
                    eficienciaCombustible = textViewEficiencia.getText().toString();
                    opc = 2;
                    if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541;
                            resultadoLitrosPor100km = (pasarAGalonesUS/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/rDistancia)*10;
                            resultadoLitrosPorkm = (pasarAGalonesUS/rDistancia);
                            pasarAKilometros = rDistancia / 1.60934;
                            resultadoMillasPorGalonUS = pasarAKilometros/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541;
                            resultadoLitrosPor100km = (pasarAGalonesUS/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/rDistancia)*10;
                            resultadoLitrosPorkm = (pasarAGalonesUS/rDistancia);
                            pasarAKilometros = rDistancia / 1.60934;
                            resultadoMillasPorGalonUS = pasarAKilometros/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541;
                            resultadoLitrosPor100km = (pasarAGalonesUS/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/rDistancia)*10;
                            resultadoLitrosPorkm = (pasarAGalonesUS/rDistancia);
                            pasarAKilometros = rDistancia / 1.60934;
                            resultadoMillasPorGalonUS = pasarAKilometros/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = pasarAKilometros/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia * 1.60934; //CONTIENE LOS KM
                            resultadoLitrosPor100km = (pasarAGalonesUS/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/pasarAKilometros)*10;
                            resultadoLitrosPorkm  =pasarAGalonesUS/pasarAKilometros;
                            resultadoMillasPorGalonUS = rDistancia/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia * 1.60934; //CONTIENE LOS KM
                            resultadoLitrosPor100km = (pasarAGalonesUS/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/pasarAKilometros)*10;
                            resultadoLitrosPorkm  =pasarAGalonesUS/pasarAKilometros;
                            resultadoMillasPorGalonUS = rDistancia/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            pasarAGalonesUS = rEficienciaCombustible*3.78541; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia * 1.60934; //CONTIENE LOS KM
                            resultadoLitrosPor100km = (pasarAGalonesUS/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUS/pasarAKilometros)*10;
                            resultadoLitrosPorkm  =pasarAGalonesUS/pasarAKilometros;
                            resultadoMillasPorGalonUS = rDistancia/rEficienciaCombustible;
                            pasarAGalonesUK = pasarAGalonesUS/4.54609;
                            resultadoMillasPorGalonUK = rDistancia/pasarAGalonesUK;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }
                }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupGalonesUK))){
                    eficienciaCombustible = textViewEficiencia.getText().toString();
                    opc = 3;
                    if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609;//CONTIENE LOS LITROS
                            resultadoLitrosPor100km = (pasarAGalonesUK/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/rDistancia)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = pasarAKilometros/rEficienciaCombustible;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609;//CONTIENE LOS LITROS
                            resultadoLitrosPor100km = (pasarAGalonesUK/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/rDistancia)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = pasarAKilometros/rEficienciaCombustible;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609;//CONTIENE LOS LITROS
                            resultadoLitrosPor100km = (pasarAGalonesUK/rDistancia)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/rDistancia)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/rDistancia;
                            pasarAKilometros = rDistancia/1.60934;
                            pasarAGalonesUS = rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = pasarAKilometros/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = pasarAKilometros/rEficienciaCombustible;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                        distancia = textViewDistancia.getText().toString();
                        if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                            precioLitro = "Por litro";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (pasarAGalonesUK/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/pasarAKilometros)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/pasarAKilometros;
                            pasarAGalonesUS  =rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/rEficienciaCombustible;
                            resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;

                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                            precioLitro = "Por galón US";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (pasarAGalonesUK/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/pasarAKilometros)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/pasarAKilometros;
                            pasarAGalonesUS  =rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/rEficienciaCombustible;
                            resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                            precioLitro = "Por galón UK";
                            pasarAGalonesUK = rEficienciaCombustible*4.54609; //CONTIENE LOS LITROS
                            pasarAKilometros = rDistancia*1.60934;
                            resultadoLitrosPor100km = (pasarAGalonesUK/pasarAKilometros)*100;
                            resultadoLitrosPor10km = (pasarAGalonesUK/pasarAKilometros)*10;
                            resultadoLitrosPorkm = pasarAGalonesUK/pasarAKilometros;
                            pasarAGalonesUS  =rEficienciaCombustible*(4.54609/3.78541);
                            resultadoMillasPorGalonUS = rDistancia/pasarAGalonesUS;
                            resultadoMillasPorGalonUK = rDistancia/rEficienciaCombustible;
                            resultadoCostoCombustible = rEficienciaCombustible*rPrecioCombustible;
                            resultadoCostoCombustiblePorPersona = resultadoCostoCombustible/rNumPersonas;
                        }
                    }
                }

                String numFormateadoLitrosPor100km;
                if(resultadoLitrosPor100km>1e10){
                    numFormateadoLitrosPor100km = String.format("%.2e",resultadoLitrosPor100km);
                }else{
                    numFormateadoLitrosPor100km = String.format("%.2f",resultadoLitrosPor100km);
                }

                String numFormateadoLitrosPor10km;
                if(resultadoLitrosPor10km>1e10){
                    numFormateadoLitrosPor10km = String.format("%.2e",resultadoLitrosPor10km);
                }else{
                    numFormateadoLitrosPor10km = String.format("%.2f",resultadoLitrosPor10km);
                }

                String numFormateadoLitrosPorkm;
                if(resultadoLitrosPorkm>1e10){
                    numFormateadoLitrosPorkm = String.format("%.2e",resultadoLitrosPorkm);
                }else{
                    numFormateadoLitrosPorkm = String.format("%.2f",resultadoLitrosPorkm);
                }

                String numFormateadoMillasPorGalonUS;
                if(resultadoMillasPorGalonUS>1e10){
                    numFormateadoMillasPorGalonUS = String.format("%.2e",resultadoMillasPorGalonUS);
                }else{
                    numFormateadoMillasPorGalonUS = String.format("%.2f",resultadoMillasPorGalonUS);
                }

                String numFormateadoMillasPorGalonUK;
                if(resultadoMillasPorGalonUK>1e10){
                    numFormateadoMillasPorGalonUK = String.format("%.2e",resultadoMillasPorGalonUK);
                }else{
                    numFormateadoMillasPorGalonUK = String.format("%.2f",resultadoMillasPorGalonUK);
                }

                String numFormateadoCostoCombustible;
                if(resultadoCostoCombustible>1e10){
                    numFormateadoCostoCombustible = String.format("%.2e",resultadoCostoCombustible);
                }else{
                    numFormateadoCostoCombustible = String.format("%.2f",resultadoCostoCombustible);
                }


                String numFormateadoCostoCombustiblePorPersona;
                if(resultadoCostoCombustiblePorPersona>1e10){
                    numFormateadoCostoCombustiblePorPersona = String.format("%.2e",resultadoCostoCombustiblePorPersona);
                }else{
                    numFormateadoCostoCombustiblePorPersona = String.format("%.2f",resultadoCostoCombustiblePorPersona);
                }



                String textoCompletoLitrosPor100km =numFormateadoLitrosPor100km+" "+getString(R.string.litros_por_cada_100_km);

                int start1 = textoCompletoLitrosPor100km.indexOf(numFormateadoLitrosPor100km);
                int end1 = start1 + numFormateadoLitrosPor100km.length();

                String textoCompletoLitrosPor10km = numFormateadoLitrosPor10km+" "+getString(R.string.litros_por_cada_10_km);

                int start2 = textoCompletoLitrosPor10km.indexOf(numFormateadoLitrosPor10km);
                int end2 = start2 + numFormateadoLitrosPor10km.length();

                String textoCompletoLitrosPorkm = numFormateadoLitrosPorkm+" "+getString(R.string.litros_por_cada_km);

                int start3 = textoCompletoLitrosPorkm.indexOf(numFormateadoLitrosPorkm);
                int end3 = start3 + numFormateadoLitrosPorkm.length();

                String textoCompletoMillasPorGalonUS =numFormateadoMillasPorGalonUS+" "+getString(R.string.millas_por_gal_n_us);

                int start4 = textoCompletoMillasPorGalonUS.indexOf(numFormateadoMillasPorGalonUS);
                int end4 = start4 + numFormateadoMillasPorGalonUS.length();

                String textoCompletoMillasPorGalonUK =numFormateadoMillasPorGalonUK+" "+getString(R.string.millas_por_gal_n_uk);

                int start5 = textoCompletoMillasPorGalonUK.indexOf(numFormateadoMillasPorGalonUK);
                int end5 = start5 + numFormateadoMillasPorGalonUK.length();

                String textoCompletoCostoCombustible =getString(R.string.costo_del_combustibleMin)+" "+numFormateadoCostoCombustible;

                int start6 = textoCompletoCostoCombustible.indexOf(numFormateadoCostoCombustible);
                int end6 = start6 + numFormateadoCostoCombustible.length();

                String textoCompletoCostoCombustiblePorPersona =numFormateadoCostoCombustiblePorPersona+" "+getString(R.string.por_persona);

                int start7 = textoCompletoCostoCombustiblePorPersona.indexOf(numFormateadoCostoCombustiblePorPersona);
                int end7 = start7 + numFormateadoCostoCombustiblePorPersona.length();


                SpannableString spannableCalculoPor100km = new SpannableString(textoCompletoLitrosPor100km);
                spannableCalculoPor100km.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start1,
                        end1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCalculoPor100km.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start1,
                        end1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableCalculoPor10km = new SpannableString(textoCompletoLitrosPor10km);
                spannableCalculoPor10km.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start2,
                        end2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCalculoPor10km.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start2,
                        end2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableCalculoPorkm = new SpannableString(textoCompletoLitrosPorkm);

                spannableCalculoPorkm.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start3,
                        end3,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCalculoPorkm.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start3,
                        end3,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableMillasPorGalonUS = new SpannableString(textoCompletoMillasPorGalonUS);
                spannableMillasPorGalonUS.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start4,
                        end4,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableMillasPorGalonUS.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start4,
                        end4,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableMillasPorGalonUK = new SpannableString(textoCompletoMillasPorGalonUK);
                spannableMillasPorGalonUK.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start5,
                        end5,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableMillasPorGalonUK.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start5,
                        end5,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                SpannableString spannableCalculoCostoCombustible = new SpannableString(textoCompletoCostoCombustible);
                spannableCalculoCostoCombustible.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start6,
                        end6,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCalculoCostoCombustible.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start6,
                        end6,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableCalculoCostoCombustiblePorPersona = new SpannableString(textoCompletoCostoCombustiblePorPersona);
                spannableCalculoCostoCombustiblePorPersona.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start7,
                        end7,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCalculoCostoCombustiblePorPersona.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start7,
                        end7,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );


                //Toast.makeText(getContext(),eficienciaCombustible +","+ distancia+","+precioLitro,Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(),"Combustible consumido: "+resultadoCombustibleConsumido +", Costo del combustible: "+ resultadoCostoCombustible +", Costo por persona: "+resultadoCostoPersona,Toast.LENGTH_LONG).show();
                calculoLitroPor100km.setText(spannableCalculoPor100km);
                calculoLitroPor10km.setText(spannableCalculoPor10km);
                calculoLitroPorKm.setText(spannableCalculoPorkm);
                calculoMillasPorGalonUS.setText(spannableMillasPorGalonUS);
                calculoMillasPorGalonUK.setText(spannableMillasPorGalonUK);
                calculoCostoCombustible.setText(spannableCalculoCostoCombustible);
                calculoCostoCombustiblePorPersona.setText(spannableCalculoCostoCombustiblePorPersona);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("eficienciaCombustibleE",String.valueOf(rEficienciaCombustible));
                editor.putString("distanciaE",String.valueOf(rDistancia));
                editor.putString("precioCombustibleE",String.valueOf(rPrecioCombustible));
                editor.putString("numeroPersonasE",String.valueOf(rNumPersonas));

                editor.putString("txtEficienciaCombustibleE",textViewEficiencia.getText().toString());
                editor.putString("txtDistanciaE",textViewDistancia.getText().toString());
                editor.putString("txtPrecioCombustibleE",textViewPrecio.getText().toString());

                editor.putString("litros100km", String.valueOf(spannableCalculoPor100km));
                editor.putString("litros10km", String.valueOf(spannableCalculoPor10km));
                editor.putString("litroskm", String.valueOf(spannableCalculoPorkm));
                editor.putString("millasGalonUS", String.valueOf(spannableMillasPorGalonUS));
                editor.putString("millasGalonUK", String.valueOf(spannableMillasPorGalonUK));
                editor.putString("costoCombustibleE", String.valueOf(spannableCalculoCostoCombustible));
                editor.putString("costoCombustiblePersonaE", String.valueOf(spannableCalculoCostoCombustiblePorPersona));
                MainActivity.numAnuncio++;

            editor.putInt("numAnuncio",MainActivity.numAnuncio);


                editor.commit();

                if (interstitialAD != null && MainActivity.numAnuncio % 12 == 0) {
                    mostrarAnuncio();
                } else {
                    Log.d(TAG, "The interstitial ad is still loading.");
                }



            }
        });





    }

    private void dibujarEficiencia(){
        textViewEficiencia.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    int extraPadding = textViewEficiencia.getWidth() + dpToPx(3); // 3dp extra para espacio
                    editTextEficiencia.setPadding(
                            editTextEficiencia.getPaddingLeft(),
                            editTextEficiencia.getPaddingTop(),
                            extraPadding,
                            editTextEficiencia.getPaddingBottom()
                    );
                }
            }
        });
    }

    private void dibujarDistancia(){
        textViewDistancia.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    int extraPadding = textViewDistancia.getWidth() + dpToPx(3); // 3dp extra para espacio
                    editTextDistancia.setPadding(
                            editTextDistancia.getPaddingLeft(),
                            editTextDistancia.getPaddingTop(),
                            extraPadding,
                            editTextDistancia.getPaddingBottom()
                    );
                }
            }
        });
    }

    private void dibujarPrecio(){
        textViewPrecio.post(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    int extraPadding = textViewPrecio.getWidth() + dpToPx(3); // 3dp extra para espacio
                    editTextPrecioLitro.setPadding(
                            editTextEficiencia.getPaddingLeft(),
                            editTextPrecioLitro.getPaddingTop(),
                            extraPadding,
                            editTextPrecioLitro.getPaddingBottom()
                    );
                }
            }
        });
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(
                requireContext(),
                "ca-app-pub-3940256099942544/1033173712", // Reemplaza con tu ID real
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAD = ad;

                        interstitialAD.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Puedes cargar un nuevo anuncio aquí si quieres
                                loadInterstitialAd();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                interstitialAD = null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                interstitialAD = null; // Limpia la referencia
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        interstitialAD = null;
                    }
                }
        );
    }

    private void mostrarAnuncio() {
        if (interstitialAD != null) {
            interstitialAD.show(requireActivity());
        } else {
            Toast.makeText(requireContext(), "Anuncio no cargado", Toast.LENGTH_SHORT).show();
        }
    }
}