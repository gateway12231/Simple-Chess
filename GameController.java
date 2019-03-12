public class GameController {
    public static boolean play = true;

    public static void main (String[] args) {
        Player nick = new Player("white", "Nick");
        Player ben = new Player("black", "Ben");
        Player currentPlayer = nick;
        Board woody = new Board(nick, ben);

        //woody.testingMethod();
        System.out.println(woody);

        while (play) {
            woody.movePhase(currentPlayer);
            System.out.println(woody);
            checkGameState();

            if (currentPlayer.equals(nick)) {
                currentPlayer = ben;
            }
            else {
                currentPlayer = nick;
            }
        }


    }

    private static void checkGameState () {

    }

    private static void takeTurn (Board board, Player currentPlayer) {
        board.movePhase(currentPlayer);
        //board.isInCheckMate();

    }
}