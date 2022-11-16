package com.gundogan.notlarm;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gundogan.notlarm.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
  }

    public class MainActivity extends AppCompatActivity {
        private NotesAdapter mAdapter;
        private List notesList = new ArrayList<>();
        private CoordinatorLayout coordinatorLayout;
        private RecyclerView recyclerView;
        private TextView noNotesView;

        private DatabaseHelper db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            coordinatorLayout = findViewById(R.id.coordinator_layout);
            recyclerView = findViewById(R.id.recycler_view);
            noNotesView = findViewById(R.id.empty_notes_view);

            db = new DatabaseHelper(this);

            notesList.addAll(db.getAllNotes());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNoteDialog(false, null, -1);
                }
            });

            mAdapter = new NotesAdapter(this, notesList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
            recyclerView.setAdapter(mAdapter);

            toggleEmptyNotes();


            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, final int position) {
                }

                @Override
                public void onLongClick(View view, int position) {
                    showActionsDialog(position);
                }
            }));
        }

        private void createNote(String note) {
            // yeni not ekleyin
            long id = db.insertNote(note);

            // veritabanına eklenen yeni notun id bilgisini alın.
            Note n = db.getNote(id);

            if (n != null) {
                // listeye yeni not ekleyin
                notesList.add(0, n);

                // listelemeyi güncelleyin
                mAdapter.notifyDataSetChanged();

                toggleEmptyNotes();
            }
        }

        private void updateNote(String note, int position) {
            Note n = notesList.get(position);
            // notun text kısmını setleyin
            n.setNote(note);

            // notu veritabanında güncelleyin
            db.updateNote(n);

            // listeyi güncelleyin
            notesList.set(position, n);
            mAdapter.notifyItemChanged(position);

            toggleEmptyNotes();
        }

        private void deleteNote(int position) {
            // veritabanından notu silin
            db.deleteNote(notesList.get(position));

            // listeden notu kaldırın
            notesList.remove(position);
            mAdapter.notifyItemRemoved(position);

            toggleEmptyNotes();
        }

        private void showActionsDialog(final int position) {
            CharSequence colors[] = new CharSequence[]{"Güncelle", "Sil"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seçeneği seçin");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        showNoteDialog(true, notesList.get(position), position);
                    } else {
                        deleteNote(position);
                    }
                }
            });
            builder.show();
        }

        private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
            View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilderUserInput.setView(view);

            final EditText inputNote = view.findViewById(R.id.note);
            TextView dialogTitle = view.findViewById(R.id.dialog_title);
            dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

            if (shouldUpdate && note != null) {
                inputNote.setText(note.getNote());
            }
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton(shouldUpdate ? "güncelle" : "kaydet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {

                        }
                    })
                    .setNegativeButton("iptal",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    dialogBox.cancel();
                                }
                            });

            final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Herhangi bir not girilmediğinde hata mesajının gösterilmesi
                    if (TextUtils.isEmpty(inputNote.getText().toString())) {
                        Toast.makeText(MainActivity.this, "Not Girin!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        alertDialog.dismiss();
                    }

                    // eğer kullanıcı notlarda güncelleme yapıyorsa
                    if (shouldUpdate && note != null) {
                        // kayıtlı notu id verisi ile güncellemek
                        updateNote(inputNote.getText().toString(), position);
                    } else {
                        // yeni not oluşturma
                        createNote(inputNote.getText().toString());
                    }
                }
            });
        }

        /**
         * Eğer kayıtlı not bulunmuyorsa layout değişikliğinin sağlanması
         */
        private void toggleEmptyNotes() {

            if (db.getNotesCount() > 0) {
                noNotesView.setVisibility(View.GONE);
            } else {
                noNotesView.setVisibility(View.VISIBLE);
            }
        }
    }



}