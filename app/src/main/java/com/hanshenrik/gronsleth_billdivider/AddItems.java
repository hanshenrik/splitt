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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class AddItems extends ActionBarActivity {
    private ArrayList<Item> items;
    private ArrayList<String> buyers;
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
        ArrayList<String> diners = intent.getStringArrayListExtra(AddDiners.EXTRA_DINERS);

        final ArrayAdapter dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, diners);
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

                String name = itemNameInput.getText().toString().trim();
                String priceStr = itemPriceInput.getText().toString();
                // don't allow empty strings to be added
                if (name.isEmpty()) {
                    displayToast(getString(R.string.empty_string_error_message), Toast.LENGTH_LONG);
                }
                // don't allow price to be unspecified
                else if (priceStr.equals("")) {
                    displayToast(getString(R.string.empty_price_error_message), Toast.LENGTH_LONG);
                }
                else {
                    addItem(name, priceStr);
                    dinersListView.clearChoices();
                    dinersListAdapter.notifyDataSetChanged();
                    itemNameInput.setText("");
                    itemPriceInput.setText("");
                    buyers.clear();
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra(AddDiners.EXTRA_NEW_ITEMS, items);
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
        double price = Double.parseDouble(priceStr);
        items.add(new Item(name, price, buyers.toArray(new String[buyers.size()])));
        displayToast("'" + name + "' " + getString(R.string.add_item_success_message_suffix), Toast.LENGTH_SHORT);
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
