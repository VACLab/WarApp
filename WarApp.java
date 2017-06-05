package edu.unc.vaclab.warapp;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by gotz on 6/2/17.
 */
public class WarApp {
    public static void main(String[] _args) {
        String _war = "Sample.war";
        int _port = 18779;

        // Launch the web app using an embedded version of Tomcat
        Process _p = null;
        try {
            _p = Runtime.getRuntime().exec("java -jar lib/webapp-runner-8.5.15.0.jar --port "+String.valueOf(_port)+" war/"+_war);
        }
        catch (Exception e){
            if (_p != null) {
                _p.destroyForcibly();
            }
            System.out.println(e.getMessage());
        }

        // Create the browser.
        Browser _browser = new Browser();
        BrowserView _view = new BrowserView(_browser);

        // Put the browser into a JFrame
        JFrame _frame = new JFrame();
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.add(_view, BorderLayout.CENTER);
        _frame.setSize(1200,800);

        // Set up a listener to kill the web server when the user closes the window.
        _frame.addWindowListener(new WindowAdapter() {
            private Process process = null;
            @Override
            public void windowClosing(WindowEvent _e) {
                if (process != null) {
                    process.destroyForcibly();
                }
            }

            private WindowAdapter init(Process _p) {
                process = _p;
                return this;
            }
        }.init(_p));

        // Point the browser to the server and display the JFrame.
        _browser.loadURL("http://localhost:"+String.valueOf(_port)+"/");
        _frame.setVisible(true);
    }
}
