package com.example.crist.pictogramsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Esta en la clase principal del sistema.
 *
 * @author Cristian Gonzalez Lopez
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    private DatabaseReference mDatabaseReference;
    private EscribirFragment escribirFragment;
    private StoryFragment storyFragment;
    private BoardFragment boardFragment;
    private CrearPictoFragment crearPictoFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();//camara
        StrictMode.setVmPolicy(builder.build());

        verfyPermissions();


    }

    /**
     * Función que verifica los peemisos que tiene la aplicación
     */
    public void verfyPermissions() {

        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_NETWORK_STATE};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[2]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[3]) == PackageManager.PERMISSION_GRANTED) {
            createDB();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 50);
        }
    }

    /**
     * Funcion que crea la base de datos del sistema.
     */
    public void createDB(){


        if(!checkDataBase()) {

            if (isNetworkAvailable()) {

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.child("pictos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        DownloadBD downloadBD = new DownloadBD();
                        downloadBD.setContextMain(MainActivity.this);
                        downloadBD.execute(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("No hay conexión a internet")
                        .setMessage("Es necesario tener conexion a internet para crear la base de datos.")
                        .setCancelable(false)
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        }).show();
            }
        }else{

        }


        escribirFragment = new EscribirFragment();
        loadFragment(escribirFragment,"escribir");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Cargar fragmento en el FrameLayout de la vista principal.
     *
     * @param fragment el fragmento
     * @param TAG      el tag
     * @return el boolean
     */
    public boolean loadFragment(Fragment fragment, String TAG){
        if(fragment != null){

            getSupportFragmentManager().beginTransaction()
                    //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, fragment, TAG)
                    .addToBackStack(null)
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()){

            case R.id.men_Escribir:

                    String TAG = "escribir";

                    escribirFragment = (EscribirFragment) fragmentManager.findFragmentByTag(TAG);
                    if(escribirFragment == null) {
                        escribirFragment = new EscribirFragment();
                        return loadFragment(escribirFragment, TAG);
                    }else{
                        return loadFragment(escribirFragment, TAG);
                    }

            case R.id.men_story:


                String TAG2 = "story";

                storyFragment = (StoryFragment) fragmentManager.findFragmentByTag(TAG2);
                if(storyFragment == null)
                    storyFragment = new StoryFragment();

                return loadFragment(storyFragment, TAG2);

            case R.id.men_board:

                String TAG3 = "board";

               boardFragment = (BoardFragment) fragmentManager.findFragmentByTag(TAG3);
                if(boardFragment == null)
                    boardFragment = new BoardFragment();

                return loadFragment(boardFragment, TAG3);

            case R.id.men_addimage:

                String TAG4 = "addimage";

                crearPictoFragment = (CrearPictoFragment) fragmentManager.findFragmentByTag(TAG4);
                if(crearPictoFragment == null)
                    crearPictoFragment = new CrearPictoFragment();

                return loadFragment(crearPictoFragment, TAG4);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        verfyPermissions();
    }


    /**
     * Comprobar la existencia de la base de datos
     *
     * @return el boolean
     */
    public boolean checkDataBase() {


        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(String.valueOf(this.getDatabasePath(DatabaseHelper.DATABASE_NAME)), null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Por favor pulsa dos veces ATRAS para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /**
     * Comprueba si hay conexion a internet.
     *
     * @return the boolean
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
