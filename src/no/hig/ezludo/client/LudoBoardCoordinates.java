package no.hig.ezludo.client;

/**
 * This class includes all coordinates from the ludo board.
 * @author Kristian
 * Created date 13.11.2015.
 *
 * All coordinates is from the upper left corner for every route.
 * array[routeNr][1] = X
 * array[routeNr][2] = Y
 *
 * To get the coordinates in right format:
 *  - Multiply X by the width of current image of ludo board
 *  - Multiply Y by the height of current image of ludo board
 *
 *  Start arrays define the 4 routs where all chips are placed at start.
 *  A player need to throw a 6 at dice to move from start-arrat to the main array.
 *  When a player has finished the main array, he or she can continue to
 *  finish array.
 *
 *  Main area indlues 1 route for each player / colour
 *  - Therefor finish-array starts at the second route with colour.
 */
public class LudoBoardCoordinates {
    double blueStart[][];  // Red start Area
    double redStart[][];  // Blue start Area
    double yellowStart[][]; // Yellow start Area
    double greenStart[][];  // Green start Are
    double mainArea[][]; // Main area for all players
    double blueFinish[][]; // Blue finish area
    double redFinish[][];  // Red finish area
    double yellowFinish[][];  // Yellow finish area
    double greenFinish[][];  // Green finish area

    public LudoBoardCoordinates() {
        blueStart = new double[5][3];
        redStart = new double[5][3];
        yellowStart = new double[5][3];
        greenStart = new double[5][3];
        mainArea = new double[52][3];
        blueFinish = new double[7][3];
        redFinish = new double[7][3];
        yellowFinish = new double[7][3];
        greenFinish = new double[7][3];

        for(int i=1; i<=4; i++) {
            for(int j=1; j<=2; j++) {
                blueStart[i][j] = 0;
                redStart[i][j] = 0;
                yellowStart[i][j] = 0;
                greenStart[i][j] = 0;
            }
        }

        for(int i=1; i<=51; i++) {
            for(int j=1; j<=2; j++) {
                mainArea[i][j] = 0;
            }
        }

        for(int i=1; i<=6; i++) {
            for(int j=1; j<=2; j++) {
                blueFinish[i][j] = 0;
                redFinish[i][j] = 0;
                yellowFinish[i][j] = 0;
                greenFinish[i][j] = 0;
            }
        }

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


        // Red route: 1 --> 51 --> 1 Then red finish array
        mainArea[1][1]= 0.5351390922401171; // RED START POS X
        mainArea[1][2]= 0.06871345029239766; // RED START POS Y
        mainArea[2][1]= 0.5351390922401171;
        mainArea[2][2]= 0.13742690058479531;
        mainArea[3][1]= 0.5351390922401171;
        mainArea[3][2]= 0.20175438596491227;
        mainArea[4][1]= 0.5351390922401171;
        mainArea[4][2]= 0.26900584795321636;
        mainArea[5][1]= 0.5351390922401171;
        mainArea[5][2]= 0.3362573099415205;
        mainArea[6][1]= 0.6017569546120058;
        mainArea[6][2]= 0.40058479532163743;
        mainArea[7][1]= 0.6676427525622255;
        mainArea[7][2]= 0.402046783625731;
        mainArea[8][1]=0.7342606149341142;
        mainArea[8][2]= 0.402046783625731;
        mainArea[9][1]= 0.8008784773060029;
        mainArea[9][2]= 0.402046783625731;
        mainArea[10][1]= 0.8674963396778916;
        mainArea[10][2]= 0.40058479532163743;
        mainArea[11][1]= 0.9333821376281113;
        mainArea[11][2]= 0.402046783625731;
        mainArea[12][1]= 0.9333821376281113;
        mainArea[12][2]= 0.4692982456140351;
        mainArea[13][1]= 0.9333821376281113;
        mainArea[13][2]= 0.5350877192982456;

        // Blue route: 14 --> 51 --> 14 Then blue finish array
        mainArea[14][1]=0.8667642752562226; // Blue Start X
        mainArea[14][2]= 0.533625730994152; // Blue start Y
        mainArea[15][1]= 0.8008784773060029;
        mainArea[15][2]= 0.5350877192982456;
        mainArea[16][1]= 0.7342606149341142;
        mainArea[16][2]=0.5350877192982456;
        mainArea[17][1]= 0.6676427525622255;
        mainArea[17][2]= 0.533625730994152;
        mainArea[18][1]= 0.6017569546120058;
        mainArea[18][2]= 0.5350877192982456;
        mainArea[19][1]= 0.5351390922401171;
        mainArea[19][2]=0.5994152046783626;
        mainArea[20][1]= 0.5351390922401171;
        mainArea[20][2]=0.6681286549707602;
        mainArea[21][1]=0.5351390922401171;
        mainArea[21][2]= 0.7339181286549707;
        mainArea[22][1]=0.5351390922401171;
        mainArea[22][2]= 0.8011695906432749;
        mainArea[23][1]= 0.5351390922401171;
        mainArea[23][2]=0.8654970760233918;
        mainArea[24][1]= 0.5351390922401171;
        mainArea[24][2]=0.9327485380116959;
        mainArea[25][1]=  0.4685212298682284;
        mainArea[25][2]= 0.9327485380116959;
        mainArea[26][1]= 0.40263543191800877;
        mainArea[26][2]= 0.9342105263157895;

        // Yellow route: 27 --> 51 --> 27 Then Yellow finish array
        mainArea[27][1]= 0.40263543191800877; // Yellow start X
        mainArea[27][2]= 0.8669590643274854; // Yellow start Y
        mainArea[28][1]=0.40263543191800877;
        mainArea[28][2]=0.7997076023391813;
        mainArea[29][1]= 0.40263543191800877;
        mainArea[29][2]=0.7339181286549707;
        mainArea[30][1]= 0.40263543191800877;
        mainArea[30][2]=0.6681286549707602;
        mainArea[31][1]=0.40263543191800877;
        mainArea[31][2]=0.6008771929824561;
        mainArea[32][1]=0.33601756954612005;
        mainArea[32][2]=0.533625730994152;
        mainArea[33][1]=0.26939970717423134;
        mainArea[33][2]=0.533625730994152;
        //mainArea[34][1]=0.13689604685212298;  // BUG?
        //mainArea[34][2]=0.533625730994152;    // BUG ?
        mainArea[34][1]=0.2027818448023426;
        mainArea[34][2]=0.5350877192982456;
        mainArea[35][1]=0.07027818448023426;
        mainArea[35][2]=0.533625730994152;
        mainArea[36][1]=0.0036603221083455345;
        mainArea[36][2]=0.5350877192982456;
        mainArea[37][1]=0.0036603221083455345;
        mainArea[37][2]=0.4678362573099415;
        mainArea[38][1]=0.0036603221083455345;
        mainArea[38][2]=0.40058479532163743;

        // Green route: 39 --> 51 --> 39 Then green finish array
        mainArea[39][1]=0.07027818448023426; // Green start X
        mainArea[39][2]=0.402046783625731; // Green start Y
        mainArea[40][1]=0.13689604685212298;
        mainArea[40][2]=0.40058479532163743;
        mainArea[41][1]=0.2027818448023426;
        mainArea[41][2]=0.402046783625731;
        mainArea[42][1]=0.26939970717423134;
        mainArea[42][2]=0.402046783625731;
        mainArea[43][1]=0.33601756954612005;
        mainArea[43][2]=0.402046783625731;
        mainArea[44][1]=0.40263543191800877;
        mainArea[44][2]=0.3362573099415205;
        mainArea[45][1]=0.40263543191800877;
        mainArea[45][2]=0.26900584795321636;
        mainArea[46][1]=0.40263543191800877;
        mainArea[46][2]=0.20321637426900585;
        mainArea[47][1]=0.40263543191800877;
        mainArea[47][2]=0.13742690058479531;
        mainArea[48][1]=0.40263543191800877;
        mainArea[48][2]=0.07017543859649122;
        mainArea[49][1]=0.40263543191800877;
        mainArea[49][2]=0.0043859649122807015;
        mainArea[50][1]=0.4685212298682284;
        mainArea[50][2]=0.0029239766081871343;
        mainArea[51][1]=0.5351390922401171;
        mainArea[51][2]=0.0029239766081871343;

        blueFinish[1][1]=0.8674963396778916;
        blueFinish[1][2]=0.4692982456140351;
        blueFinish[2][1]=0.8008784773060029;
        blueFinish[2][2]=0.4692982456140351;
        blueFinish[3][1]=0.7342606149341142;
        blueFinish[3][2]=0.4678362573099415;
        blueFinish[4][1]=0.6676427525622255;
        blueFinish[4][2]=0.4692982456140351;
        blueFinish[5][1]=0.6017569546120058;
        blueFinish[5][2]=0.4692982456140351;
        blueFinish[6][1]=0.5344070278184481;  // GOAL X
        blueFinish[6][2]=0.4649122807017544;  // GOAL Y

        redFinish[1][1]=0.4685212298682284;
        redFinish[1][2]=0.06871345029239766;
        redFinish[2][1]=0.4685212298682284;
        redFinish[2][2]=0.13742690058479531;
        redFinish[3][1]=0.4685212298682284;
        redFinish[3][2]=0.20321637426900585;
        redFinish[4][1]=0.4685212298682284;
        redFinish[4][2]=0.26900584795321636;
        redFinish[5][1]=0.4677891654465593;
        redFinish[5][2]=0.3347953216374269;
        redFinish[6][1]=0.465592972181552; // GOAL X
        redFinish[6][2]=0.3991228070175439; // GOAL Y


        yellowFinish[1][1]=0.4685212298682284;
        yellowFinish[1][2]=0.8669590643274854;
        yellowFinish[2][1]=0.4677891654465593;
        yellowFinish[2][2]=0.7997076023391813;
        yellowFinish[3][1]=0.4685212298682284;
        yellowFinish[3][2]=0.7324561403508771;
        yellowFinish[4][1]=0.4685212298682284;
        yellowFinish[4][2]=0.6666666666666666;
        yellowFinish[5][1]=0.4677891654465593;
        yellowFinish[5][2]=0.5994152046783626;
        yellowFinish[6][1]=0.46486090775988287; // GOAL X
        yellowFinish[6][2]=0.5248538011695907;  // GOAL Y

        greenFinish[1][1]=0.07027818448023426;
        greenFinish[1][2]=0.4678362573099415;
        greenFinish[2][1]=0.13689604685212298;
        greenFinish[2][2]=0.4678362573099415;
        greenFinish[3][1]=0.2027818448023426;
        greenFinish[3][2]=0.4678362573099415;
        greenFinish[4][1]=0.26939970717423134;
        greenFinish[4][2]=0.4678362573099415;
        greenFinish[5][1]=0.33601756954612005;
        greenFinish[5][2]=0.4678362573099415;
        greenFinish[6][1]=0.40043923865300146; // GOAL X
        greenFinish[6][2]=0.46637426900584794; // GOAL Y
    }
}
