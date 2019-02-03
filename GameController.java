public class GameController {
    public static void main (String[] args) {
        Player nick = new Player("white");
        Player ben = new Player("black");
        Board woody = new Board(nick, ben);

        System.out.println(woody);

        //woody.testingMethod();
    }
}