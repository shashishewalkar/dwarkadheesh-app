package test.e_commerceapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Menu navigationMenu;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    DatabaseReference databaseReference;
    TextView navHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set toolbar as actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // get instance of navigation view
        navigationView = findViewById(R.id.nav_view);

        // get nav view header title
        navHeaderTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_header_title);

        // get drawer instance
        drawerLayout = findViewById(R.id.drawer_layout);

        // onClick of navigation items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // set item as selected to persist highlight
                item.setChecked(true);
                // close drawer when item is tapped
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();
                        break;
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        break;
                    case R.id.nav_register:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new RegisterFragment()).commit();
                        break;
                    case R.id.nav_sign_in:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new SignInFragment()).commit();
                        break;
                    case R.id.nav_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new CartFragment()).commit();
                        break;
                    case R.id.nav_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new AccountFragment()).commit();
                        break;
                }

                return true;
            }
        });

        // Get instance of firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // fetch navigation menu
        navigationMenu = navigationView.getMenu();

        // authStateListner to login or logout
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    // user logged out
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

                    navigationMenu.setGroupVisible(R.id.login_group, false);
                    navigationMenu.setGroupVisible(R.id.logout_group, true);

                    // set name in action bar
                    navHeaderTitle.setText("Welcome, Guest User");
                } else {
                    // user logged in
                    Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();

                    navigationMenu.setGroupVisible(R.id.login_group, true);
                    navigationMenu.setGroupVisible(R.id.logout_group, false);

                    // set name in action bar
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                String name = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child("name").getValue().toString();
                                navHeaderTitle.setText("Welcome, " + name);
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                // display home page
                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();
            }
        });

        // display home page
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HomeFragment()).commit();
    }

    // open menu when hamburger is tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
