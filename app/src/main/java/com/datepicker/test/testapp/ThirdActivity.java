package com.datepicker.test.testapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

public class ThirdActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextWeb;

    private ImageButton imageBtnPhone;
    private ImageButton imageBtnWeb;
    private ImageButton imageBtnPhoneContacts;
    private ImageButton imageBtnMail;
    private ImageButton imageBtnCamera;

    private final int PHONE_CALL_CODE = 100;
    private final int PICTURE_FROM_CAMERA= 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextWeb = findViewById(R.id.editTextWeb);

        imageBtnPhone = findViewById(R.id.imageButtonPhone);
        imageBtnWeb = findViewById(R.id.imageButtonWeb);
        imageBtnPhoneContacts = findViewById(R.id.imageButtonPhoneContacts);
        imageBtnMail = findViewById(R.id.imageButtonMail);
        imageBtnCamera = findViewById(R.id.imageButtonCamera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);       //Activar flecha ir atrás.
        getSupportActionBar().setDisplayShowHomeEnabled(true);       //Vamos a forzar y cambiar icono en el Action Bar
        getSupportActionBar().setIcon(R.mipmap.ic_mariposa);         //Para que funcione, debemos decirle cuál es el icono


        //---> BOTÓN PARA LA LLAMADA (sin permisos)
    /*  Dependiendo del tipo de aplicación que queremos, podemos ahorrarnos el tema de los
        permisos y que llame directamente de la aplicación. Esto de la siguiente manera:

        imageBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recogemos el número de tlf ingresado
                String phoneNumber = editTextPhone.getText().toString();

                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intentPhone);
                }    else
                    Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_LONG).show();
            }
        });
    */


        //---> BOTÓN PARA LA LLAMADA (con permisos)

        imageBtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recogemos el número de tlf ingresado
                String phoneNumber = editTextPhone.getText().toString();

                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    //Comprobamos la versión actual de android en el que estamos corriendo

                    //Si la versión actual es superior a la version 6.0 (Marshmallow)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        //Comprobamos si el usuario ha aceptado, denegado o si nunca se le ha pedido el permiso
                        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                            //Ha aceptado
                            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                //Si el permiso es distinto de cero, entonces fue negado.
                                return;
                            }
                            startActivity(intentCall);
                        } else{
                            //Ha denegado o es la primera vez que se le pregunta
                            if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {

                                /* Si, anteriormente, no se le ha preguntado al usuario si concede el
                                permiso, quiere decir que no lo ha rechazado y que no necesita una
                                explicación acerca de la solicitud, esto para aclarar dudas al
                                respecto.
                                    requestPermissions -> método asíncrono, no devuelve un resultado en
                                tiempo instantáneo. Necesita el método onRequestPermissionsResult.
                                Como es asíncrono, necesita un código(PHONE_CALL_CODE) para identificar
                                este caso de uso al momento de ejecutarlo
                                    Se pueden comprobar varios permisos pero, como en este caso, solo
                                queremos comprobar uno, lo pasamos de una vez (Manifest.permission.CALL_PHONE)*/

                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);

                            }else{
                                // El usuario ha negado el permiso  y posiblemente necesite una explicación.
                                Toast.makeText(ThirdActivity.this, "Please, enable the request permission", Toast.LENGTH_SHORT).show();

                                //Muestra la pantalla de detalles
                                Intent intentDetails = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                                /*Se ocultará al usuario cualquier actividad hasta que
                                establezca una acción(permitir la solicitud o desinstalar la app)*/
                                intentDetails.addCategory(Intent.CATEGORY_DEFAULT);

                                //Devuelve el nombre del paquete de esta aplicación.
                                intentDetails.setData(Uri.parse("package:" + getPackageName()));

                                /*Si se establece, esta actividad se convertirá en el inicio de una nueva tarea en esta
                                pila de historial. La tarea actual se colocará en la parte delantera de la pantalla con
                                 el estado en el que estuvo por última vez*/
                                intentDetails.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                //Tan pronto como el usuario navega fuera de ella, la actividad finaliza.
                                intentDetails.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                                //La  actividad  nueva no se mantiene en la lista de actividades iniciadas recientemente
                                intentDetails.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                                startActivity(intentDetails);
                            }
                        }
                    } else
                        OlderVersion(phoneNumber);
                } else
                    Toast.makeText(ThirdActivity.this, "Insert a phone number", Toast.LENGTH_LONG).show();
            }

            private void OlderVersion(String phoneNumber) {
                //Hacemos el intent implícito. Pasamos acción y un uri, recogiendo la variable
                Intent iCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

                //El ACTION_CALL nos pide comprobar si el método de llamado de tlf está declarado en el Manifest (ver Manifest)
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(iCall);
                }else
                    Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_LONG).show();
            }
        });


        //---> BOTÓN PARA LA DIRECCIÓN WEB
        imageBtnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = editTextWeb.getText().toString();

                if(url != null && !url.isEmpty()){
                    Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
                    startActivity(intentWeb);

                    /*Otra forma de hacerlo sería:
                    Intent intentWeb = new Intent();
                    intentWeb.setAction(Intent.ACTION_VIEW);
                    intentWeb.setData(Uri.parse("http://"+url));
                    startActivity(intentWeb);
                    */
                }
            }
        });


        //---> BOTÓN PARA LOS CONTACOS DE TELÉFONO
        imageBtnPhoneContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentContacts = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people"));
                startActivity(intentContacts);
            }
        });


        //--->BOTÓN PARA EMAIL
        imageBtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "andreinaog13@gmail.com";

                if(email != null && !email.isEmpty()) {

                    //Email rápido
                /*Intent intentMailTo = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+ email));
                startActivity(intentMailTo);*/

                    //Email completo
                    Intent intentMail = new Intent(Intent.ACTION_SEND, Uri.parse("mailto"+ email));
                    intentMail.setType("text/plain");
                    intentMail.putExtra(EXTRA_SUBJECT, "Mail's title");
                    intentMail.putExtra(EXTRA_TEXT, "Hi there, I love MyForm App, but... ");
                    intentMail.putExtra(EXTRA_EMAIL, new String[]{"andreinaog13@gmail.com", "zangetsujm@gmail.com"});
                    //Preguntamos con cuál aplicación abrir
                    startActivity(Intent.createChooser(intentMail, "Elige cliente de correo"));

                    /* O simplemente(Email rápido):
                    startActivity(intentMail);
                    */
                }else
                    Toast.makeText(ThirdActivity.this, "No se puede acceder a su correo", Toast.LENGTH_LONG).show();


            }
        });


        //----> BOTÓN PARA LA CÁMARA
        imageBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCamara = new Intent("android.media.action.IMAGE_CAPTURE");

                //Si queremos traer una foto
                startActivityForResult(intentCamara, PICTURE_FROM_CAMERA);

                /*Si no, simplemente:
                startActivity(intentCamara);
                */
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case PICTURE_FROM_CAMERA:

                if(resultCode == Activity.RESULT_OK){
                    String result = data.toUri(0);
                    Toast.makeText(ThirdActivity.this, "Result: "+result, Toast.LENGTH_LONG).show();

                }else
                    Toast.makeText(ThirdActivity.this, "There was an error with the picture, try again", Toast.LENGTH_LONG).show();

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Comprobamos que el usuario haya aceptado el permiso
    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //Sobrecarga de método para permisos del tlf
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Estamos en el caso del teléfono
        switch (requestCode) {
            case PHONE_CALL_CODE:

                String permission = permissions[0];      // Manifest.permission.CALL_PHONE
                int result = grantResults[0];            // Resultado de la solicitud que se le ha hecho al usuario

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    // Comprobar si ha sido aceptado o denegado la petición de permiso
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        // Concedió su permiso
                        String phoneNumb = editTextPhone.getText().toString();    //Recogemos el teléfono
                        Intent intentC = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumb));
                        if (ActivityCompat.checkSelfPermission(ThirdActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
                        startActivity(intentC);
                    }
                    else {
                        // No concendió su permiso
                        Toast.makeText(ThirdActivity.this, "You declined the access", Toast.LENGTH_SHORT).show();
                    }
                }break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
