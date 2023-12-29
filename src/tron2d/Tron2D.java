/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tron2d;

import view.MainWindow;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tron2D {

    private static final Logger LOGGER = Logger.getLogger(Tron2D.class.getName());

    /**
     * The main method to start the application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow();              
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "An error occurred while starting the application", ex);
        }
    }
}
