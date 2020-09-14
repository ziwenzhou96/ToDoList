package comp5216.sydney.edu.au.todolist;

import org.apache.commons.io.FileUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Define variables
    private ListView listView;
    //定义数据
//    private List<ListItem> items;
    private List<ToDoItem> items;
    //定义ListItemAdapter对象
    private ListItemAdapter itemsAdapter;

    ToDoItemDB db;
    ToDoItemDao toDoItemDao;


    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int ADD_ITEM_REQUEST_CODE = 648;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference the "listView" variable to the id "lstView" in the layout
        listView = (ListView) findViewById(R.id.lstView);
        //为ListView对象赋值,取得inflater
        LayoutInflater inflater =getLayoutInflater();
        //初始化数据
        readItemsFromFile();

        // Create an instance of ToDoItemDB and ToDoItemDao
        db = ToDoItemDB.getDatabase(this.getApplication().getApplicationContext());
        toDoItemDao = db.toDoItemDao();
        readItemsFromDatabase();

        //创建自定义Adapter的对象
        itemsAdapter = new ListItemAdapter(items,inflater);
        //将布局添加到ListView中
        listView.setAdapter(itemsAdapter);

//        // Create an ArrayList of String
//        items = new ArrayList<String>();
//        items.add("item one");
//        items.add("item two");
//        // Create an adapter for the list view using Android's built-in item layout
//        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                items);
//        // Connect the listView and the adapter
//        listView.setAdapter(itemsAdapter);



        // Setup listView listeners
        setupListViewListener();
    }

    private void initData() {
        items = new ArrayList<ToDoItem>();
        ListItem item_one  = new ListItem("one");
        ListItem item_two  = new ListItem("two");
        ListItem item_three = new ListItem("three");

//        items.add(item_one);
//        items.add(item_two);
//        items.add(item_three);
    }

    public void onAddItemClick(View view) {
//        String toAddString = addItemEditText.getText().toString();
//        if (toAddString != null && toAddString.length() > 0) {
//            Date date = new Date();
//            DateFormat df1 = DateFormat.getDateInstance();
//            DateFormat df2 = DateFormat.getDateTimeInstance();
//            String dateStr = df1.format(date);
//            itemsAdapter.add(dateStr+":\t\t"+toAddString); // Add text to list view adapter
//            addItemEditText.setText("");
//
//        }
//        String updateItem = (String) itemsAdapter.getItem(position);
//        Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);
        Intent intent = new Intent(MainActivity.this, EditOrAddToDoItemActivity.class);
        if (intent != null) {
            // put "extras" into the bundle for access in the edit activity
            intent.putExtra("item", "");
//            intent.putExtra("position", "");
            // brings up the second activity
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE);
            itemsAdapter.notifyDataSetChanged();
//            saveItemsToFile();
            saveItemsToDatabase();
        }

    }

    private void setupListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(position); // Remove item from the ArrayList
                                itemsAdapter.notifyDataSetChanged(); // Notify listView adapter to update the list

//                                saveItemsToFile();
                                saveItemsToDatabase();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });

                builder.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem updateItem = (ToDoItem) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem.getToDoItemName());
                Intent intent = new Intent(MainActivity.this, EditOrAddToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem.getToDoItemName());
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                items.get(position).setToDoItemName(editedItem);
//                        items.set(position, editedItem);
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);
                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();

//                saveItemsToFile();
                saveItemsToDatabase();
            }else if(resultCode == RESULT_CANCELED){
                Log.i("Canceled","");
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == ADD_ITEM_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String addedItem = data.getExtras().getString("item");
                Log.i("Added Item in list:", addedItem);
                Toast.makeText(this, "updated:" + addedItem, Toast.LENGTH_SHORT).show();
                ToDoItem newItem = new ToDoItem(addedItem);
                items.add(newItem);
                itemsAdapter.notifyDataSetChanged();

//                saveItemsToFile();
                saveItemsToDatabase();
            }else if(resultCode == RESULT_CANCELED){
                Log.i("Canceled","");
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void readItemsFromFile(){
        //retrieve the app's private folder.
        //this folder cannot be accessed by other apps
        File filesDir = getFilesDir();
        //prepare a file to read the data
        File todoFile = new File(filesDir,"todo.txt");
        //if file does not exist, create an empty list
        if(!todoFile.exists()){
            items = new ArrayList<ToDoItem>();
        }else{
            try{
                //read data and put it into the ArrayList
                items = new ArrayList<ToDoItem>();
                for(String line:FileUtils.readLines(todoFile)){
//                    Log.i("Read:",line);
                    items.add(ToDoItem.getObjectFromLine(line));
                }
            }catch(IOException ex){
                items = new ArrayList<ToDoItem>();
            }catch (ParseException e) {
                items = new ArrayList<ToDoItem>();
            }
        }
    }

    private void saveItemsToFile(){
        File filesDir = getFilesDir();
        //using the same file for reading. Should use define a global string instead.
        File todoFile = new File(filesDir,"todo.txt");
        try{
        //write list to file
            FileUtils.writeLines(todoFile,items);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void readItemsFromDatabase()
    {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<ToDoItem> itemsFromDB = toDoItemDao.listAll();
                    items = new ArrayList<ToDoItem>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (ToDoItem item : itemsFromDB) {
                            items.add(item);
                            Log.i("SQLite read item", "ID: " + item.getToDoItemID() +
                                    "Name: " + item.getToDoItemName());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }
    private void saveItemsToDatabase()
    {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                //delete all items and re-insert
                toDoItemDao.deleteAll();
                for (ToDoItem todo : items) {
//                    ToDoItem item = new ToDoItem(todo);
                    toDoItemDao.insert(todo);
                    Log.i("SQLite saved item", todo.getToDoItemName());
                }
                return null;
            }
        }.execute();
    }
}
