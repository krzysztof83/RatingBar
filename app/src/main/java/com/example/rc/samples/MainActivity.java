package com.example.rc.samples;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.rc.samples.contentProvider.PersonContentProvider;

import models.Person;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int READ_CONTACT_REQUEST = 345;
    @BindView(R.id.list)
    protected ListView listView;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private PersonAdapter adapter;

    static final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getLoaderManager().initLoader(0, null, this);
        Cursor c = getContentResolver().query(PersonContentProvider.CONTENT_URI, null, null, null, null);
        adapter = new PersonAdapter(this, c);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }

    @OnClick(R.id.addAllContactButton)
    public void importAllContact() {
        if (checkPermision()) return;
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String numbers = null;
                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                    numbers = phones.getString(phones.getColumnIndex("data1"));
                }
                addPerson(name, numbers, 0.0);
            }

        }

    }

    @OnClick(R.id.addContactButton)
    public void importContact() {
        if (checkPermision()) return;
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACT_REQUEST && grantResults[0] != -1) {
            importContact();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String numbers = null;
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            numbers = phones.getString(phones.getColumnIndex("data1"));
                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        addPerson(name, numbers, 0.0);
                    }
                }
                break;
        }
    }

    @NonNull
    private void addPerson(String name, String phone, Double rating) {
        ContentValues values = new ContentValues();
        values.put(Person.NAME, name);
        values.put(Person.PHONE, phone);
        values.put(Person.RATING, rating);
        getContentResolver().insert(PersonContentProvider.CONTENT_URI, values);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                long itemId = adapter.getItemId(info.position);

                getContentResolver().delete(
                        Uri.parse(PersonContentProvider.CONTENT_URI + "/" + itemId),
                        null,
                        null
                );
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private boolean checkPermision() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST);
            Log.i("TAG", "PERMISSIONS NEEDED");
            return true;
        }
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, PersonContentProvider.CONTENT_URI, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
