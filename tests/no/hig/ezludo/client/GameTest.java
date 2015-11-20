package no.hig.ezludo.client;

import junit.framework.TestCase;
import no.hig.ezludo.server.Game;
import org.junit.Test;


/**
 * Created by jdr on 20/11/15.
 */
public class GameTest {

    public static void main(String[] args) {

        Thread t = new Thread(() -> {
            GameBot GameBot1 = new GameBot("nils@hei.no", "12345678", "nilzWithSkillz");
        });
        t.start();

        Thread l = new Thread(() -> {
            GameBot GameBot2 = new GameBot("per@hei.no", "12345678", "pirkola69");
        });
        l.start();

        Thread p = new Thread(() -> {
            GameBot GameBot3 = new GameBot("franz@hei.no", "12345678", "franZKlla");
        });
        p.start();

        Thread r = new Thread(() -> {
            GameBot GameBot4 = new GameBot("swag@hei.no", "12345678", "kitty");
        });
        r.start();

        Thread b = new Thread(() -> {
            GameBot GameBot5 = new GameBot("heihopp@hei.no", "12345678", "sudoMan");
        });
        b.start();

        Thread a = new Thread(() -> {
            GameBot GameBot6 = new GameBot("lars@hei.no", "12345678", "pimpKillaLett");
        });
        a.start();
    }
}
