package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class CompleteBill extends ActionBarActivity {
    private ArrayList<Item> items;
    private ListView itemListView;
    private TextView totalCostView;
    private double totalCost = 0;
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_bill);
        initialize();

        // get the items
        Intent intent = getIntent();
        items = (ArrayList<Item>) intent.getSerializableExtra(AddDiners.EXTRA_ITEMS);

        // calculate total cost
        for (Item item : items) {
            totalCost += item.getPrice();
        }
        totalCostView.setText(getString(R.string.total_cost_prefix) + DF.format(totalCost));

        final ArrayAdapter itemListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemListView.setAdapter(itemListAdapter);


        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String itemName = parent.getItemAtPosition(pos).toString().split(" ")[0];

                for (Item item : items) {
                    if (item.getName().equals(itemName)) {
                        displayToast(getString(R.string.complete_bill_buyers_prefix) + " " +
                                Arrays.toString(item.getBuyers()).replace("[", "").replace("]", ""),
                                Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    private void initialize() {
        this.itemListView = (ListView) findViewById(R.id.items);
        this.totalCostView =(TextView) findViewById(R.id.total_cost);
    }

    private void displayToast(CharSequence message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complete_bill, menu);
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
