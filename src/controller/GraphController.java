package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import com.sun.prism.impl.paint.PaintUtil;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import application.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

public class GraphController {
    @FXML
    private ListView<String> elements;
    private Main main;
    @FXML
    private TextField name;
    @FXML
    private TextField value;
    @FXML
    private Pane AddItemPage;
    private List<String> values = new ArrayList<String>();
    @FXML
    private Pane GraphPane;
    @FXML
    private TextField FirstStep;
    private DbxWebAuthNoRedirect webAuth;
    private DbxRequestConfig config;
    private DbxClient client;
    @FXML
    private TextField code;
    @FXML
    private Pane AuthorizePane;
    
    static class DropManager{
        private static DbxClient client = null;
        static public DbxClient getManager(){
            return client;
        }
    }
    
    public void setMain(Main main){
        this.main = main;
     // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = "9paa53gaevjt6aj";
        final String APP_SECRET = "je9lfk1vw822gyx";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        config = new DbxRequestConfig(
            "JavaTutorial/1.0", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        
        String authorizeUrl = webAuth.start();

        FirstStep.setText(String.format("1. Go to: %1$s", authorizeUrl));
    }
    
    @FXML
    private void Submit(){
        DbxAuthFinish authFinish;
        Boolean isPassed = true;
        try {
            authFinish = webAuth.finish(code.getText());
            
            String accessToken = authFinish.accessToken;
            
            client = new DbxClient(config, accessToken);
        }
        catch (DbxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            isPassed = false;
        }
        if(isPassed){
            AuthorizePane.setVisible(false);
        }
    }
    
    @FXML
    private void Save(){
        FileInputStream inputStream = null;
        try {
            File inputFile = new File("working-draft.txt");
            FileWriter outputStream = new FileWriter(inputFile);
            for(String s : values){
                outputStream.write(s + ",");   
            }
            outputStream.close();
            inputStream = new FileInputStream(inputFile);
            DbxEntry.File uploadedFile = client.uploadFile("/magnum-opus.txt",
                DbxWriteMode.add(), inputFile.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DbxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void Add(){
        AddItemPage.setVisible(true);
    }
    
    @FXML
    private void AddValue(){
        try
        {
            Integer.parseInt(value.getText());
            values.add(String.format("%1$s %2$s", name.getText(), value.getText()));
            elements.setItems(FXCollections.observableArrayList(values));
        }
        catch(Exception e){
        }
        AddItemPage.setVisible(false);
    }
    
    @FXML
    private void Show(){
        List<Rectangle> rects = new ArrayList<>();
        List<Label> labels = new ArrayList<>();
        int i = 0;
        for (String S : values) {
            String[] ss = S.split(" ");
            rects.add(new Rectangle(70 * i, 250 - 10 * Integer.parseInt(ss[1]), 50, 10 * Integer.parseInt(ss[1])));
            Label l = new Label(ss[0]);
            l.setLayoutX(70 * i);
            l.setLayoutY(250);
            l.setMaxWidth(50);
            labels.add(l);
            i++;
        }
        GraphPane.getChildren().addAll(rects);
        GraphPane.getChildren().addAll(labels);
        GraphPane.setVisible(true);
    }
    
    @FXML
    private void Back(){
        GraphPane.setVisible(false);
    }
}
