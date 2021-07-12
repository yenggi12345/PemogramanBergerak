package com.example.travelapps.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.travelapps.R;
import com.example.travelapps.database.DatabaseHelper;
import com.example.travelapps.session.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class BookHotelActivity extends AppCompatActivity {
    protected Cursor cursor;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Spinner spinNamanamaHotel, spinPilihDaraHotel, spinDewasa, spinAnak;
    SessionManager session;
    String email;
    int id_book;
    public String sNamanamaHotel, sPilihDaerahHotel, sTanggal, sDewasa, sAnak;
    int jmlDewasa, jmlAnak;
    int hargaDewasa, hargaAnak;
    int hargaTotalDewasa, hargaTotalAnak, hargaTotal;
    private EditText etTanggal;
    private DatePickerDialog dpTanggal;
    Calendar newCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_hotel);

        dbHelper = new DatabaseHelper(BookHotelActivity.this);
        db = dbHelper.getReadableDatabase();

        final String[] NamanamaHotel = {"An Hotel Jakarta", "Aryaduta Jakarta", "Merlynn park", "The Acacia Hotel", "Aloft Jakarta Hasyim"};
        final String[] PilihDaerahHotel = {"Grand Zuri Pekanbaru", "The Premiere Hotel Pekanbaru", "Sabrina Paninsula", "Hotel Dafam Pekanbaru", "Winstar Hotel"};
        final String[] dewasa = {"0", "1", "2", "3", "4", "5","6"};
        final String[] anak = {"0", "1", "2", "3", "4", "5","6"};

        spinPilihDaraHotel = findViewById(R.id.namahotelpadang);
        spinNamanamaHotel = findViewById(R.id.namahotelpadang);

        spinPilihDaraHotel = findViewById(R.id.namahotelpekanbaru);
        spinNamanamaHotel = findViewById(R.id.namahotelpekanbaru);

        spinPilihDaraHotel= findViewById(R.id.namahotelmedan);
        spinNamanamaHotel = findViewById(R.id.namahotelmedan);

        spinPilihDaraHotel = findViewById(R.id.namahotelmalang);
        spinNamanamaHotel = findViewById(R.id.namahotelmalang);

        spinPilihDaraHotel = findViewById(R.id.namahoteljakarta);
        spinNamanamaHotel = findViewById(R.id.namahoteljakarta);
        spinDewasa = findViewById(R.id.dewasa);
        spinAnak = findViewById(R.id.anak);

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, PilihDaerahHotel);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPilihDaraHotel.setAdapter(adapterAsal);

        ArrayAdapter<CharSequence> adapterNamanamaHotal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, NamanamaHotel);
        adapterNamanamaHotal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinNamanamaHotel.setAdapter(adapterNamanamaHotal);

        ArrayAdapter<CharSequence> adapterDewasa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, dewasa);
        adapterDewasa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDewasa.setAdapter(adapterDewasa);

        ArrayAdapter<CharSequence> adapterAnak = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, anak);
        adapterAnak.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAnak.setAdapter(adapterAnak);

        spinPilihDaraHotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPilihDaerahHotel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinNamanamaHotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPilihDaerahHotel = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDewasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sDewasa = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinAnak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAnak = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnBook = findViewById(R.id.book);

        etTanggal = findViewById(R.id.tanggal_berangkat);
        etTanggal.setInputType(InputType.TYPE_NULL);
        etTanggal.requestFocus();
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        setDateTimeField();

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perhitunganHarga();
                if (sPilihDaerahHotel != null && sNamanamaHotel != null && sTanggal != null && sDewasa != null) {
                    if ((sPilihDaerahHotel.equalsIgnoreCase("Pekanbaru") && sNamanamaHotel.equalsIgnoreCase("Pekanbaru"))
                            || (sPilihDaerahHotel.equalsIgnoreCase("Padang") && sNamanamaHotel.equalsIgnoreCase("Padang"))
                            || (sPilihDaerahHotel.equalsIgnoreCase("Malang") && sNamanamaHotel.equalsIgnoreCase("Malang"))
                            || (sPilihDaerahHotel.equalsIgnoreCase("Medan") && sNamanamaHotel.equalsIgnoreCase("Medan"))
                            || (sPilihDaerahHotel.equalsIgnoreCase("Jakarta") && sNamanamaHotel.equalsIgnoreCase("Jakarta"))) {
                        Toast.makeText(BookHotelActivity.this, "Pilih Daerah Hotel dan Nama - nama Hotel !", Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(BookHotelActivity.this)
                                .setTitle("Ingin booking hotel sekarang?")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            db.execSQL("INSERT INTO TB_BOOK (asal, tujuan, tanggal, dewasa, anak) VALUES ('" +
                                                    sPilihDaerahHotel + "','" +
                                                    sNamanamaHotel + "','" +
                                                    sTanggal + "','" +
                                                    sDewasa + "','" +
                                                    sAnak + "');");
                                            cursor = db.rawQuery("SELECT id_book FROM TB_BOOK ORDER BY id_book DESC", null);
                                            cursor.moveToLast();
                                            if (cursor.getCount() > 0) {
                                                cursor.moveToPosition(0);
                                                id_book = cursor.getInt(0);
                                            }
                                            db.execSQL("INSERT INTO TB_HARGA (username, id_book, harga_dewasa, harga_anak, harga_total) VALUES ('" +
                                                    email + "','" +
                                                    id_book + "','" +
                                                    hargaTotalDewasa + "','" +
                                                    hargaTotalAnak + "','" +
                                                    hargaTotal + "');");
                                            Toast.makeText(BookHotelActivity.this, "Booking berhasil", Toast.LENGTH_LONG).show();
                                            finish();
                                        } catch (Exception e) {
                                            Toast.makeText(BookHotelActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Tidak", null)
                                .create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(BookHotelActivity.this, "Mohon lengkapi data pemesanan!", Toast.LENGTH_LONG).show();
                }
            }
        });

        setupToolbar();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.tbKrl);
        toolbar.setTitle("Form Booking");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void perhitunganHarga() {
        if (sPilihDaerahHotel.equalsIgnoreCase("Pekanbaru") && sNamanamaHotel.equalsIgnoreCase("Padang")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Pekanbaru") && sNamanamaHotel.equalsIgnoreCase("Medan")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Pekanbaru") && sNamanamaHotel.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 150000;
            hargaAnak = 120000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Pekanbaru") && sNamanamaHotel.equalsIgnoreCase("Malang")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Padang") && sNamanamaHotel.equalsIgnoreCase("Pekanbaru")) {
            hargaDewasa = 100000;
            hargaAnak = 70000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Padang") && sNamanamaHotel.equalsIgnoreCase("Medan")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Padang") && sNamanamaHotel.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Padang") && sNamanamaHotel.equalsIgnoreCase("Malang")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Medan") && sNamanamaHotel.equalsIgnoreCase("Padang")) {
            hargaDewasa = 200000;
            hargaAnak = 150000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Medan") && sNamanamaHotel.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 120000;
            hargaAnak = 100000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Medan") && sNamanamaHotel.equalsIgnoreCase("Malang")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Medan") && sNamanamaHotel.equalsIgnoreCase("Pekanbaru")) {
            hargaDewasa = 280000;
            hargaAnak = 150000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Malang") && sNamanamaHotel.equalsIgnoreCase("Pekanbaru")) {
            hargaDewasa = 250000;
            hargaAnak = 140000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Malang") && sNamanamaHotel.equalsIgnoreCase("Medan")) {
            hargaDewasa = 120000;
            hargaAnak = 90000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Malang") && sNamanamaHotel.equalsIgnoreCase("Padang")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Malang") && sNamanamaHotel.equalsIgnoreCase("Jakarta")) {
            hargaDewasa = 170000;
            hargaAnak = 130000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Jakarta") && sNamanamaHotel.equalsIgnoreCase("pekanbaru")) {
            hargaDewasa = 180000;
            hargaAnak = 140000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Jakarta") && sNamanamaHotel.equalsIgnoreCase("Padang")) {
            hargaDewasa = 190000;
            hargaAnak = 160000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Jakarta") && sNamanamaHotel.equalsIgnoreCase("Medan")) {
            hargaDewasa = 80000;
            hargaAnak = 40000;
        } else if (sPilihDaerahHotel.equalsIgnoreCase("Jakarta") && sNamanamaHotel.equalsIgnoreCase("Malang")) {
            hargaDewasa = 180000;
            hargaAnak = 150000;
        }

        jmlDewasa = Integer.parseInt(sDewasa);
        jmlAnak = Integer.parseInt(sAnak);

        hargaTotalDewasa = jmlDewasa * hargaDewasa;
        hargaTotalAnak = jmlAnak * hargaAnak;
        hargaTotal = hargaTotalDewasa + hargaTotalAnak;
    }

    private void setDateTimeField() {
        etTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpTanggal.show();
            }
        });

        dpTanggal = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei",
                        "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                sTanggal = dayOfMonth + " " + bulan[monthOfYear] + " " + year;
                etTanggal.setText(sTanggal);

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
}

