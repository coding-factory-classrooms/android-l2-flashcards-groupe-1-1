package com.gwesaro.mycheeseornothing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = "bleu_d_auvergne.jpg";
        String name = path.split("\\.")[0];
        int drawableResourceId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());

        Log.i("Main", "Fromages : \n\n" +
                //France
                "Bleu d'Auvergne : " + R.drawable.bleu_d_auvergne + "\n" +
                "Bouton de Culotte : " + R.drawable.bouton_de_culotte + "\n" +
                "Brie de Meaux : " + R.drawable.brie_de_meaux + "\n" +
                "Camembert : " + R.drawable.camembert + "\n" +
                "Cantal : " + R.drawable.cantal + "\n" +
                "Curé Nantais : " + R.drawable.cure_nantais + "\n" +
                "Epoisses : " + R.drawable.epoisses + "\n" +
                "Maroille : " + R.drawable.maroille + "\n" +
                "Mont d'Or : " + R.drawable.mont_dor + "\n" +
                "Munster : " + R.drawable.munster + "\n" +
                "Ossau Iraty : " + R.drawable.ossau_iraty + "\n" +
                "Pont l'Evêque : " + R.drawable.pont_leveque + "\n" +
                "Reblochon : " + R.drawable.reblochon + "\n" +
                "Roquefort : " + R.drawable.roquefort + "\n" +
                "Mimolette : " + R.drawable.mimolette + "\n" +
                // pays bas
                "Edam : " + R.drawable.edam + "\n" +
                "Etorki : " + R.drawable.etorki + "\n" +
                // Americain
                "Blue Marble Jack : " + R.drawable.blue_marble_jack + "\n" +
                "Capricious : " + R.drawable.capricious + "\n" +
                // Bresilien
                "Queijo Minas : " + R.drawable.queijo_minas + "\n" +
                "Queijo do Reino : " + R.drawable.queijo_do_reino + "\n" +
                // Italien
                "Mozzarella : " + R.drawable.mozzarella + "\n" +
                "Figliata : " + R.drawable.figliata + "\n" +
                "Ricotta : " + R.drawable.ricotta + "\n" +
                "Parmesan : " + R.drawable.parmesan + "\n" +
                // Norvegien
                "Brunost : " + R.drawable.brunost + "\n" +
                "Gammelost : " + R.drawable.gammelost + "\n" +
                "\n" +
                "British : " + R.drawable.british_flag + "\n" +
                "France : " + R.drawable.france_flag + "\n" +
                "Brésil : " + R.drawable.brasilian_flag + "\n" +
                "Norvège : " + R.drawable.norvege_flag + "\n" +
                "Italie : " + R.drawable.italian_flag + "\n" +
                "Pays-Bas : " + R.drawable.pays_bas_flag + "\n" +
                "UnitedStates : " + R.drawable.usa_flag + "\n"
        );
    }
}