package com.damonw.nodejs.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Add extends AppCompatActivity {
    Button addBtn;
    EditText addTitle;
    EditText catEdit;
    String gettoken=MainActivity.gettoken;
    String todourl=MainActivity.todourl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addBtn=(Button) findViewById(R.id.addBtn);
        addTitle=(EditText) findViewById(R.id.addTitleInp);
        catEdit=(EditText) findViewById(R.id.catEdit);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ck()){
                    add();
                }
            }
        });
        ckdb();
    }

    public void ckdb(){
        try{
            SQLiteDatabase dbe = SQLiteDatabase.openDatabase("/data/data/com.damonw.nodejs.todolist/databases/token.db", null, 0);

            Cursor resultSet = dbe.rawQuery("select * from token", null);
            resultSet.moveToFirst();
            String res=resultSet.getString(1);
            if(!res.isEmpty()){
                //tokenabc.setText(res);
                gettoken=res;
            }else{
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
            }
            dbe.close();
        }catch(SQLiteException e) {

            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);

        }
    }

    private boolean ck(){
        String title=addTitle.getText().toString();
        if(title.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private void add(){
        final String url=todourl+"/add";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status=obj.getString("status");
                            String action=obj.getString("action");
                            if(status.equals("success")){
                                onBackPressed();
                            }else{

                            }

                        } catch (Throwable t) {
                            Log.e("Todo List Login", "Could not parse malformed JSON: \"" + response + "\"");
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", response);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", gettoken);
                params.put("title", addTitle.getText().toString());
                params.put("cat",catEdit.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);
    }
}
