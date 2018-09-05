package com.example.crist.pictogramsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa al fragmento del apartado de los tableros.
 *
 * @author Cristian Gonzalez Lopez
 */
public class BoardFragment extends Fragment {


    private List<Board> boardList;
    private ListView listView;
    private Board_Listview_adapter boardListviewAdapter;
    private String newDir = "";

    /**
     * Instancia un nuevo fragmento
     */
    public BoardFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        newDir = getActivity().getApplicationContext().getDir("DirName", Context.MODE_PRIVATE).toString()+"/boards.txt";



        File file = new File(newDir);
        boardList = new ArrayList<>();

        if (file.exists()){

            try {
                InputStreamReader inputStream = new InputStreamReader(new FileInputStream(newDir), "utf8");
                BufferedReader bufferedReader = new BufferedReader(inputStream, 8192);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                String JsonTotal = stringBuilder.toString();

                JSONArray jsonArray = new JSONArray(JsonTotal);

                for (int i=0; i< jsonArray.length(); i++) {

                    Gson gson = new Gson();
                    Board board = gson.fromJson(jsonArray.get(i).toString(), Board.class);
                    boardList.add(board);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            boardList = new ArrayList<>();
        }
        boardListviewAdapter = new Board_Listview_adapter(getActivity().getApplicationContext(), boardList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_frag_board, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myview =  inflater.inflate(R.layout.frag_board, null);


        listView = myview.findViewById(R.id.boardListView);
        listView.setAdapter(boardListviewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), BoardLayout.class);
                intent.putExtra("boardVP", boardList.get(i));
                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int position = i;

                new AlertDialog.Builder(getActivity())
                        .setTitle("Borrar Tablero de comunicación")
                        .setMessage("¿Está seguro que desea borrar el tablero de comunicación?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boardList.remove(position);
                                writeFile();
                                boardListviewAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "El tablero ha sido borrado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id){

            case R.id.menu_frg_board_add:


                Intent intent = new Intent(getActivity(), BoardAdd.class);
                startActivityForResult(intent, 30);


                    return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 30){
            if (resultCode == Activity.RESULT_OK){

                String temp = data.getStringExtra("gson");
                Gson gson = new Gson();
                Board boardintent = (Board) gson.fromJson(temp, Board.class);
                boardList.add(boardintent);
                boardListviewAdapter.notifyDataSetChanged();
                writeFile();
            }
        }

    }

    /**
     * Escribe el fichero json con los tableros que existen en la base de datos.
     */
    public void writeFile() {

        //Writer writer = new FileWriter(newDir);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String temp = gson.toJson(boardList);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(newDir), false);
            byte[] content = temp.getBytes();
            fileOutputStream.write(content);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
