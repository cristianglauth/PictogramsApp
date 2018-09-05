package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al fragmento del apartado de los cuentos.
 *
 * @author Cristian Gonzalez Lopez
 */
public class StoryFragment extends Fragment {

    private List<Story> STORY_LIST;

    private Story_Listview_adapter story_listview_adapter;

    private String fileName = "";

    /**
     * Instancia un nuevo objeto de la clase
     */
    public StoryFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        fileName = getActivity().getApplicationContext().getDir("DirName", Context.MODE_PRIVATE).toString()+"/stories";
        STORY_LIST = new ArrayList<>();

        File file = new File(fileName);
        if (!file.exists()){
            file.mkdir();
        }

        File[] files = file.listFiles();


        for (int i=0; i<files.length;i++) {
            if (files[i].isDirectory()) {
                File[] files1 = files[i].listFiles();
                for (int j=0; j<files1.length;j++) {
                    if (!files1[j].isDirectory()) {
                        try {
                            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(files1[j].toString()), "utf8");
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);
                            StringBuilder stringBuilder = new StringBuilder();
                            String line = null;
                            while ((line = bufferedReader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }

                            bufferedReader.close();
                            String JsonFinal = stringBuilder.toString();

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            Story story = gson.fromJson(JsonFinal, Story.class);
                            STORY_LIST.add(story);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        story_listview_adapter = new Story_Listview_adapter(getActivity().getApplicationContext(), STORY_LIST);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.frag_story, null);



        ListView listView = myview.findViewById(R.id.story_listview);
        story_listview_adapter.notifyDataSetChanged();
        listView.setAdapter(story_listview_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(getActivity(), StoryLayout.class);
                intent.putExtra("story", STORY_LIST.get(i));
                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int position = i;

                new AlertDialog.Builder(getActivity())
                        .setTitle("Borrar Cuento")
                        .setMessage("¿Está seguro que desea borrar el cuento?")
                        .setPositiveButton("BORRAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Story story = STORY_LIST.get(position);

                                //File filetemp = new File(fileName+"/"+story.getTittle()+".txt");
                                File filetemp = new File(fileName+"/"+story.getTittle());
                                if (filetemp.exists()){
                                    if (rmdir(filetemp)){
                                        STORY_LIST.remove(position);
                                        story_listview_adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "El cuento "+story.getTittle()+" ha sido eliminado", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        })
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();

                return true;
            }
        });

        return myview;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_story_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent intent = new Intent(getActivity(), StoryAdd.class);
        startActivityForResult(intent, 10);


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10){
            if (resultCode == Activity.RESULT_OK){

                String temp = data.getStringExtra("gson");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Story storyintent = gson.fromJson(temp, Story.class);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName+"/"+storyintent.getTittle()+"/"+storyintent.getTittle()+".txt"), false);
                    byte[] content = temp.getBytes();
                    fileOutputStream.write(content);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                STORY_LIST.add(storyintent);
                story_listview_adapter.notifyDataSetChanged();

            }
        }
    }

    /**
     * Elimina el directorio donde se guada el cuento
     *
     * @param folder the folder
     * @return the boolean
     */
    public static boolean rmdir(final File folder) {

        if (folder.isDirectory()) {
            File[] list = folder.listFiles();
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    File tmpF = list[i];
                    if (tmpF.isDirectory()) {
                        rmdir(tmpF);
                    }
                    tmpF.delete();
                }
            }
            if (folder.delete()) {
                return true;
            }
        }
        return false;
    }
}
