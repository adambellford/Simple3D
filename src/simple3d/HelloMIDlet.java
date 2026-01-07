/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simple3d;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class HelloMIDlet extends MIDlet {

    private StarField starField;

    public void startApp() {
        if (starField == null) {
            starField = new StarField();
        }
        Display.getDisplay(this).setCurrent(starField);
    }

    public void pauseApp() { }

    public void destroyApp(boolean unconditional) { }
}