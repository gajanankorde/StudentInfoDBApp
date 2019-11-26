package com.gajanan.example.studentinfodbapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText edtId, edtName, edtEmail, edtMobNumber;
    Button btnAdd, btnGetData, btnUpdate, btnDelete, btnViewAll, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this);

        edtId = findViewById(R.id.edt_id);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtMobNumber = findViewById(R.id.edt_mobNumber);

        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        btnUpdate = findViewById(R.id.btn_update);
        btnGetData = findViewById(R.id.btn_view);
        btnViewAll = findViewById(R.id.btn_viewAll);

        btnClear = findViewById(R.id.btn_clear);

        edtName.requestFocus();

        addData();
        getData();
        viewAll();
        updateData();
        deleteData();

        clearData();


    }

    public void addData(){

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name= edtName.getText().toString();
                String email= edtEmail.getText().toString();
                String mobNumber= edtMobNumber.getText().toString();

                if (name.isEmpty() || email.isEmpty() || mobNumber.isEmpty()){

                    Toast.makeText(MainActivity.this, "Please fill out all details", Toast.LENGTH_LONG).show();
                }
                else {

                    if (!myDB.checkUser(email)){

                        boolean isInserted = myDB.insertData(name,email,mobNumber);
                        if (isInserted == true){
                            Toast.makeText(MainActivity.this, "Data Inserted Successfully..", Toast.LENGTH_SHORT).show();
                            clear();
                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(MainActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });
    }
    public void getData(){

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = edtId.getText().toString();
                String email=edtEmail.getText().toString();
                String name="";
                String mobNumber="";


                if (!(email.isEmpty() && id.equals(String.valueOf("")))){

                    if (myDB.checkUser(email)){

                        Cursor cursor = myDB.getDataUsingEmail(email);

                        if (cursor.moveToNext()){

                            id=cursor.getString(0);
                            name=cursor.getString(1);
                            email=cursor.getString(2);
                            mobNumber=cursor.getString(3);
                        }

                        edtId.setText(id);
                        edtName.setText(name);
                        edtEmail.setText(email);
                        edtMobNumber.setText(mobNumber);


                    }
                    else if (myDB.checkUserId(id)){

                        Cursor cursor = myDB.getData(id);

                        if (cursor.moveToNext()){

                            id=cursor.getString(0);
                            name=cursor.getString(1);
                            email=cursor.getString(2);
                            mobNumber=cursor.getString(3);
                        }

                        edtId.setText(id);
                        edtName.setText(name);
                        edtEmail.setText(email);
                        edtMobNumber.setText(mobNumber);
                    }

                    else {
                        Toast.makeText(MainActivity.this, "User Does not Exists", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (id.isEmpty()) {
                        edtId.setError("Enter ID");
                        edtId.requestFocus();
                        return;
                    }
                    else {
                        edtEmail.setError("Enter email");
                        edtEmail.requestFocus();
                        return;
                    }
                }

            }
        });
    }
    public void viewAll(){

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = myDB.getAllData();

                if (cursor.getCount() == 0){
                    showMessage("Error", "Nothing found in DB");
                    return;
                }

                StringBuffer buffer = new StringBuffer();

                while (cursor.moveToNext()){
                    buffer.append("ID: "+cursor.getString(0)+"\n");
                    buffer.append("Name: "+cursor.getString(1)+"\n");
                    buffer.append("Email: "+cursor.getString(2)+"\n");
                    buffer.append("Mobile Number: "+cursor.getString(3)+"\n\n");
                }
                showMessage("All data", buffer.toString());

            }
        });


    }
    public void updateData(){

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id= edtId.getText().toString();
                String name= edtName.getText().toString();
                String email= edtEmail.getText().toString();
                String mobNumber= edtMobNumber.getText().toString();

                if (id.isEmpty() || name.isEmpty() || email.isEmpty() || mobNumber.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill out all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer updateRow = myDB.updateData(id, name, email, mobNumber);

                    if (updateRow > 0){
                        Toast.makeText(MainActivity.this, "Data Updated successfully", Toast.LENGTH_SHORT).show();
                        clear();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid id", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }
    public void deleteData(){

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id= edtId.getText().toString();

                if (id.isEmpty()){
                    edtId.setError("Please Enter ID");
                    return;
                }
                else{

                    Integer deletedRow = myDB.deleteData(edtId.getText().toString());

                    if (deletedRow > 0){
                        Toast.makeText(MainActivity.this, "Data Deleted Successfully..", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    private void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clear(){
        edtId.setText("");
        edtName.setText("");
        edtEmail.setText("");
        edtMobNumber.setText("");
    }

    public void clearData(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear();

            }
        });
    }

}
