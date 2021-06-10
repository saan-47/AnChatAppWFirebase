package com.hassan.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText title, genre;
    Button bt;
    Button read;
    Button signout;
    TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.editTitle);
        genre = findViewById(R.id.editGenre);
        bt = findViewById(R.id.button);
        signout = findViewById(R.id.btnsignout);
        read = findViewById(R.id.extract);
        tx = findViewById(R.id.Rdata);

        //this button is used to sign out from the app
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //this finish will help us closing the session of logged in user
                finish();
            }
        });

        //reading data from the realtime firebase database
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("movies").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Movie movie = dataSnapshot.getValue(Movie.class);
                            tx.setText(tx.getText().toString()+" , "+movie.genre);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //adding data(Movie title and Movie genre in the FirebaseData base
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentTitle = title.getText().toString();
                String sentgenre = genre.getText().toString();
                FirebaseDatabase.getInstance().getReference("movies").push().setValue(new Movie(sentTitle,sentgenre)).addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Successfully inserted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "UnSuccessfully!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}