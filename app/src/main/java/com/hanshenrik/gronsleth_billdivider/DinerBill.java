package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DinerBill extends ActionBarActivity {
    private HashMap<String, double[]> items;
    ArrayList<String> itemsAsList = new ArrayList<>();
    private double totalCost = 0;
    private static final DecimalFormat DF = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diner_bill);

        Intent intent = getIntent();
        String dinerName = intent.getStringExtra(AddDiners.EXTRA_DINER_BILL_TITLE);
        items = (HashMap<String, double[]>) intent.getSerializableExtra(AddDiners.EXTRA_DINER_ITEMS);
        getSupportActionBar().setTitle(dinerName + getString(R.string.diner_bill_title_suffix));

        parseItems();

        ListView itemsListView = (ListView) findViewById(R.id.diner_items);
        TextView totalCostView = (TextView) findViewById(R.id.individual_total_cost);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsAsList);
        itemsListView.setAdapter(adapter);

        totalCostView.setText(getString(R.string.total_cost_prefix) + DF.format(totalCost));
    }

    private void parseItems() {
        double[] values;
        double price;
        String fraction;
        for (String item : items.keySet()) {
            values = items.get(item);
            price = values[0] * (1.0/values[1]);
            fraction = (values[1] == 1) ? "(1)" : "(1/" + DF.format(values[1]) + ")";
            itemsAsList.add(item + " // " + fraction + " // £" + DF.format(price));

            // calculate total cost while we're at it
            totalCost += price;
        }
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
