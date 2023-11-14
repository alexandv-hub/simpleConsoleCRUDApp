package com.consoleCRUDApp;

import com.consoleCRUDApp.view.MainView;

public class MainApp {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();
        MainView mainMenuView = new MainView();
        mainMenuView.runMainMenu(context);
    }
}
