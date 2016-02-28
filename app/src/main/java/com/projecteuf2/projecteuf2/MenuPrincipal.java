package com.projecteuf2.projecteuf2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        Fragment fragment;
    static final int REQUEST_TAKE_PHOTO = 1;

        //static Firebase myFirebase = new Firebase("https://pruebaparafirebase.firebaseio.com/");

    String mCurrentPhotoPath;

    private String APP_DIRECTORY = "photoApp/";
    private String MEDIA_DIRECTORY =APP_DIRECTORY+"media/";
    private String TEMPORAL_NAME = "temporal.jpg";

    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        Firebase myFirebase = new Firebase("https://pruebaparafirebase.firebaseio.com/");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageView = (ImageView) findViewById(R.id.imageView2);

        myFirebase.child("photo_profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("DATOS_PERFIL: " + dataSnapshot);
                System.out.println("VALOR: " + dataSnapshot.getValue());
                // Bitmap fotoPerfil = (Bitmap) dataSnapshot.getValue();
                /*String fotoPerfil = dataSnapshot.getValue().toString();
                Bitmap fotografia = StringToBitMap(fotoPerfil);
                imageView.setImageBitmap(fotografia);*/
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }

    private Bitmap StringToBitMap(String encodedString) {
        try{
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean transaccion= false;


        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            fragment = new BlankFragment();
            transaccion = true;

        } else if (id == R.id.nav_slideshow) {
            

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if(transaccion){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onToFotoPerfil(View view) throws IOException {
       /* Firebase myFirebase = new Firebase("https://pruebaparafirebase.firebaseio.com/");
        dispatchTakePictureIntent();*/

        final CharSequence[] options = {"Tomar foto", "Elegir de galería", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int seleccion) {
                if (options[seleccion] == "Tomar foto") {
                    openCamera();
                } else if (options[seleccion] == "Elegir de galería") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*"); //muesra todas las imagenes
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de la imagen"), SELECT_PICTURE);
                } else if (options[seleccion] == "Cancelar") {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        /*

        //Creamos el Intent para llamar a la Camara
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //Creamos una carpeta en la memeria del terminal
        File imagesFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        if(!imagesFolder.exists()) {
            imagesFolder.mkdirs();
            System.out.println("CARPETA CREADA: "+imagesFolder);
        }
        System.out.println("LA CARPETA EXISTE: " + imagesFolder);
        //añadimos el nombre de la imagen
        String name = getCode()+".jpg";
        File image = new File(imagesFolder, name);
        Uri uriSavedImage = Uri.fromFile(image);
        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        System.out.println("IMAGEN SE GUARDADARA EN: " + uriSavedImage);
        //Lanzamos la aplicacion de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, 1);

*/
/*
        //subir a FIREBASE
        //Bitmap bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.chicken);//your image
        Bitmap bmp =  BitmapFactory.decodeFile(String.valueOf(uriSavedImage));

        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        myFirebase.child("photo_"+name).setValue(imageFile);
*/
        }

    private void openCamera() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), MEDIA_DIRECTORY);
        file.mkdirs();//crear directorio donde se guardara la imagen

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +File.separator + MEDIA_DIRECTORY +
                File.separator + TEMPORAL_NAME;

        File newFile = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        startActivityForResult(intent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if(resultCode == RESULT_OK){
                    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator +
                            MEDIA_DIRECTORY + File.separator + TEMPORAL_NAME;
                   // decodeBitmap(dir); //FALLA
                }
                break;
            case SELECT_PICTURE:
                if(resultCode==RESULT_OK){
                    Uri path = data.getData();
                    //imageView.setImageURI(path); FALLA
                }
        }
    }

    private void decodeBitmap(String dir) {
        System.out.println("GUARDA LA FOTO EN FIRABASE");
        Firebase myFirebase = new Firebase("https://pruebaparafirebase.firebaseio.com/");

        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);

      //  imageView.setImageBitmap(bitmap);  FALLA

        myFirebase.child("photo_profile").setValue(bitmap);
    }
/*private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String date = dateFormat.format(new Date());
        String photoCode = "pic_" + date;
        return photoCode;
    }*/

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        if(!storageDir.exists()){
            System.out.println("CREAR EL DIRECTORIO");
            storageDir.mkdirs();
        }else{
            System.out.println("EL DIRECTORIO YA EXISTE");
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("FOTO GUARDADA EN: "+mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
/*
    public void imageToFirebase(){

        Firebase myFirebase = new Firebase("https://pruebaparafirebase.firebaseio.com/");

        Bitmap bmp =  BitmapFactory.decodeResource(getResources(),
                R.drawable.panda);//your image
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bmp.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        myFirebase.child("photo_").setValue(imageFile);
    }*/

}
