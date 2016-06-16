package com.damonw.nodejs.todolist;

import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Login extends AppCompatActivity {
    EditText userInp,pwdInp;
    TextView loginMsg;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userInp=(EditText) findViewById(R.id.userInp);
        pwdInp=(EditText) findViewById(R.id.pwdInp);
        loginBtn=(Button) findViewById(R.id.loginBtn);
        loginMsg=(TextView) findViewById(R.id.loginMsg);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               String user= userInp.getText().toString();
               String pwd= pwdInp.getText().toString();
               if(ck(user,pwd)){
                   login();
                   //loginMsg.setText("success");
               }else{
                   loginMsg.setText(R.string.login_fail);
               }
            }
        });
    }

    private boolean ck(String user, String pwd){
        if(user.isEmpty()||pwd.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    private void login(){
        final String url="http://nodejs.damonw.com/api/auth";
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
                                String token=obj.getString("token");
                                storageTk(token);

                            }else{
                                loginMsg.setText("Login Fail");
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
                params.put("username", userInp.getText().toString());
                params.put("pwd", pwdInp.getText().toString());
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void storageTk(String token){
        if(token.isEmpty()){
            loginMsg.setText("Login Fail");
        }else{
            loginMsg.setText("Login Success");
            SQLiteDatabase db;
            db = openOrCreateDatabase( "/data/data/com.damonw.nodejs.todolist/databases/token.db",SQLiteDatabase.CREATE_IF_NECESSARY , null);
            try {
                final String CREATE_TABLE_CONTAIN = "create table if not exists token(id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,token VARCHAR)";
                db.execSQL(CREATE_TABLE_CONTAIN);
                //Toast.makeText(LoginActivity.this, "table created ", Toast.LENGTH_LONG).show();
                //String delsql="delete from token";
                //db.execSQL(delsql);
                String sql ="INSERT or replace INTO token VALUES(1,'"+token+"')" ;
                db.execSQL(sql);
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
            }
            catch (Exception e) {
                Toast.makeText(Login.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

}
