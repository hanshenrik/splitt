package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

public class DinerBill extends ActionBarActivity {
    private LinkedHashMap<String, double[]> items;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diner_bill);

        // dummy, should be fetched from somewhere
        this.items = new LinkedHashMap<>();
        items.put("beer", new double[]{3, 1});
        items.put("wine btl", new double[]{11, 1 / 2});
        items.put("steak", new double[] {15, 1} );
        this.itemsListView = (ListView) findViewById(R.id.diner_items);
        // TODO: create custom adapter for LinkedHashMap!
        // ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        // itemsListView.setAdapter(adapter);


        Intent intent = getIntent();
        String dinerName = intent.getStringExtra(AddDiner.EXTRA_DINER_BILL_TITLE);
        getSupportActionBar().setTitle(dinerName + getString(R.string.title_suffix_activity_diner_bill));

        //scales = intent.getStringArrayListExtra("Scales");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diner_bill, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
