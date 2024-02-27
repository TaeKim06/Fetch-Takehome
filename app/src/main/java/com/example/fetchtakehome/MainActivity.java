package com.example.fetchtakehome;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.net.*;
import java.io.*;
import java.util.*;



public class MainActivity extends AppCompatActivity {
    Map<Integer, ArrayList<Item>> groupedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        groupedItems  = new HashMap<>();

        // Initializing RecyclerView and Adapter
        RecyclerAdapter adapter = new RecyclerAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        runManager(adapter);

    }

    // Starts the functions that read the json from website and creates hashmap groupedItems store the nonnull, non "" items
    // Sorts individual arraylists, then populates the data in order of listId
    private void runManager(RecyclerAdapter adapter) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String inputLine = readData(); // Read data from URL
                    Log.d("JSON Response", inputLine); // Log JSON response
                    updateMap(inputLine); // Update adapter with data
                    ArrayList<Item> sortedList = createSingleArrayList();
//                    Log.d("Test", sortedList.get(0).allToString());

                    // Update RecyclerView with data
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setGroupedItems(sortedList);
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Function that connects to the internet to read the json data from the website and returns all the data in the form of a string
    public String readData() throws Exception {
        URL jData = new URL("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        URLConnection hiringJson = jData.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(hiringJson.getInputStream()));
        StringBuilder jsonData = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
//            Log.d("json file", inputLine);
            jsonData.append(inputLine);
        }
        in.close();
        return jsonData.toString();
    }

    // Takes in a string in the format of a json file with parameters id, listId, and name and populates hashmap
    public void updateMap(String inputLine){
        try {
            JSONArray jsonArray = new JSONArray(inputLine);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int listId = jsonObject.getInt("listId");
                String name = jsonObject.getString("name");

                // populating Hashmap using listId as key to arraylists
                if (!name.equalsIgnoreCase("null") && !name.equalsIgnoreCase("")) {
                    Item item = new Item(id, listId, name);
//                    Log.d("Item: ", item.allToString());
                    if (!groupedItems.containsKey(listId)) {
                        groupedItems.put(listId, new ArrayList<>());
                    }
                    groupedItems.get(listId).add(item);
                }
            }

//            // Logging whether or not the items have been properly grouped into arraylists
            for (Map.Entry<Integer, ArrayList<Item>> entry : groupedItems.entrySet()) {
                int listId = entry.getKey();
                ArrayList<Item> items = entry.getValue();
                StringBuilder listItems = new StringBuilder();
                for(Item item: items){
                    listItems.append(item.allToString()).append("; ");
                }
                Log.d("Grouped Items", "List " + listId + ":{" + listItems + "}");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Sorts the individual arraylists in the hashmap and creates one large arraylist to display using the recyclerview
    public ArrayList<Item> createSingleArrayList(){
        ArrayList<Item> newList = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Item>> entry : groupedItems.entrySet()) {
            String listId = entry.getKey().toString();
            Log.d("List Not Null", listId);
            ArrayList<Item> items = entry.getValue();

            Collections.sort(items);
            newList.addAll(items);
        }
        return newList;
    }
}