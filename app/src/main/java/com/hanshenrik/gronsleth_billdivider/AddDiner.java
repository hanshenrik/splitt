package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

public class AddDiner extends ActionBarActivity {

    private ArrayList<Diner> diners;
    private ArrayList<String> dinerNames; // create custom adapter
    private EditText dinerNameInput;
    private ListView dinersListView;
    private Button addItemButton;
    public final static String EXTRA_DINER_BILL_TITLE = "com.hanshenrik.gronsleth_billdivider.DINER_BILL_TITLE";
    public final static String EXTRA_ITEMS = "com.hanshenrik.gronsleth_billdivider.ITEMS";
    public final static String EXTRA_DINER_NAMES = "com.hanshenrik.gronsleth_billdivider.DINER_NAMES";
    public final static String EXTRA_NEW_ITEMS = "com.hanshenrik.gronsleth_billdivider.NEW_ITEMS";
    private final static int NEW_ITEMS_REQUEST = 1;
    private HashMap<String, double[]> dinerItems;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ITEMS_REQUEST) {
                ArrayList<Item> newItems = (ArrayList<Item>) data.getSerializableExtra(EXTRA_NEW_ITEMS);
                // TODO: review structure. too many nested for loops, even though data set is always small.
                for (Item item : newItems) {
                    for (String buyer : item.getBuyers()) {
                        for (Diner diner : diners) {
                            if (diner.getName().equals(buyer)) {
                                diner.addItem(item.getName(), item.getPrice(), item.getBuyers().length);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diner);
        initialize();

        final ArrayAdapter dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dinerNames);
        dinersListView.setAdapter(dinersListAdapter);

        dinerNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addDiner(dinerNameInput.getText().toString());
                    dinersListAdapter.notifyDataSetChanged();
                    dinerNameInput.setText("");
                }
                // hide keyboard when clicking 'Done' by returning false, but don't want that for now
                return true;
            }
        });

        dinersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String dinerName = parent.getItemAtPosition(pos).toString();
                dinerItems = getCurrentDinerItems(dinerName);

                Intent intent = new Intent(getApplicationContext(), DinerBill.class);
                intent.putExtra(EXTRA_DINER_BILL_TITLE, dinerName);
                intent.putExtra(EXTRA_ITEMS, dinerItems);
                startActivity(intent);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddItems.class);
                intent.putStringArrayListExtra(EXTRA_DINER_NAMES, dinerNames);
                startActivityForResult(intent, NEW_ITEMS_REQUEST);
            }
        });
    }

    private HashMap<String, double[]> getCurrentDinerItems(String dinerName) {
        for (Diner diner : diners) {
            if (diner.getName().equals(dinerName)) {
                return diner.getItems();
            }
        }
        return null;
    }

    private void initialize() {
        this.diners = new ArrayList<>();
        this.dinerNames = new ArrayList<>();
        this.dinerNameInput = (EditText) findViewById(R.id.diner_name_input);
        this.dinersListView = (ListView) findViewById(R.id.diner_names);
        this.addItemButton = (Button) findViewById(R.id.go_to_add_item_button);
    }

    private void addDiner(String name) {
        // get rid of leading and trailing whitespaces
        name = name.trim();

        // don't allow empty strings to be added
        if (name.isEmpty()) {
            displayToast(getString(R.string.empty_string_error_message), Toast.LENGTH_LONG);
        } else {
            diners.add(new Diner(name));
            dinerNames.add(name); // ugly fix, make custom adapter
            displayToast("'" + name + "' " + getString(R.string.add_diner_success_message_suffix), Toast.LENGTH_SHORT);
        }
    }

    private void displayToast(CharSequence message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds newItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_diner, menu);
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
