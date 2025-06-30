package apps.lustven.fuelcalculator;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ActivityOpciones extends AppCompatActivity implements AdapterView.OnClickListener{

    private LinearLayout linearCompartir,linearValorar,linearContactar,linearApps,linearPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        linearCompartir = (LinearLayout) findViewById(R.id.linearCompartir);
        linearValorar = (LinearLayout) findViewById(R.id.linearValorar);
        linearContactar = (LinearLayout) findViewById(R.id.linearContactar);
        linearApps = (LinearLayout) findViewById(R.id.linearApps);
        linearPrivacy = (LinearLayout) findViewById(R.id.linearPrivacy);

        linearCompartir.setOnClickListener(this);
        linearValorar.setOnClickListener(this);
        linearContactar.setOnClickListener(this);
        linearApps.setOnClickListener(this);
        linearPrivacy.setOnClickListener(this);



    }



    @Override
    public void onClick(View view) {
        int id = view.getId();

       if(id == R.id.linearCompartir){
           abrirCompartir();
       }else if(id == R.id.linearValorar){
           abrirValorar();
       }else if(id == R.id.linearApps){
           abrirMoreApps();
       }else if(id == R.id.linearContactar){
           abrirContactar();
       }else if(id == R.id.linearPrivacy){
           abrirPrivacy();
       }
    }

    private void abrirPrivacy() {


        //AÑADIR LINK POLÍTICAS DE PRIVACIDAD
        Intent myLink = new Intent(Intent.ACTION_VIEW);
        myLink.setData(Uri.parse("https://docs.google.com/document/d/1-hf0dHABamlR9sA3Nk4vukxUdasLaWDii-obj68KJk8/edit?usp=sharing"));
        startActivity(myLink);
    }

    private void abrirContactar() {

        // Dirección de email
        String email = "lktmapps@gmail.com";
        // Asunto y cuerpo del correo
        String subject = "";
        String body = "";

        // Configura el Intent para abrir la aplicación de correo
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));// Solo muestra apps de correo
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);


           startActivity(intent);



    }

    private void abrirValorar() {

        //AÑADIR LINK GOOGLE O SAMSUNG
        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.samsung.com/appquery/appDetail.as?appId=apps.lustven.fuelcalculator")));
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.samsung.com/appquery/appDetail.as?appId=apps.lustven.fuelcalculator")));
        }

    }

    private void abrirMoreApps() {


            // Infla el diseño del diálogo personalizado
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_more_apps, null);

            // Crear el diálogo y configurar el layout personalizado
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            // Referencias a los botones en el diseño personalizado
            MaterialButton buttonPositive = dialogView.findViewById(R.id.btnAceptar);
            MaterialButton buttonNegative = dialogView.findViewById(R.id.btnCancelar);

            // Configura el evento de clic del botón "Aceptar"
            buttonPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.samsung.com/appquery/appDetail.as?appId=apps.lustven.fuelcalculator")));
                    }catch (ActivityNotFoundException e){
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.samsung.com/appquery/appDetail.as?appId=apps.lustven.fuelcalculator")));
                    }

                }
            });

            // Configura el evento de clic del botón "Cancelar"
            buttonNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acción cuando se presiona el botón "Cancelar"
                    dialog.dismiss();  // Cierra el diálogo
                }
            });

            // Mostrar el diálogo
            dialog.show();

    }

    private void abrirCompartir() {

        //AÑADIR LINK GOOGLE O SAMSUNG
        Intent compartir = new Intent(Intent.ACTION_SEND);
        compartir.setType("text/plain");
        String shareBody = "https://apps.samsung.com/appquery/appDetail.as?appId=apps.lustven.fuelcalculator";
        String shareSub = "Fuel Calcultor";
        compartir.putExtra(Intent.EXTRA_SUBJECT,shareSub);
        compartir.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(compartir, "Compartir"));
    }
}
