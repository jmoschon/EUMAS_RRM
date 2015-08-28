/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import eumas_rrm.EUMAS_RRM;
import java.io.IOException;

/**
 *
 * @author jmoschon
 */
public class Main2 {

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 50; i++) {
            EUMAS_RRM rrmA = new EUMAS_RRM();
            rrmA.core();

            rrmA.makeSingleAgent(i);
        }
    }

}
