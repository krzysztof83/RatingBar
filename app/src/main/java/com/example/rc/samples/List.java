//package com.example.rc.samples;
//
//import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//
//import java.util.ArrayList;
//
//import models.Person;
//
//public class List<P> extends AppCompatActivity {
//
//
//    SimpleCursorAdapter mAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list);
//        ListView listView=(ListView) findViewById(R.id.myListView);
//
//
//        java.util.List<Person> myPersonList = new ArrayList<>();
//        String[] nameTable=new String[myPersonList.size()];
//
//        for (int i = 0; i < myPersonList.size(); i++) {
//            nameTable[i]=myPersonList.get(i).getName();
//        }
//
//        Cursor cursor=
//        int[] toViews = {android.R.id.text1};
//
//        mAdapter = new SimpleCursorAdapter(this,R.id.myListView,c,nameTable,toViews);
//        setListAdapter(mAdapter);
//
//
////        mAdapter = new SimpleCursorAdapter(this,
////                android.R.layout.simple_list_item_1, null,
////                fromColumns, toViews, 0);
////        setListAdapter(mAdapter);
//
//    }
//}
