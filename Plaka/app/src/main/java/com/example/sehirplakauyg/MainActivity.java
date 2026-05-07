package com.example.sehirplakauyg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ListView listeIller, listePlakalar;
    Button btnKaristir;

    String[] sehirler = {
            "Adana","Adiyaman","Afyonkarahisar","Agri","Amasya",
            "Ankara","Antalya","Artvin","Aydin","Balikesir",
            "Bilecik","Bingol","Bitlis","Bolu","Burdur",
            "Bursa","Canakkale","Cankiri","Corum","Denizli",
            "Diyarbakir","Edirne","Elazig","Erzincan","Erzurum",
            "Eskisehir","Gaziantep","Giresun","Gumushane","Hakkari",
            "Hatay","Isparta","Mersin","Istanbul","Izmir",
            "Kars","Kastamonu","Kayseri","Kirklareli","Kirsehir",
            "Kocaeli","Konya","Kutahya","Malatya","Manisa",
            "Kahramanmaras","Mardin","Mugla","Mus","Nevsehir",
            "Nigde","Ordu","Rize","Sakarya","Samsun",
            "Siirt","Sinop","Sivas","Tekirdag","Tokat",
            "Trabzon","Tunceli","Sanliurfa","Usak","Van",
            "Yozgat","Zonguldak","Aksaray","Bayburt","Karaman",
            "Kirikkale","Batman","Sirnak","Bartin","Ardahan",
            "Igdir","Yalova","Karabuk","Kilis","Osmaniye",
            "Duzce"
    };

    ArrayList<Integer> plakaListesi;
    ArrayAdapter<String> sehirAdapter;
    ArrayAdapter<Integer> plakaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listeIller = findViewById(R.id.listIller);
        listePlakalar = findViewById(R.id.listPlakalar);
        btnKaristir = findViewById(R.id.btnPlakaUret);

        sehirAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                sehirler
        );

        listeIller.setAdapter(sehirAdapter);

        plakaListesi = new ArrayList<>();

        plakalarOlustur();

        listeIller.setOnItemClickListener((parent, view, position, id) -> {

            String secilenSehir = sehirler[position];
            int dogruPlaka = position + 1;
            int ekrandaGorunen = plakaListesi.get(position);

            Intent ikinciSayfa = new Intent(MainActivity.this, SecondActivity.class);

            ikinciSayfa.putExtra("ilAdi", secilenSehir);
            ikinciSayfa.putExtra("gercekPlaka", dogruPlaka);
            ikinciSayfa.putExtra("gosterilenPlaka", ekrandaGorunen);

            startActivity(ikinciSayfa);
        });

        btnKaristir.setOnClickListener(v -> plakalarOlustur());
    }

    private void plakalarOlustur() {

        plakaListesi.clear();

        for(int i = 0; i < 81; i++){
            plakaListesi.add(i + 1);
        }

        Collections.shuffle(plakaListesi);

        plakaAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                plakaListesi
        );

        listePlakalar.setAdapter(plakaAdapter);
    }
}