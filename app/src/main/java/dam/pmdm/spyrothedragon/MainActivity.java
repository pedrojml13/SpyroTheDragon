package dam.pmdm.spyrothedragon;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GuideBinding guideBinding;
    NavController navController = null;
    MediaPlayer backgroundSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        guideBinding = binding.includeLayout;
        setContentView(binding.getRoot());

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        // Inicializar la guía
        initializeGuide();

    }

    private void initializeGuide() {

        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isGuideShown = preferences.getBoolean("isGuideShown", false);

        if (!isGuideShown) {
            showGuide();

            // Guardamos en SharedPreferences que la guía ya se mostró
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isGuideShown", true);
            editor.apply();
        }
    }

    private void showGuide(){
        guideBinding.guide.setVisibility(View.VISIBLE);
        guideBinding.guideStart.setVisibility(View.VISIBLE);
        MediaPlayer paso_guia = MediaPlayer.create(getApplicationContext(), R.raw.paso_guia);
        MediaPlayer fin_guia = MediaPlayer.create(getApplicationContext(), R.raw.fin_guia);

        backgroundSound = MediaPlayer.create(MainActivity.this, R.raw.the_leyend_of_spyro);
        backgroundSound.start();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        View view = findViewById(R.id.nav_characters);

        guideBinding.saltarGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Detener los sonidos
                backgroundSound.stop();

                // Ocultar todos los elementos de la guía
                guideBinding.guide.setVisibility(View.GONE);
                guideBinding.saltarGuia.setVisibility(View.GONE);


            }
        });

        guideBinding.comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideBinding.guideStart.setVisibility(View.GONE);

                guideBinding.guideFirstTab.setVisibility(View.VISIBLE);

                paso_guia.start();

                guideBinding.pulse.setX(view.getX()+100);
                guideBinding.pulse.setY(height - 320);

                guideBinding.pulse.setVisibility(View.VISIBLE);

                startPulseAnimation();
                startExpandAnimation(guideBinding.tab1);
            }
        });

        guideBinding.guideFirstTab.setOnClickListener(new View.OnClickListener() {

            View view = findViewById(R.id.nav_worlds);

            @Override
            public void onClick(View v) {
                guideBinding.guideFirstTab.setVisibility(View.GONE);

                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.rotate_slide_in_right)
                        .build();

                navController.navigate(R.id.navigation_worlds, null, navOptions);
                binding.navView.setSelectedItemId(R.id.navigation_worlds);

                paso_guia.start();


                guideBinding.guideSecondTab.setVisibility(View.VISIBLE);


                guideBinding.pulse.setX(view.getX() + 100);
                guideBinding.pulse.setY(height-320);
                guideBinding.pulse.setVisibility(View.VISIBLE);

                startPulseAnimation();
                startExpandAnimation(guideBinding.tab2);
            }
        });

        guideBinding.guideSecondTab.setOnClickListener(new View.OnClickListener() {

            View view = findViewById(R.id.nav_collectibles);

            @Override
            public void onClick(View v) {
                guideBinding.guideSecondTab.setVisibility(View.GONE);
                //guideBinding.guideSecondTab.setTranslationX(Resources.getSystem().getDisplayMetrics().widthPixels/3f - guideBinding.guideFirstTab.getWidth());

                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.rotate_slide_in_right)
                        .build();

                paso_guia.start();

                navController.navigate(R.id.navigation_collectibles, null, navOptions);

                guideBinding.guideThirdTab.setVisibility(View.VISIBLE);

                guideBinding.pulse.setX(view.getX() + 100);
                guideBinding.pulse.setY(height-320);

                guideBinding.pulse.setVisibility(View.VISIBLE);

                startPulseAnimation();
                startExpandAnimation(guideBinding.tab3);
            }
        });

        guideBinding.guideThirdTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideBinding.guideThirdTab.setVisibility(View.GONE);
                guideBinding.guideInformationIcon.setVisibility(View.VISIBLE);

                paso_guia.start();

                showInfoDialog();

                /*guideBinding.pulse.setX(width - guideBinding.pulse.getWidth());
                guideBinding.pulse.setY(-80);

                guideBinding.pulse.setVisibility(View.VISIBLE);

                startPulseAnimation();

                 */
                startExpandAnimation(guideBinding.informationIcon);
            }
        });

        guideBinding.guideInformationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideBinding.guideInformationIcon.setVisibility(View.GONE);
                guideBinding.guideEnd.setVisibility(View.VISIBLE);
            }
        });

        guideBinding.finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fin_guia.start();
                guideBinding.guide.setVisibility(View.GONE);

                backgroundSound.stop();

            }
        });

    }

    // Método para iniciar la animación correctamente cada vez que se llame
    private void startPulseAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(guideBinding.pulse, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(guideBinding.pulse, "scaleY", 1f, 0.5f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(guideBinding.pulse, "alpha", 0f, 1f);

        scaleX.setRepeatCount(3);
        scaleY.setRepeatCount(3);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY).before(fadeIn);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    // Método para iniciar la animación de agrandamiento correctamente cada vez que se llame
    private void startExpandAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0.7f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, fadeIn);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }




    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else
        if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }



}