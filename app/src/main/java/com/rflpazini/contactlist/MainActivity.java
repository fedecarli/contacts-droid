package com.rflpazini.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import com.rflpazini.contactlist.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        updateContacts();
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
                                                deleteContact(name);
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
                        showNumber(position, name);
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

        builder.setTitle(title)
                .setPositiveButton(R.string.action_add,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String name = nameDialog.getText().toString();
                                String number = phoneDialog.getText().toString();

                                addContactDb(name, number);
                                dialog.dismiss();
                                showToast(Constants.ADD_CONTACT);
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addContactDb(String name, String phone) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbInfo.DbEntry.CONT_NAME, name);
        values.put(DbInfo.DbEntry.CONT_PHONE, phone);

        db.insertWithOnConflict(DbInfo.DbEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        updateContacts();
    }

    private void deleteContact(String name) {
        Log.i("TESTE", name);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(DbInfo.DbEntry.TABLE,
                DbInfo.DbEntry.CONT_NAME + " = ?",
                new String[]{name});
        db.close();
        updateContacts();
    }

    private void updateContacts() {
        items = new ArrayList<String>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(DbInfo.DbEntry.TABLE,
                new String[]{DbInfo.DbEntry._ID, DbInfo.DbEntry.CONT_NAME,
                        DbInfo.DbEntry.CONT_PHONE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DbInfo.DbEntry.CONT_NAME);
            items.add(cursor.getString(idx));
        }

        if (itemsAdapter == null) {
            itemsAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    items);
            listView.setAdapter(itemsAdapter);
        } else {
            itemsAdapter.clear();
            itemsAdapter.addAll(items);
            itemsAdapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    private void showNumber(int position, String name) {
        int pos = position + 1;
        Log.i("MAIN", pos + ":" + name);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                DbInfo.DbEntry.TABLE + " WHERE " +
                DbInfo.DbEntry.CONT_NAME + "='" +
                name + "'", null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int indexNumber = cursor.getColumnIndex(DbInfo.DbEntry.CONT_PHONE);
                String phone = cursor.getString(indexNumber);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(phone)
                        .setTitle(name);
                AlertDialog alert = builder.create();
                alert.show();
            }
            cursor.close();
        }
    }

    protected void showToast(String type) {
        if (type.equals(Constants.ADD_CONTACT)) {
            Toast.makeText(getApplicationContext(), getString(R.string.add_contact_snack), Toast.LENGTH_SHORT).show();

        } else if (type.equals(Constants.REMOVE_CONTACT)) {
            Toast.makeText(getApplicationContext(), getString(R.string.remove_contact_snack), Toast.LENGTH_SHORT).show();
        }
    }
}
