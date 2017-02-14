package com.rflpazini.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rflpazini.contactlist.db.DbHelper;
import com.rflpazini.contactlist.db.DbInfo;
import com.rflpazini.contactlist.model.User;
import com.rflpazini.contactlist.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private ArrayList<String> items;
    private ArrayList<String> subItems;
    private ArrayAdapter<String> itemsAdapter;
    private DbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.listItems);

        mHelper = new DbHelper(this);


        addListeners();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact(MainActivity.this, getString(R.string.action_add_title));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addListeners() {
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int position, long id) {
                        TextView textView = (TextView) item.findViewById(android.R.id.text1);
                        final String name = textView.getText().toString();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("O contato de " + name + " será deletado")
                                .setTitle(R.string.delete_contact_title)
                                .setPositiveButton(R.string.delete,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
//                                                deleteContact(name);
                                                dialog.dismiss();
                                                showToast(Constants.REMOVE_CONTACT);
                                            }
                                        })
                                .setNegativeButton(android.R.string.cancel, null);
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    }
                }
        );

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
                        TextView textView = (TextView) item.findViewById(android.R.id.text1);
                        String name = textView.getText().toString();
                        Context context = item.getContext();
//                        showNumber(name, context, position);
                    }
                }
        );
    }

    public void addContact(Context context, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.content_dialog, null);
        builder.setView(dialogView);

        final EditText nameDialog = (EditText) dialogView.findViewById(R.id.nameDialog);
        final EditText phoneDialog = (EditText) dialogView.findViewById(R.id.phoneDialog);
        phoneDialog.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        final EditText emailDialog = (EditText) dialogView.findViewById(R.id.emailDialog);

        builder.setTitle(title)
                .setPositiveButton(R.string.action_add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String name = nameDialog.getText().toString();
                                String number = phoneDialog.getText().toString();
                                String email = emailDialog.getText().toString();

//                                addContactDb(name, number, email);
                                dialog.dismiss();
                                showToast(Constants.ADD_CONTACT);
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    protected void showToast(String type) {
        if (type.equals(Constants.ADD_CONTACT)) {
            Toast.makeText(getApplicationContext(), getString(R.string.add_contact_snack), Toast.LENGTH_SHORT).show();

        } else if (type.equals(Constants.REMOVE_CONTACT)) {
            Toast.makeText(getApplicationContext(), getString(R.string.remove_contact_snack), Toast.LENGTH_SHORT).show();
        }
    }
}
