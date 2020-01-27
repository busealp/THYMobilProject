package com.mobilproject.helper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleExcel {
    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "Deneme";
    private static String SPREADSHEET_ID = "11647QGuPrGhXGVFtCaDeDmkKkwlDyXt_LyinWz_reIw";

    private static Credential authorize() throws IOException, GeneralSecurityException {
        // Load client secrets.
        InputStream in = GoogleExcel.class.getResourceAsStream("/credentials.json");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
                .authorize("user");
        return credential;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build();
    }
/*
    public static void main(String[] args) throws IOException,GeneralSecurityException {
        sheetsService = getSheetsService();
        String range = "Sayfa1!A1:C2";

        ValueRange responce = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID,range)
                .execute();
        List<List<Object>> values = responce.getValues();
        for (List row : values){
            System.out.println(row.get(2));
        }


    }

*/
    public static String Excel(int i) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
        String range = "Sayfa1!A1:C2";
        String text=null;

        ValueRange responce = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = responce.getValues();
        for (List row : values) {
             text = row.get(i).toString();
        }
        return text;
    }
}
