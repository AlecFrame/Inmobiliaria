package com.vertacnik.inmobiliaria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.vertacnik.inmobiliaria.databinding.ActivityMainBinding;
import com.vertacnik.inmobiliaria.request.ApiClient;
import com.vertacnik.inmobiliaria.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding b;
    private MainViewModel vm;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setSupportActionBar(b.appBarMain.toolbar); // iniciar el ToolBar

        initNavigation();
        initDrawerMenu();

        // ----- Cosas del ViewModel ----- //
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainViewModel.class);

        vm.getToastMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Cargar el encabezado del menú hamburgueza
        vm.getPropietario().observe(this, p -> {
            cambiarEncabezado(p.getNombre(), p.getApellido(), p.getDni(), p.getEmail());
        });
    }

    @SuppressLint("SetTextI18n")
    private void cambiarEncabezado(String nombre, String apellido, String dni, String email) {
        View headerView = b.navView.getHeaderView(0);

        TextView tvImageDrawer = headerView.findViewById(R.id.tvUserImageDrawer);
        TextView tvNombreDrawer = headerView.findViewById(R.id.tvNombreDrawer);
        TextView tvMailDrawer = headerView.findViewById(R.id.tvMailDrawer);
        TextView tvDatosDrawer = headerView.findViewById(R.id.tvDatosDrawer);

        tvImageDrawer.setText(nombre.charAt(0)+""+apellido.charAt(0));
        tvNombreDrawer.setText(nombre+" "+apellido);
        tvMailDrawer.setText(email);
        tvDatosDrawer.setText("PROPIETARIO - DNI "+dni);
    }
    private void initNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment == null) return;

        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.inicioFragment,
                R.id.perfilFragment,
                R.id.inmueblesFragment,
                R.id.inquilinosFragment,
                R.id.contratosFragment
        )
                .setOpenableLayout(b.drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(
                this,
                navController,
                appBarConfiguration
        );
    }
    private void initDrawerMenu() {
        b.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                cargarPropietario();
            }
        });

        b.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                showLogoutDialog();
                return true;
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) {
                b.drawerLayout.closeDrawers();
            }
            return handled;
        });
    }
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("¿Estás seguro que querés salir de la sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    logout();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();

        b.drawerLayout.closeDrawers();
    }
    private void logout() {
        ApiClient.eliminarCredenciales(getApplication());
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void cargarPropietario() {
        vm.cargarPropietario();
    }
}