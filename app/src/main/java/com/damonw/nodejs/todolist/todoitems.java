package com.damonw.nodejs.todolist;

/**
 * Created by Damon on 29/5/16.
 */
public class todoitems {
    private String name;
    private String id;
    private String cat;
    public todoitems(String name,String id,String cat) {
        this.name = name;
        this.id = id;
        this.cat=cat;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getCat(){
        return cat;
    }
    public void setCat(String cat){
        this.cat = cat;
    }

}
