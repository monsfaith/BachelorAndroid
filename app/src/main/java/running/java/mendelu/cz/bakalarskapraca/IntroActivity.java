package running.java.mendelu.cz.bakalarskapraca;

import android.Manifest;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class IntroActivity extends AppIntro {

    //https://github.com/heinrichreimer/material-intro


    //https://github.com/apl-devs/AppIntro

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.

        /*addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Cieľ", "Cieľom aplikácie Exam Helper je vytvoriť Ti vhodné podmienky pre učenie, ktoré Ti umožnia efektívnu prípravu na skúšky.", R.drawable.book1, getResources().getColor(R.color.red_400)));
        addSlide(AppIntroFragment.newInstance("Skúšky", "Pridaj si skúšky, ktoré Ťa čakajú a aplikácia Ťa upozorní na potrebnú prípravu.", R.drawable.tasks1, getResources().getColor(R.color.cyan_600)));
        addSlide(AppIntroFragment.newInstance("Študijné prestávky", "Učenie je efektívne, keď máš pravidelné študijné prestávky. Máš k dispozícii aktivity odporúčané vykonávať počas prestávok.", R.drawable.mind1, getResources().getColor(R.color.yellow_800)));
        addSlide(AppIntroFragment.newInstance("Sústredenie", "Prestávky medzi učením Ti pomáhajú zlepšiť sústredenie, znížiť stres a celkovo vplývajú dobre na tvoje zdravie.", R.drawable.target1, getResources().getColor(R.color.lime_700)));
        addSlide(AppIntroFragment.newInstance("Plány", "Primárne máš nastavený celodenný plán. Čiastkové plány sa Ti aktivujú, ak si v aplikácii vyberieš skúšku, na ktorú sa chceš pripraviť.", R.drawable.happy1, getResources().getColor(R.color.yellow_800)));
        addSlide(AppIntroFragment.newInstance("Si pripravený?", "Pridaj si skúšky, zvoľ si aktivity počas plánov a začni so svojím efektívnym plánom!", R.drawable.winner1, getResources().getColor(R.color.cyan_700)));


        // OPTIONAL METHODS
        // Override bar/separator color.

        setBarColor(Color.parseColor("#ffffff"));
        setColorDoneText(getResources().getColor(R.color.black));
        setDoneText("Hotovo");
        setSkipText("Preskočiť");
        setColorSkipButton(getResources().getColor(R.color.black));
        setSeparatorColor(Color.parseColor("#00ccff"));
        setIndicatorColor(getResources().getColor(R.color.yellow_700),getResources().getColor(R.color.black));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
       finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
