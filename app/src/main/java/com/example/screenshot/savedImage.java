package com.example.screenshot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.screenshot.R;

import java.io.File;
import java.util.ArrayList;

public class savedImage extends AppCompatActivity {
    ListView imageList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_image);

        imageList = findViewById(R.id.imageLV);

        ArrayList<String> imageFileNames = getListOfImageFileNames();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, imageFileNames);
        imageList.setAdapter(adapter);

        //This is for delete item option on long press
        registerForContextMenu(imageList);

        // When we click on a particular file, it should open
        imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String fileName = (String) parent.getItemAtPosition(position);
                openImageFile(fileName);
            }
        });
    }

    private ArrayList<String> getListOfImageFileNames() {
        ArrayList<String> imageFileNames = new ArrayList<>();
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (storageDir.isDirectory()) {
            File[] files = storageDir.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("Screenshot_")) {
                    imageFileNames.add(file.getName());
                }
            }
        }

        return imageFileNames;
    }

    private void openImageFile(String fileName) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", imageFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(imageUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Add read permission

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*onCreateContextMenu Method:
    This method is used to create a context menu that appears when a user performs a long
     click on an item in the ListView. In your code, the onCreateContextMenu method is called
    when the user long-clicks an item. Inside this method, you inflate a menu resource (in
    your case, R.menu.context_menu) to create the context menu items.*/

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu); // Inflate context menu layout
    }

    /*onContextItemSelected Method:
    This method is called when a context menu item is selected. It receives a MenuItem object
    that represents the selected item and provides information about it. In your code, you use
    the AdapterView.AdapterContextMenuInfo object (info) to get information about the item that
    was long-clicked, such as its position in the ListView. You then retrieve the filename
    associated with that position using the adapter (which should be properly assigned at the
    class level). The switch statement is used to determine which context menu item was selected,
     and you take specific actions accordingly.*/

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedFileName = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.delete_option:
                deleteImageFile(selectedFileName);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*deleteImageFile Method:
    This method is responsible for deleting the selected image file from the storage. It receives the
    filename of the image to be deleted. Inside the method, you construct the full path to the image
    file using the storage directory and the provided filename. You check if the file exists and if it
    does, you attempt to delete it. If the deletion is successful, you remove the filename from the
    adapter (which holds the list of image filenames displayed in the ListView) and call
    adapter.notifyDataSetChanged() to refresh the ListView with the updated list.*/

    private void deleteImageFile(String fileName) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, fileName);

        if (imageFile.exists()) {
            if (imageFile.delete()) {
                adapter.remove(fileName);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
