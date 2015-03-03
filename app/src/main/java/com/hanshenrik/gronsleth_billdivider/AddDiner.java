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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddDiner extends ActionBarActivity {

    private ArrayList<String> diners;
    private EditText dinerNameInput;
    private ListView dinersListView;
    public final static String EXTRA_DINER_BILL_TITLE = "com.hanshenrik.gronsleth_billdivider.DINER_BILL_TITLE";
    public final static String EXTRA_ITEMS = "com.hanshenrik.gronsleth_billdivider.ITEMS";
    private HashMap<String, double[]> items;
//    private final static int DINER_BILL_REQUEST = 1;
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        if (resultCode == RESULT_OK) {
//            if (requestCode == DINER_BILL_REQUEST) {
//                // do we really need to do anything here? don't think we're sending any info from the individual bill back here.
//                System.out.println("requestCode == DINER_BILL_REQUEST");
//                //scales = data.getStringArrayListExtra("Scales");
//                //scaleIdx = 0;
//            }
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diner);

        this.diners = new ArrayList<>();
        this.dinerNameInput = (EditText) findViewById(R.id.diner_name_input);
        this.dinersListView = (ListView) findViewById(R.id.diner_names);


        // dummy data. maybe keep in AddItems class and fetched from there?
        this.items = new HashMap<>();
        //        item                   total price  shared between, do 1.0/this when presenting/calculating
        items.put("beer", new double[] { 3,           1 });
        items.put("wine btl", new double[]{11, 3});
        items.put("nachos", new double[]{12, 3});
        items.put("steak", new double[] {15, 1});

        final ArrayAdapter dinersListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diners);
        dinersListView.setAdapter(dinersListAdapter);

        dinerNameInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addDiner(dinerNameInput.getText().toString());
                    dinersListAdapter.notifyDataSetChanged();
                    dinerNameInput.setText("");
                }
                // hide keyboard when clicking 'Done' by returning false
                return false;
            }
        });

        dinersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(getApplicationContext(), DinerBill.class);
                intent.putExtra(EXTRA_DINER_BILL_TITLE, parent.getItemAtPosition(pos).toString());
                intent.putExtra(EXTRA_ITEMS, items);
                startActivity(intent);

                //startActivityForResult(intent, DINER_BILL_REQUEST);
                //startActivity(intent);
            }
        });

        dinersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                removeDiner(parent.getItemAtPosition(pos).toString());
                dinersListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    private void addDiner(String diner) {
        // get rid of leading and trailing whitespaces
        diner = diner.trim();

        // don't allow empty strings to be added
        if (diner.isEmpty()) {
            displayToast(getString(R.string.add_diner_error_message), Toast.LENGTH_LONG);
        } else {
            diners.add(diner);
            displayToast("'" + diner + "' " + getString(R.string.add_diner_success_message), Toast.LENGTH_SHORT);
        }
    }

    // NOT IN SPEC, but shouldn't be too hard to add 'hold long to delete' functionality
    private void removeDiner(String diner) {
        diners.remove(diner);
        displayToast("'" + diner + "' " + getString(R.string.remove_diner_success_message), Toast.LENGTH_SHORT);
    }

    private void displayToast(CharSequence message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }
}
