package no.hig.ezludo.client;

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

        blueStart[1][1]=0.7679355783308931;
        blueStart[1][2]=0.7017543859649122;
        blueStart[2][1]=0.7013177159590044;
        blueStart[2][2]=0.7675438596491229;
        blueStart[3][1]=0.7679355783308931;
        blueStart[3][2]=0.8318713450292398;
        blueStart[4][1]=0.8345534407027818;
        blueStart[4][2]=0.7660818713450293;

        redStart[1][1]=0.767203513909224;
        redStart[1][2]=0.10380116959064327;
        redStart[2][1]=0.7013177159590044;
        redStart[2][2]=0.1695906432748538;
        redStart[3][1]=0.767203513909224;
        redStart[3][2]=0.23538011695906433;
        redStart[4][1]=0.8338213762811127;
        redStart[4][2]=0.1695906432748538;

        yellowStart[1][1]=0.1698389458272328;
        yellowStart[1][2]=0.7002923976608187;
        yellowStart[2][1]=0.10322108345534407;
        yellowStart[2][2]=0.7660818713450293;
        yellowStart[3][1]=0.23645680819912152;
        yellowStart[3][2]=0.7660818713450293;
        yellowStart[4][1]=0.1698389458272328;
        yellowStart[4][2]=0.8333333333333334;

        greenStart[1][1]=0.1698389458272328;
        greenStart[1][2]=0.10380116959064327;
        greenStart[2][1]=0.10322108345534407;
        greenStart[2][2]=0.16812865497076024;
        greenStart[3][1]=0.1698389458272328;
        greenStart[3][2]=0.23538011695906433;
        greenStart[4][1]=0.23572474377745242;
        greenStart[4][2]=0.16812865497076024;
    }
}
