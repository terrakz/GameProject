/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

class MyProtocol {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    public MyProtocol() {

    }

    public String processInput(int direction) {

        switch (direction) {
            case (UP): {
                return "UP";
            }
            case (DOWN): {
                return "DOWN";
            }
            case (LEFT): {
                return "LEFT";
            }
            case (RIGHT): {
                return "RIGHT";
            }
            default: return "";
        }
    }

}
