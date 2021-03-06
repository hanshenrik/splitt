package com.hanshenrik.gronsleth_billdivider;

import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AddDiners extends ActionBarActivity {

    private ArrayList<Diner> diners;
    private EditText dinerNameInput;
    private HashMap<String, double[]> dinerItems;
    private ArrayList<Item> items;
    private ArrayAdapter dinersListAdapter;
    public final static String EXTRA_DINER_BILL_TITLE = "com.hanshenrik.gronsleth_billdivider.DINER_BILL_TITLE";
    public final static String EXTRA_DINER_ITEMS =      "com.hanshenrik.gronsleth_billdivider.DINER_ITEMS";
    public final static String EXTRA_DINERS =           "com.hanshenrik.gronsleth_billdivider.DINERS";
    public final static String EXTRA_NEW_ITEMS =        "com.hanshenrik.gronsleth_billdivider.NEW_ITEMS";
    public final static String EXTRA_ITEMS =            "com.hanshenrik.gronsleth_billdivider.ITEMS";
    private final static int NEW_ITEMS_REQUEST = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_ITEMS_REQUEST) {
                ArrayList<Item> newItems = (ArrayList<Item>) data.getSerializableExtra(EXTRA_NEW_ITEMS);
                items.addAll(newItems);
                // TODO: review structure. too many nested for loops, even though data set is always small.
                if (!newItems.isEmpty()) {
                    for (Item item : newItems) {
                        for (String buyer : item.buyers) {
                            for (Diner diner : diners) {
                                if (diner.name.equals(buyer)) {
                                    diner.addItem(item.name, item.price, item.buyers.length);
                                }
                            }
                        }
                    }
                    dinersListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diners);

        this.diners = new ArrayList<>();
        this.items = new ArrayList<>();
        this.dinerNameInput = (EditText) findViewById(R.id.diner_name_input);
        ListView dinersListView = (ListView) findViewById(R.id.diner_names);
        Button addItemButton = (Button) findViewById(R.id.go_to_add_item_button);
        Button completeBillButton = (Button) findViewById(R.id.go_to_complete_bill_button);

        this.dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diners);
        dinersListView.setAdapter(dinersListAdapter);

        dinerNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addDiner(dinerNameInput.getText().toString().trim());
                    dinersListAdapter.notifyDataSetChanged();
                    dinerNameInput.setText("");
                }
                // could return false to hide keyboard when clicking 'Done', but want to keep it
                // open to make it easy to add several diners
                return true;
            }
        });

        dinersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String dinerName = parent.getItemAtPosition(pos).toString().split(" ")[0];
                for (Diner diner : diners) {
                    if (diner.name.equals(dinerName)) {
                        dinerItems = diner.items;
                    }
                }

                Intent intent = new Intent(getApplicationContext(), DinerBill.class);
                intent.putExtra(EXTRA_DINER_BILL_TITLE, dinerName);
                intent.putExtra(EXTRA_DINER_ITEMS, dinerItems);
                startActivity(intent);
            }
        });

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dinerNames = new ArrayList<>();
                for (Diner diner : diners) {
                    dinerNames.add(diner.name);
                }

                Intent intent = new Intent(getApplicationContext(), AddItems.class);
                intent.putExtra(EXTRA_DINERS, dinerNames);
                startActivityForResult(intent, NEW_ITEMS_REQUEST);
            }
        });

        completeBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CompleteBill.class);
                intent.putExtra(EXTRA_ITEMS, items);
                startActivity(intent);
            }
        });
    }

    private void addDiner(String name) {
        // don't allow empty strings to be added
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_string_error_message),
                    Toast.LENGTH_LONG).show();
        } else {
            diners.add(new Diner(name));
            Toast.makeText(getApplicationContext(), "'" + name + "' " + getString(R.string.add_diner_success_message_suffix),
                    Toast.LENGTH_SHORT).show();
        }
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
