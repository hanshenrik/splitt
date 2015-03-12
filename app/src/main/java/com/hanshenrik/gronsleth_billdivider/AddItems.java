package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class AddItems extends ActionBarActivity {
    private ArrayList<Item> items;
    private ArrayList<String> buyers;
    private ArrayList<String> dinerNames; // create custom adapter?
    private EditText itemNameInput, itemPriceInput;
    private ListView dinersListView;
    private Button addItemButton, doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        initialize();

        // get the list of diners
        Intent intent = getIntent();
        this.dinerNames = (ArrayList) intent.getSerializableExtra(AddDiner.EXTRA_DINER_NAMES);

        final ArrayAdapter dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, dinerNames);
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

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = dinersListView.getCheckedItemPositions();
                for (int i = 0; i < checked.size(); i++) {
                    int pos = checked.keyAt(i);
                    if (checked.valueAt(i)) {
                        buyers.add(dinersListAdapter.getItem(pos).toString());
                    }
                }

                addItem(itemNameInput.getText().toString(), itemPriceInput.getText().toString());
                dinersListView.clearChoices();
                dinersListAdapter.notifyDataSetChanged(); // does this change background choices for selected?
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

    private void initialize() {
        this.items = new ArrayList<>();
        this.buyers = new ArrayList<>();

        this.itemNameInput = (EditText) findViewById(R.id.item_name_input);
        this.itemPriceInput = (EditText) findViewById(R.id.item_price_input);
        this.dinersListView = (ListView) findViewById(R.id.diner_names);
        this.addItemButton = (Button) findViewById(R.id.add_item_button);
        this.doneButton = (Button) findViewById(R.id.add_item_done_button);
    }

    private void addItem(String name, String priceStr) {
        // get rid of leading and trailing whitespaces
        name = name.trim();
        double price = Double.parseDouble(priceStr);

        // don't allow empty strings to be added
        if (name.isEmpty()) {
            displayToast(getString(R.string.empty_string_error_message), Toast.LENGTH_LONG);
        }
        // TODO: validate price input
        else {
            items.add(new Item(name, price, buyers));
            displayToast("'" + name + "' " + getString(R.string.add_item_success_message_suffix), Toast.LENGTH_SHORT);
        }
    }

    // TODO: duplicate, put in util class?
    private void displayToast(CharSequence message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
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
}
