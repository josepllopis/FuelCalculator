package apps.lustven.fuelcalculator;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Application application = getApplication();
        ((MyApplication)application).showAdIfAvailable(this, new MyApplication.OnShowAdCompleteListener() {
            @Override
            public void onShowAdComplete() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivityMain();
                    }
                },2000);
            }

            @Override
            public void onShowAdDismiss() {
                startActivityMain();
            }
        });




    }

    private void startActivityMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
