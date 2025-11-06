package com.hanlie.mobisuts_2111500077;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.hanlie.mobisuts_2111500077.ClassGlobal;
import com.hanlie.mobisuts_2111500077.MainActivity;
import com.hanlie.mobisuts_2111500077.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DaftarAkun extends AppCompatActivity {

    EditText txtNama, txtHp, txtSekolah, txtAlamat, txtTtgll;
    Button btnUpfoto, btnDaftar;

    RequestQueue requestQueue;
    StringRequest stringRequest;

    ImageView IvProfile;
    Bitmap bitmap, decoded;
    int bitmap_size = 60;
    int PICK_IMAGE_REQUEST = 1;
    public static Integer cFoto;

    private void zoomGambar(String gambar){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DaftarAkun.this);
        View mView =getLayoutInflater().inflate(R.layout.dialog_zoom, null);
        PhotoView IvProfile = mView.findViewById(R.id.IvProfile);

        if (cFoto>0 && gambar.equalsIgnoreCase("foto")){
            IvProfile.setImageDrawable(IvProfile.getDrawable());
        }

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_akun);

        txtNama = findViewById(R.id.txtNama);
        txtHp = findViewById(R.id.txtHp);
        txtSekolah = findViewById(R.id.txtSekolah);
        txtAlamat = findViewById(R.id.txtAlamat);
        txtTtgll = findViewById(R.id.txtTtgll);
        btnUpfoto = findViewById(R.id.btnUpfoto);
        btnDaftar = findViewById(R.id.btnDaftar);
        IvProfile = (ImageView) findViewById(R.id.IvProfile);
        cFoto = 0;

        IvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { zoomGambar("foto"); }
        });

        btnUpfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihGambar();
                ClassGlobal.global_gambar="foto";
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtHp.length() < 10){
                    txtHp.setError("Nomor Wa minimal 10 angka!");
                    txtHp.requestFocus();
                }
                else if(txtNama.getText().toString().trim().isEmpty()){
                    txtNama.setError("Nama ga boleh kosong yah!");
                    txtNama.requestFocus();
                }
                else if(txtSekolah.getText().toString().trim().isEmpty()){
                    txtSekolah.setError("Asal sekolah ga boleh kosong yah!");
                    txtSekolah.requestFocus();
                }
                else if(txtAlamat.getText().toString().trim().isEmpty()){
                    txtAlamat.setError("Alamat ga boleh kosong yah!");
                    txtAlamat.requestFocus();
                }
                else if (cFoto == 0) {
                    Toast.makeText(getApplicationContext(), "Harap Upload Foto dulu Bos!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    connectDB("daftar");
                }
            }
        });
    }

    private void pilihGambar(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih foto"),
                PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            if (ClassGlobal.global_gambar.equalsIgnoreCase("foto")){
                cFoto++;
            }
            Uri filepath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                setToImageView(getResizedBitmap(bitmap, 1600));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void setToImageView(Bitmap bmp){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        if(ClassGlobal.global_gambar.equalsIgnoreCase("foto")){
            decoded = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(bytes.toByteArray()));
            IvProfile.setImageBitmap(decoded);
            IvProfile.setVisibility(View.VISIBLE);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize){
        int lebar = image.getWidth();
        int tinggi = image.getHeight();
        float rasio = (float) lebar/ (float) tinggi;
        if(rasio > 1){
            lebar = maxSize;
            tinggi = (int) (lebar / rasio);
        } else {
            tinggi = maxSize;
            lebar = (int) (tinggi * rasio);
        }
        return Bitmap.createScaledBitmap(image, lebar, tinggi, true);
    }

    private void connectDB(String aksi){
        stringRequest = new StringRequest(Request.Method.POST,
                ((ClassGlobal) getApplicationContext()).getUrl() + "daftar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String pesan = jObj.getString("pesan");
                            boolean hasil = jObj.getBoolean("hasil");
                            Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
                            if (hasil && aksi.equalsIgnoreCase("daftar")) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Gagal hubungi Server incikboss! : " +
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                if(aksi.equalsIgnoreCase("daftar"))
                    param.put("aksi", "daftar");
                param.put("nama", txtNama.getText().toString().trim());
                param.put("nowa", txtHp.getText().toString().trim());
                param.put("sekolah", txtSekolah.getText().toString().trim());
                param.put("ttgll", txtTtgll.getText().toString().trim());
                param.put("alamat", txtAlamat.getText().toString().trim());
                param.put("fotodaftar", getStringImage(decoded));
                return param;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
