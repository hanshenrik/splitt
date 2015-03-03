package com.hanshenrik.gronsleth_billdivider;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class AddItems extends ActionBarActivity {
    private HashMap<String, ArrayList<Diner>> items;
    private double totalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
    }

    private void addItem(String name, double price, ArrayList<Diner> diners) {
        // get rid of leading and trailing whitespaces
        name = name.trim();

        // don't allow empty strings to be added
        if (name.isEmpty()) {
            displayToast(getString(R.string.empty_string_error_message), Toast.LENGTH_LONG);
        }
        // TODO: validate price input
        else {
            for (Diner diner : diners) {
                diner.addItem(name, price, diners.size());
            }
            items.put(name, diners);
            displayToast("'" + name + "' " + getString(R.string.add_item_success_message_suffix), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_items, menu);
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

    private void displayToast(CharSequence message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }
}
