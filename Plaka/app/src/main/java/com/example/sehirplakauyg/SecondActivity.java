package com.example.sehirplakauyg;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    TextView txtIl, txtPlakaGosterilen, txtPlakaGercek, txtSonuc;
    Button btnGeriDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        txtIl = findViewById(R.id.txtIl);
        txtPlakaGosterilen = findViewById(R.id.txtPlakaGosterilen);
        txtPlakaGercek = findViewById(R.id.txtPlakaGercek);
        txtSonuc = findViewById(R.id.txtSonuc);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        String ilAdi = getIntent().getStringExtra("ilAdi");
        int gercekPlaka = getIntent().getIntExtra("gercekPlaka", 0);
        int gosterilenPlaka = getIntent().getIntExtra("gosterilenPlaka", 0);

        txtIl.setText("Sehir: " + ilAdi);
        txtPlakaGosterilen.setText("Gosterilen Plaka: " + gosterilenPlaka);
        txtPlakaGercek.setText("Gercek Plaka: " + gercekPlaka);

        if (gercekPlaka == gosterilenPlaka) {
            txtSonuc.setText("Sonuc: Dogru Eslesme");
        } else {
            txtSonuc.setText("Sonuc: Yanlis Eslesme");
        }

        btnGeriDon.setOnClickListener(v -> finish());
    }
}