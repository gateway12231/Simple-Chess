import java.util.Scanner;

public class GameController {
    public static boolean play = true;
    public static Player winner = null;
    public static boolean stalemate = false;

    public static void main (String[] args) {
        Player white;
        Player black;
        Player currentPlayer;
        Board woody;
        Scanner reader = new Scanner(System.in);

        System.out.println("Please enter the name of the white player: ");
        white = new Player("white", reader.nextLine());
        System.out.println("Please enter the name of the black player: ");
        black = new Player("black", reader.nextLine());
        woody = new Board(white, black);
        currentPlayer = white;

        // Initial board print and state check. The state check is in place in case the board is initialized to a final position
        System.out.println(woody);
        woody.checkGameState(currentPlayer);
        //woody.testingMethod();

        // The play loop
        while (play) {
            woody.movePhase(currentPlayer);
            System.out.println(woody);
            woody.checkGameState(currentPlayer);

            if (currentPlayer.equals(white)) {
                currentPlayer = black;
            }
            else {
                currentPlayer = white;
            }
        }

        // The end states based off of results from the checkGameState method
        if (winner == null && stalemate) {
            System.out.println("Game over by stalemate");
        }
        else if (winner.equals(white)) {
            System.out.println(white.getName() + " wins by checkmate");
        }
        else if (winner.equals(black)) {
            System.out.println(black.getName() + " wins by checkmate");
        }
        else {
            System.out.println("Something dun fucked up");
        }

    }

    /*private static void takeTurn (Board board, Player currentPlayer) {
        board.movePhase(currentPlayer);
        //board.isInCheckMate();

    }*/
}