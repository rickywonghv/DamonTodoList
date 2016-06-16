package com.damonw.nodejs.todolist;

import android.content.Intent;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    public String db_name = "token";
    public String table_name = "token"; //table name
    sqldb helper = new sqldb(MainActivity.this, db_name);
    private TextView tokenabc;
    public static String gettoken;
    private ListView listV;
    ArrayList<String> items = new ArrayList<String>();
    List<todoitems> todo_list = new ArrayList<todoitems>();
    private MyAdapter adapter;
    public static String todourl="http://todo.damonwong.xyz/api";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, Add.class);
                startActivity(loginIntent);
            }
        });
        db = helper.getWritableDatabase();
        listV=(ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ckdb();

        listitem();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ckdb();
        listitem();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ckdb();
        listitem();
    }

    public void ckdb(){
        try{
            SQLiteDatabase  dbe = SQLiteDatabase.openDatabase("/data/data/com.damonw.nodejs.todolist/databases/token.db", null, 0);

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

    public void listitem(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = todourl+"/list/"+gettoken;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            todo_list.clear();
                            JSONArray items=response.getJSONArray("items");
                            for(int i=0; i < items.length() ; i++) {
                                JSONObject json_data = items.getJSONObject(i);
                                String id=json_data.getString("_id");
                                String title=json_data.getString("title");
                                String cat=json_data.getString("cat");
                                todo_list.add(new todoitems(title,id,cat));
                                createdb(id,title);
                            }
                            adapter = new MyAdapter(MainActivity.this,todo_list);
                            listV.setAdapter(adapter);

                            listV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position,long id) {
                                    TextView textmsgid=(TextView)view.findViewById(R.id.todoid);
                                    String textid=textmsgid.getText().toString();
                                    del(textid);
                                    return false;
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", response);
                    }
                }
        );
        queue.add(getRequest);
    }

    private void del(String textid){

        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = todourl+"/del/"+textid+"/"+gettoken;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                Toast.makeText(MainActivity.this,"Deleted", Toast.LENGTH_LONG).show();
                                listitem();
                            }else{
                                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", response);
                    }
                }
        );
        queue.add(getRequest);
    }

    private void createdb(String id,String title){
        SQLiteDatabase db;

        db = openOrCreateDatabase( "todo.db",SQLiteDatabase.CREATE_IF_NECESSARY , null);
        try {
            final String CREATE_items_TABLE_CONTAIN = "create table if not exists items(id VARCHAR PRIMARY KEY NOT NULL,title VARCHAR)";
            db.execSQL(CREATE_items_TABLE_CONTAIN);

            String sql ="INSERT or replace INTO items VALUES('"+id+"','"+title+"')" ;
            db.execSQL(sql);
            db.close();
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "ERROR " + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("Error", e.toString());
        }
    }

    public boolean deleteTitle(String name){ //delete sqlite table, insert the Table name
        return db.delete(table_name, "id" + "=" + name, null) > 0;

    }

    public void logout(){
        deleteTitle("token");
        this.deleteDatabase("/data/data/com.damonw.nodejs.todolist/databases/token.db");
        Intent loginIntent = new Intent(this, Login.class);
        startActivity(loginIntent);
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
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
