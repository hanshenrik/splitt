package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class AddItems extends ActionBarActivity {
    private HashMap<String, ArrayList<Diner>> items;
    private ArrayList<Diner> buyers = new ArrayList<>();
    private ArrayList<Diner> diners = new ArrayList<>(); // TODO: get this from somewhere, temp dummy variable
    private ArrayList<String> dinerNames; // create custom adapter
    private EditText itemNameInput, itemPriceInput;
    private ListView dinersListView;
    private Button addItemButton, doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        // get the list of diners
        Intent intent = getIntent();
        this.dinerNames = (ArrayList) intent.getSerializableExtra(AddDiner.EXTRA_DINER_NAMES);

        this.itemNameInput = (EditText) findViewById(R.id.item_name_input);
        this.itemPriceInput = (EditText) findViewById(R.id.item_price_input);
        this.dinersListView = (ListView) findViewById(R.id.diner_names);
        this.addItemButton = (Button) findViewById(R.id.add_item_button);
        this.doneButton = (Button) findViewById(R.id.add_item_done_button);

        // dummy data
        diners.add(new Diner("hans henrik"));
        diners.add(new Diner("tom"));
        diners.add(new Diner("helena"));

        final ArrayAdapter dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dinerNames);
        dinersListView.setAdapter(dinersListAdapter);

        itemNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // set focus to the price input
                    itemPriceInput.requestFocus();
                }
                return true;
            }
        });

        itemPriceInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // hide keyboard when clicking 'Done' by returning false
                return false;
            }
        });

        dinersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String buyer = parent.getItemAtPosition(pos).toString();
                for (Diner diner : diners) {
                    if (diner.getName().equals(buyer)) {
                        buyers.add(diner);
                    }
                }
                view.setBackgroundColor(Color.BLUE);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(itemNameInput.getText().toString(), itemPriceInput.getText().toString());
                dinersListAdapter.notifyDataSetChanged();
                itemNameInput.setText("");
                itemPriceInput.setText("");
                buyers.clear();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(AddDiner.EXTRA_NEW_ITEMS, items);
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }

    private void addItem(String name, String priceStr/*, ArrayList<Diner> buyers*/) {
        // get rid of leading and trailing whitespaces
        name = name.trim();
        double price = Double.parseDouble(priceStr);

        // don't allow empty strings to be added
        if (name.isEmpty()) {
            displayToast(getString(R.string.empty_string_error_message), Toast.LENGTH_LONG);
        }
        // TODO: validate price input
        else {
            // TODO: select diners from list of names and add them here.
//            for (Diner buyer : buyers) {
//                buyer.addItem(name, price, buyers.size());
//            }
//            items.put(name, buyers);
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
