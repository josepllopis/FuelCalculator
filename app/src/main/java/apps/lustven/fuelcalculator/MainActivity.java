package apps.lustven.fuelcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import apps.lustven.fuelcalculator.databinding.ActivityMainBinding;
import apps.lustven.fuelcalculator.fragments.fragmentEfficency;
import apps.lustven.fuelcalculator.fragments.fragmentVolumen;
import apps.lustven.fuelcalculator.model.App;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    SharedPreferences.Editor editor;
    String strRef = "https://appsjunior-c9230-default-rtdb.europe-west1.firebasedatabase.app/";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(strRef);

    DrawerLayout drawer;
    private ListView menuLateral;
    private List<App> listMoreApps;
    private AppAdapter appAdapter;
    private ImageView menuButton;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG_VOLUMEN = "fragment_volumen";
    AdView adView;
    FrameLayout bannerContainer;
    public static int numAnuncio = 0;
    private static final String TAG_EFICIENCIA = "fragment_eficiencia";
    private String guardarDistancia,guardarPrecioCombustible,guardarEficienciaCombustible,guardarNumeroPersonas,guardarTxtEficienciaCombustible,guardarTxtDistancia,guardarTxtPrecioCombustible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/




        replaceFragment(new fragmentVolumen());


        bottomNavigationView = findViewById(R.id.bottomNavegation);

        if (savedInstanceState != null) {
            int selectedItem = savedInstanceState.getInt("selectedItem", R.id.navegationVolumen);

            if(selectedItem == R.id.navegationVolumen){
                showFragment(TAG_VOLUMEN, fragmentVolumen::new);
            }else if(selectedItem == R.id.navegationEfficency){
                replaceFragment(new fragmentEfficency());
                showFragment(TAG_EFICIENCIA, fragmentEfficency::new);
            }
            bottomNavigationView.setSelectedItemId(selectedItem);

        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavegation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.navegationVolumen){
                replaceFragment(new fragmentVolumen());
            }else if(id == R.id.navegationEfficency){
                replaceFragment(new fragmentEfficency());
            }

            return true;
        });


        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);


        bannerContainer = findViewById(R.id.frameBanner);

        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-3940256099942544/9214589741");
        // Request an anchored adaptive banner with a width of 360.
        adView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, 360));
        // Create a new ad view.

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Replace ad container with new ad view.
        bannerContainer.removeAllViews();
        bannerContainer.addView(adView);



        // En tu actividad o fragmento
        ImageView themeToggle = (ImageView)findViewById(R.id.themeSwitch);

// Iconos para el modo día y noche
        int iconDay = R.drawable.baseline_wb_sunny_24;      // Icono para el modo día
        int iconNight = R.drawable.outline_wb_sunny_24;  // Icono para el modo noche

// Comprobar el estado inicial del tema y establecer el icono adecuado
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            themeToggle.setImageResource(iconNight);
        } else {
            themeToggle.setImageResource(iconDay);
        }

        // Configura el listener para cambiar el tema y el icono
        themeToggle.setOnClickListener(new View.OnClickListener() {
            private boolean isNightMode = ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);

            @Override
            public void onClick(View v) {
                if (isNightMode) {
                    // Cambiar a modo día
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    themeToggle.setImageResource(iconDay);
                    editor = preferences.edit();
                    editor.putBoolean("night",false);// Cambiar al icono de día
                } else {
                    // Cambiar a modo noche
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = preferences.edit();
                    editor.putBoolean("night",true);
                    themeToggle.setImageResource(iconNight);  // Cambiar al icono de noche
                }
                editor.commit();
                isNightMode = !isNightMode; // Alterna el estado
            }
        });

        menuLateral = (ListView) findViewById(R.id.menulateral);

        listMoreApps = new ArrayList<>();
        appAdapter = new AppAdapter(this, R.layout.list_row, listMoreApps,this);
        menuLateral.setAdapter(appAdapter);

        reference = FirebaseDatabase.getInstance().getReference().child("Apps");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listMoreApps.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    App app = dataSnapshot.getValue(App.class);
                    listMoreApps.add(app);
                }
                appAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.getMessage());
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        Drawable opc = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_menu_24, this.getTheme()); //Le pasamos el mipmap pero estamos creando drwaable no nos da problemas de momento
        //Instanciamos el Layout del Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        //Instanciamos y construimos el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        toggle.setDrawerIndicatorEnabled(false);// Hay que decirle que deshabilite el burger actual
        toggle.setHomeAsUpIndicator(opc); //Le indico cual va a ser el Drawable que utilizará para sacar y meter el navigator
        //Creamos un listener especial él.
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        menuButton = findViewById(R.id.menuButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ActivityOpciones.class);
                startActivity(i);
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framePrincipal, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavegation);
        outState.putInt("selectedItem", bottomNav.getSelectedItemId());
    }

    private void showFragment(String tag, Supplier<Fragment> fragmentSupplier) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Ocultar todos los fragments actuales
        for (Fragment fragment : fragmentManager.getFragments()) {
            transaction.hide(fragment);
        }

        // Buscar si ya existe un fragment con esa tag
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment != null) {
            transaction.show(fragment);
        }

        transaction.commit();
    }


}