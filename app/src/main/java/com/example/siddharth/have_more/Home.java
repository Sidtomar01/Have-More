package com.example.siddharth.have_more;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.siddharth.have_more.Comman.Comman;
import com.example.siddharth.have_more.Interface.ItemClickListener;
import com.example.siddharth.have_more.Model.Category;

import com.example.siddharth.have_more.Model.Token;
import com.example.siddharth.have_more.viewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference Category;
    TextView textFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> searchadapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    Mydatabase2 localdb;
    SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);
  swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary,android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,android.R.color.holo_blue_dark);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Comman.isConnectedtoInternet(getBaseContext())) {
                    loadMenu();
                    //register service

                }
                else
                {
                    Toast.makeText(Home.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                if(Comman.isConnectedtoInternet(getBaseContext())) {
                    loadMenu();
                    //register service

                }
                else
                {
                    Toast.makeText(Home.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        database=FirebaseDatabase.getInstance();
        Category=database.getReference("Category");

localdb=new Mydatabase2(this);

        Paper.init(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent=new Intent(Home.this,Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        //set name for the user
        View headerView=navigationView.getHeaderView(0);
        textFullName= (TextView) headerView.findViewById(R.id.textFullName);
        textFullName.setText(Comman.currentUser.getName());

        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        updateToken(FirebaseInstanceId.getInstance().getToken());



       materialSearchBar= (MaterialSearchBar) findViewById(R.id.searchbar);
        materialSearchBar.setHint("ENTER YOUR FOOD");
       // materialSearchBar.setSpeechMode(false);
        loadSuggest();//write function to load suggest from firebase.

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {






            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text we will change suggest list

                List<String> suggest=new ArrayList<String>();
                for(String search:suggestList)//LOOP IN AC
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toString()));
                    suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                ///when seature is closed
                //restore original suggest adapter

                if(!enabled)
                {
                    recycler_menu.setAdapter(adapter);
                }


            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //search finished
                //show result

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void updateToken(String token) {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens=db.getReference("Tokens");
        Token data=new Token(token,false);
        tokens.child(Comman.currentUser.getPhone()).setValue(data);
    }

    private void startSearch(CharSequence text) {
        searchadapter=new FirebaseRecyclerAdapter<com.example.siddharth.have_more.Model.Category, MenuViewHolder>(

                com.example.siddharth.have_more.Model.Category.class,R.layout.menu_item,MenuViewHolder.class,
                Category.orderByChild("Name").equalTo(text.toString())//compare name




        ) {


            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {

                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);






                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClick) {
                        //Strat new activity
                        Intent fooddetail = new Intent(Home.this, FoodDetail.class);
                        //get categoryid and send it to the new activity

                        Toast.makeText(Home.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                        fooddetail.putExtra("FoodId", searchadapter.getRef(position).getKey());
                        startActivity(fooddetail);


                    }
                });
            }
        };
        recycler_menu.setAdapter(searchadapter);



    }

    private void loadSuggest() {
Category.orderByChild("Category").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot postsnapshot:dataSnapshot.getChildren())
        {

            Category item=postsnapshot.getValue(com.example.siddharth.have_more.Model.Category.class);
            suggestList.add(item.getName());//add name of food to suggest list
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});


    }

    private void loadMenu() {

        adapter =new FirebaseRecyclerAdapter<Category, MenuViewHolder>
                (Category.class,R.layout.menu_item,MenuViewHolder.class,Category) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, final Category model, final int position) {


                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                if(localdb.isFavourite(adapter.getRef(position).getKey()))
                {
                    viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                }


             //click to change status
                viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!localdb.isFavourite(adapter.getRef(position).getKey()))
                        {
                            localdb.addtoFavourite(adapter.getRef(position).getKey());
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(Home.this, ""+model.getName() + "was added to favourite", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localdb.removeFromFavourite(adapter.getRef(position).getKey());
                            viewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(Home.this, ""+model.getName() + "was removed from favourite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





                final Category clickItem=model;




                viewHolder.setItemClickListener(new ItemClickListener()
                {
                    @Override
                    public void onClick(View v,int position, boolean isLongClick)
                    {
                        //Strat new activity
                        Intent fooddetail=new Intent(Home.this,FoodDetail.class);
                        //get categoryid and send it to the new activity

                        Toast.makeText(Home.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        fooddetail.putExtra("FoodId",adapter.getRef(position).getKey());
                        startActivity(fooddetail);


                    }


                });






            }
        } ;
        recycler_menu.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId()==R.id.refresh)
        {
            loadMenu();
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {
            Intent intent=new Intent(Home.this,Cart.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(Home.this, OrderStatus.class);
            startActivity(intent);
        } else if(id==R.id.nav_Password){
            showChangePasswordDialog();

        } else if (id == R.id.log_out) {

           Paper.book().destroy();

            Intent intent=new Intent(Home.this,SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please Fill All Information");
        alertDialog.setIcon(R.drawable.ic_security_black_24dp);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.change_passwd_layout,null);
        final MaterialEditText edtPassword= (MaterialEditText) view.findViewById(R.id.password);
        final MaterialEditText edtnewPasswd= (MaterialEditText) view.findViewById(R.id.newpassword);

        final MaterialEditText edtRepeatpasswd= (MaterialEditText) view.findViewById(R.id.repeatpassword);


        alertDialog.setView(view);
        alertDialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //change passwd here

                final AlertDialog waitingDialog=new SpotsDialog(Home.this);
                waitingDialog.show();


                if(edtPassword.getText().toString().equals(Comman.currentUser.getPassword()))
                {
                    if(edtnewPasswd.getText().toString().equals(edtRepeatpasswd.getText().toString()))
                    {
                        Map<String, Object> PasseordUpdate=new HashMap<>();
                        PasseordUpdate.put("password",edtnewPasswd.getText().toString() );
                        //Make update
                        DatabaseReference user=FirebaseDatabase.getInstance().getReference("User");
                        user.child(Comman.currentUser.getPhone()).updateChildren(PasseordUpdate).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                Toast.makeText(Home.this, "Password is Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        waitingDialog.dismiss();

                        Toast.makeText(Home.this, "New Password Doesn't Match", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    waitingDialog.dismiss();
                    Toast.makeText(Home.this, "WRONG OLD PASSWORD!!!   :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
