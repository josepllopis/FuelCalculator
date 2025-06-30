package apps.lustven.fuelcalculator.fragments;

import static android.content.ContentValues.TAG;
import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.webkit.internal.ApiFeature;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import apps.lustven.fuelcalculator.MainActivity;
import apps.lustven.fuelcalculator.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentVolumen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentVolumen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView settingsEficiencia,settingsDistancia,settingsPrecio;
    private EditText editTextEficiencia;
    private TextView textViewEficiencia;
    private EditText editTextDistancia;
    private EditText editTextPrecioLitro;
    private EditText editPersona;
    private TextView textViewDistancia;
    private TextView textViewPrecio;
    private Button calcular;
    InterstitialAd interstitialAD;

    private TextView calculoCombustibleConsumido, calculoCostoCombustible, calculoCostoPersona;
    private String guardarDistancia,guardarPrecioCombustible,guardarEficienciaCombustible,guardarNumeroPersonas,guardarTxtEficienciaCombustible,guardarTxtDistancia,guardarTxtPrecioCombustible,guardarCombustibleConsumido,guardarCostoCombustible,guardarCostoPorPersona;

    public fragmentVolumen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentVolumen.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentVolumen newInstance(String param1, String param2) {
        fragmentVolumen fragment = new fragmentVolumen();
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
        return inflater.inflate(R.layout.fragment_volumen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadInterstitialAd();

        SharedPreferences preferences = getContext().getSharedPreferences("datos", Context.MODE_PRIVATE);

        guardarEficienciaCombustible = preferences.getString("eficienciaCombustible","");
        guardarDistancia = preferences.getString("distancia","");
        guardarPrecioCombustible = preferences.getString("precioCombustible","");
        guardarNumeroPersonas = preferences.getString("numeroPersonas","");
        guardarTxtEficienciaCombustible = preferences.getString("txtEficienciaCombustible",getString(R.string.popupLitrosPor100km));
        guardarTxtDistancia = preferences.getString("txtDistancia",getString(R.string.popupKilometros));
        guardarTxtPrecioCombustible = preferences.getString("txtPrecioCombustible",getString(R.string.popupPorLitro));

        guardarCombustibleConsumido = preferences.getString("combustibleConsumido",getString(R.string.combustible_consumido));
        guardarCostoCombustible = preferences.getString("costoConsumido",getString(R.string.costo_del_combustible));
        guardarCostoPorPersona = preferences.getString("costoPorPersona",getString(R.string.costo_por_persona));
        MainActivity.numAnuncio = preferences.getInt("numAnuncio",0);


        editTextEficiencia = view.findViewById(R.id.editEficiencia);
        editTextDistancia = view.findViewById(R.id.editDistancia);
        editTextPrecioLitro = view.findViewById(R.id.editPrecio);
        editPersona = view.findViewById(R.id.editPersona);
        textViewEficiencia = view.findViewById(R.id.fixedTextEficiencia);
        textViewDistancia = view.findViewById(R.id.fixedTextDistancia);
        textViewPrecio = view.findViewById(R.id.fixedTextPrecio);

        editTextEficiencia.setText(guardarEficienciaCombustible);
        editTextDistancia.setText(guardarDistancia);
        editTextPrecioLitro.setText(guardarPrecioCombustible);
        editPersona.setText(guardarNumeroPersonas);
        textViewEficiencia.setText(guardarTxtEficienciaCombustible);
        textViewDistancia.setText(guardarTxtDistancia);
        textViewPrecio.setText(guardarTxtPrecioCombustible);

        calculoCombustibleConsumido = view.findViewById(R.id.calculoCombustibleConsumido);
        calculoCostoCombustible = view.findViewById(R.id.calculoCostoCombustible);
        calculoCostoPersona = view.findViewById(R.id.calculoCostoPersona);


        SpannableString spannableCombustibleConsumido = new SpannableString(guardarCombustibleConsumido);
        SpannableString spannableCostoCombustible = new SpannableString(guardarCostoCombustible);
        SpannableString spannableCostoPersona = new SpannableString(guardarCostoPorPersona);
        // Regex para números con o sin decimales (ej: 7, 7.5, 7.50, 123.45)
        Pattern pattern = Pattern.compile("\\d+([.,]\\d+)?");
        Matcher matcher = pattern.matcher(guardarCombustibleConsumido);
        Matcher matcher2 = pattern.matcher(guardarCostoCombustible);
        Matcher matcher3 = pattern.matcher(guardarCostoPorPersona);

// Recorre cada número encontrado y aplica formato
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            // Color naranja
            spannableCombustibleConsumido.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableCombustibleConsumido.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        while (matcher2.find()) {
            int start = matcher2.start();
            int end = matcher2.end();

            // Color naranja
            spannableCostoCombustible.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableCostoCombustible.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        while (matcher3.find()) {
            int start = matcher3.start();
            int end = matcher3.end();

            // Color naranja
            spannableCostoPersona.setSpan(new ForegroundColorSpan(Color.parseColor("#FFA500")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Negrita
            spannableCostoPersona.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        calculoCombustibleConsumido.setText(spannableCombustibleConsumido);
        calculoCostoCombustible.setText(spannableCostoCombustible);
        calculoCostoPersona.setText(spannableCostoPersona);

        settingsEficiencia = view.findViewById(R.id.settingsEficiencia);
        settingsDistancia = view.findViewById(R.id.settingsDistancia);
        settingsPrecio = view.findViewById(R.id.settingsPrecio);
        dibujarEficiencia();
        dibujarDistancia();
        dibujarPrecio();




        settingsEficiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), settingsEficiencia);
                popupMenu.getMenuInflater().inflate(R.menu.menu_opc_1, popupMenu.getMenu());

                // Manejar clics en los ítems del menú
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() != R.id.option1_1) {
                            if(item.getItemId() != R.id.option1_2){
                                if(item.getItemId() != R.id.option1_3){
                                    if(item.getItemId() != R.id.option1_4){
                                        if(item.getItemId() != R.id.option1_5){

                                        }else{
                                            textViewEficiencia.setText(getString(R.string.popupMillasPorGalonUK));
                                            dibujarEficiencia();
                                        }
                                    }else{
                                        textViewEficiencia.setText(getString(R.string.popupMillasPorGalonUS));
                                        dibujarEficiencia();
                                    }
                                }else{
                                    textViewEficiencia.setText(getString(R.string.popupLitrosPorkm));
                                    dibujarEficiencia();
                                }
                            }else{
                                textViewEficiencia.setText(getString(R.string.popupLitrosPor10km));
                                dibujarEficiencia();
                            }
                        } else {
                            textViewEficiencia.setText(getString(R.string.popupLitrosPor100km));
                            dibujarEficiencia();
                        }
                        return true;
                    }

                });

                popupMenu.show(); // Muestra el menú

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


        calcular = view.findViewById(R.id.botonCalcularE);

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
            double resultadoCombustibleConsumido = 0;
            double resultadoCostoCombustible = 0;
            double resultadoCostoPersona = 0;
            double pasarAKilometros = 0;
            double pasarAGalonesUS = 0;
            double pasarAGalonesUK = 0;
            double pasarAKilometrosporLitro = 0;
            int opc = 0;
            if(textViewEficiencia.getText().toString().equals(getString(R.string.popupLitrosPor100km))){
                eficienciaCombustible = textViewEficiencia.getText().toString();
                opc = 1;
                if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/100;
                        resultadoCostoCombustible = rPrecioCombustible*resultadoCombustibleConsumido;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/100;
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/100;
                        pasarAGalonesUK = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }
                }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/100;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/100;
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/100;
                        pasarAGalonesUS = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }
                }
            }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupLitrosPor10km))){
                eficienciaCombustible = textViewEficiencia.getText().toString();
                opc = 2;
                if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/10;
                        resultadoCostoCombustible = rPrecioCombustible*resultadoCombustibleConsumido;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/10;
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia)/10;
                        pasarAGalonesUK = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }
                }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/10;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/10;
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros)/10;
                        pasarAGalonesUS = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }
                }
            }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupLitrosPorkm))){
                eficienciaCombustible = textViewEficiencia.getText().toString();
                opc = 3;
                if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia);
                        resultadoCostoCombustible = rPrecioCombustible*resultadoCombustibleConsumido;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia);
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*rDistancia);
                        pasarAGalonesUK = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }
                }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros);
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros);
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        pasarAKilometros = rDistancia * 1.60934;
                        resultadoCombustibleConsumido =  (rEficienciaCombustible*pasarAKilometros);
                        pasarAGalonesUS = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }
                }
            }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupMillasPorGalonUS))){
                eficienciaCombustible = textViewEficiencia.getText().toString();
                opc = 4;
                if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/3.78541;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;

                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/3.78541;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        pasarAGalonesUS = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUS*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUS;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/3.78541;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        pasarAGalonesUK = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }
                }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        resultadoCombustibleConsumido = rDistancia/rEficienciaCombustible;
                        resultadoCombustibleConsumido = resultadoCombustibleConsumido*3.78541;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        resultadoCombustibleConsumido = rDistancia/rEficienciaCombustible;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        double galonesUK = rDistancia/rEficienciaCombustible;
                        resultadoCombustibleConsumido = galonesUK*0.83267;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;

                    }
                }
            }else if(textViewEficiencia.getText().toString().equals(getString(R.string.popupMillasPorGalonUK))){
                eficienciaCombustible = textViewEficiencia.getText().toString();
                opc = 5;
                if(textViewDistancia.getText().toString().equals(getString(R.string.popupKilometros))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/4.54609;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/4.54609;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        pasarAGalonesUK = resultadoCombustibleConsumido/3.78541;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        pasarAKilometrosporLitro = (rEficienciaCombustible*1.60934)/4.54609;
                        resultadoCombustibleConsumido = rDistancia/pasarAKilometrosporLitro;
                        pasarAGalonesUK = resultadoCombustibleConsumido/4.54609;
                        resultadoCostoCombustible = pasarAGalonesUK*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                        resultadoCombustibleConsumido = pasarAGalonesUK;
                    }
                }else if(textViewDistancia.getText().toString().equals(getString(R.string.popupMillas))){
                    distancia = textViewDistancia.getText().toString();
                    if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                        precioLitro = "Por litro";
                        resultadoCombustibleConsumido = rDistancia/rEficienciaCombustible;
                        resultadoCombustibleConsumido = resultadoCombustibleConsumido*4.54609;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                        precioLitro = "Por galón US";
                        double galonesUK = rDistancia/rEficienciaCombustible;
                        resultadoCombustibleConsumido = galonesUK*1.2010;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;

                    }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                        precioLitro = "Por galón UK";
                        resultadoCombustibleConsumido = rDistancia/rEficienciaCombustible;
                        resultadoCostoCombustible = resultadoCombustibleConsumido*rPrecioCombustible;
                        resultadoCostoPersona = resultadoCostoCombustible/rNumPersonas;
                    }
                }
            }

            String numFormateadoCombustibleConsumido;
            if(resultadoCombustibleConsumido>1e10){
                numFormateadoCombustibleConsumido = String.format("%.2e",resultadoCombustibleConsumido);
            }else{
                numFormateadoCombustibleConsumido = String.format("%.2f",resultadoCombustibleConsumido);
            }

                String numFormateadoCostoCombustible;
                if(resultadoCostoCombustible>1e10){
                    numFormateadoCostoCombustible = String.format("%.2e",resultadoCostoCombustible);
                }else{
                    numFormateadoCostoCombustible = String.format("%.2f",resultadoCostoCombustible);
                }

                String numFormateadoCostoPersona;
                if(resultadoCostoPersona>1e10){
                    numFormateadoCostoPersona = String.format("%.2e",resultadoCostoPersona);
                }else{
                    numFormateadoCostoPersona = String.format("%.2f",resultadoCostoPersona);
                }

                String abreviatura="";

                if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorLitro))){
                    abreviatura = getString(R.string.litrosMin);
                }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUS))){
                    abreviatura = getString(R.string.galonesUS);
                }else if(textViewPrecio.getText().toString().equals(getString(R.string.popupPorGalonUK))){
                    abreviatura = getString(R.string.galonesUK);
                }


                String textoCompletoCombustibleConsumido = getString(R.string.combustible_consumido)+" "+numFormateadoCombustibleConsumido+" "+abreviatura;

                int start1 = textoCompletoCombustibleConsumido.indexOf(numFormateadoCombustibleConsumido);
                int end1 = start1 + numFormateadoCombustibleConsumido.length();

                String textoCompletoCostoCombustible = getString(R.string.costo_del_combustible)+" "+numFormateadoCostoCombustible;

                int start2 = textoCompletoCostoCombustible.indexOf(numFormateadoCostoCombustible);
                int end2 = start2 + numFormateadoCostoCombustible.length();

                String textoCompletoCostoPersona = getString(R.string.costo_por_persona)+" "+numFormateadoCostoPersona+" "+getString(R.string.por_persona);

                int start3 = textoCompletoCostoPersona.indexOf(numFormateadoCostoPersona);
                int end3 = start3 + numFormateadoCostoPersona.length();


                SpannableString spannableCombustibleConsumido = new SpannableString(textoCompletoCombustibleConsumido);
                spannableCombustibleConsumido.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start1,
                        end1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCombustibleConsumido.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start1,
                        end1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableCostoCombustible = new SpannableString(textoCompletoCostoCombustible);
                spannableCostoCombustible.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start2,
                        end2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCostoCombustible.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start2,
                        end2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                SpannableString spannableCostoPersona = new SpannableString(textoCompletoCostoPersona);

                spannableCostoPersona.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FFA500")), // naranja
                        start3,
                        end3,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                spannableCostoPersona.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        start3,
                        end3,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );


            calculoCombustibleConsumido.setText(spannableCombustibleConsumido);
            calculoCostoCombustible.setText(spannableCostoCombustible);
            calculoCostoPersona.setText(spannableCostoPersona);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("eficienciaCombustible",String.valueOf(rEficienciaCombustible));
            editor.putString("distancia",String.valueOf(rDistancia));
            editor.putString("precioCombustible",String.valueOf(rPrecioCombustible));
            editor.putString("numeroPersonas",String.valueOf(rNumPersonas));

            editor.putString("txtEficienciaCombustible",textViewEficiencia.getText().toString());
            editor.putString("txtDistancia",textViewDistancia.getText().toString());
            editor.putString("txtPrecioCombustible",textViewPrecio.getText().toString());

            editor.putString("combustibleConsumido", String.valueOf(spannableCombustibleConsumido));
            editor.putString("costoConsumido", String.valueOf(spannableCostoCombustible));
            editor.putString("costoPorPersona", String.valueOf(spannableCostoPersona));
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

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
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