package no.hig.ezludo.client;

import java.util.HashMap;

/**
 * Created by Kristian on 13.11.2015.
 */
public class LudoBoardCoordinates {
    double blueStart[][];
    double redStart[][];
    double yellowStart[][];
    double greenStart[][];

    public LudoBoardCoordinates() {

        //double [RUTENR][X=1]
        //double [RUTENR][Y=2];

        // Koordinat X må ganges med bildets bredde
        // Koordinat Y må ganges med bildets høyde

        blueStart[1][1]=0.7972181551976574;
        blueStart[1][2]=0.7309941520467836;
        blueStart[2][1]=0.8638360175695461;
        blueStart[2][2]=0.7967836257309941;
        blueStart[3][1]=0.7964860907759883;
        blueStart[3][2]=0.8625730994152047;
        blueStart[4][1]=0.7276720351390923;
        blueStart[4][2]=0.7967836257309941;

        redStart[1][1]=0.7957540263543191;
        redStart[1][2]=0.13450292397660818;
        redStart[2][1]=0.7291361639824304;
        redStart[2][2]=0.19883040935672514;
        redStart[3][1]=0.8616398243045388;
        redStart[3][2]=0.19736842105263158;
        redStart[4][1]=0.7957540263543191;
        redStart[4][2]=0.2646198830409357;

        yellowStart[1][1]=0.19838945827232796;
        yellowStart[1][2]=0.7309941520467836;
        yellowStart[2][1]=0.13103953147877012;
        yellowStart[2][2]=0.7967836257309941;
        yellowStart[3][1]=0.2642752562225476;
        yellowStart[3][2]=0.7982456140350878;
        yellowStart[4][1]=0.19838945827232796;
        yellowStart[4][2]=0.8640350877192983;

        greenStart[1][1]=0.19912152269399708;
        greenStart[1][2]=0.13450292397660818;
        greenStart[2][1]=0.13103953147877012;
        greenStart[2][2]=0.20029239766081872;
        greenStart[3][1]=0.26500732064421667;
        greenStart[3][2]=0.20029239766081872;
        greenStart[4][1]=0.19765739385065886;
        greenStart[4][2]=0.2675438596491228;

    }
}
