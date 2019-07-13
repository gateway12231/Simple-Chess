package com.nwurth.simplechess;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;
import java.util.HashMap;


public class Board extends AppCompatActivity {
    private Player whitePlayer;
    private Player blackPlayer;
    // This represents the board in a 8X8 array. All empty spaces are null. The first index is the letter and the second is the number
    private GamePiece[][] playSpace;
    private Context c;

    public Board (Player whitePlayer, Player blackPlayer, Context inMatch) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.c = inMatch;
        playSpace = new GamePiece[8][8];
        initializePlaySpace();
        // This was used to test move generation
        /*playSpace = new GamePiece[][]
                       {{null, null, new GamePiece(whitePlayer, "pawn", 0, 2), new GamePiece(blackPlayer, "pawn", 0, 4), null, null, null, new GamePiece(whitePlayer, "king", 0, 7)},
                        {null, null, null, null, null, null, new GamePiece(blackPlayer, "rook", 1, 6), null},
                        {null, null, null, null, null, null, new GamePiece(blackPlayer, "rook", 2, 6), null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {new GamePiece(blackPlayer, "king", 7, 0), null, null, null, null, null, null, null}};*/
    }

    public void testingMethod () {
        GamePiece piece = playSpace[0][7];
        generateMoves(piece);
        printToast(piece.allMovesToString());
    }

    private void initializePlaySpace () { // This "places" all the pieces on the board
        for (int i = 0; i < 8; i += 1) { // i is the current number
            for (int j = 0; j < 8; j += 1) { // j is the current letter
                if (i == 1) { // The white pawn row
                    // I don't remember why I have the j first
                    playSpace[j][i] = new GamePiece(whitePlayer, "pawn", j, i);
                }
                else if (i == 6) { // The black pawn row
                    playSpace[j][i] = new GamePiece(blackPlayer, "pawn", j, i);
                }
                else if (i == 0){ // The back row. Made use of symmetry
                    if (j == 0 || j == 7) {
                        playSpace[j][i] = new GamePiece(whitePlayer, "rook", j, i);
                    }
                    else if (j == 1 || j == 6) {
                        playSpace[j][i] = new GamePiece(whitePlayer, "knight", j, i);
                    }
                    else if (j == 2 || j == 5) {
                        playSpace[j][i] = new GamePiece(whitePlayer, "bishop", j, i);
                    }
                    else if (j == 3) {
                        playSpace[j][i] = new GamePiece(whitePlayer, "queen", j, i);
                    }
                    else {
                        playSpace[j][i] = new GamePiece(whitePlayer, "king", j, i);
                    }
                }
                else if (i == 7) {
                    if (j == 0 || j == 7) {
                        playSpace[j][i] = new GamePiece(blackPlayer, "rook", j, i);
                    }
                    else if (j == 1 || j == 6) {
                        playSpace[j][i] = new GamePiece(blackPlayer, "knight", j, i);
                    }
                    else if (j == 2 || j == 5) {
                        playSpace[j][i] = new GamePiece(blackPlayer, "bishop", j, i);
                    }
                    else if (j == 3) {
                        playSpace[j][i] = new GamePiece(blackPlayer, "queen", j, i);
                    }
                    else {
                        playSpace[j][i] = new GamePiece(blackPlayer, "king", j, i);
                    }
                }
                else {
                    playSpace[j][i] = null;
                }
            }
        }
    }

    // This checks if there is a checkmate or a stalemate and sets static variables within GameController accordingly
    public int checkGameState (Player currentPlayer) {
        // This is the part for checkmate
        if (currentPlayer.equals(whitePlayer) && isInCheckmate(findPiece(blackPlayer, "king")[0])) {
            // Stops the play loop
            //GameController.play = false;
            // Sets the winner
            //GameController.winner = whitePlayer;

            return 1;
        }

        else if (currentPlayer.equals(blackPlayer) && isInCheckmate(findPiece(whitePlayer, "king")[0])) {
            //GameController.play = false;
            //GameController.winner = blackPlayer;
            return 2;
        }

        // This is the part for stalemate
        else if (isInStalemate(whitePlayer) || isInStalemate(blackPlayer)) {
            //GameController.play = false;
            //GameController.stalemate = true;
            return 3;
        }
        else{
            return 0;
        }
    }

    // Returns true if there is a checkmate on the provided piece
    private boolean isInCheckmate (GamePiece king) {
        // Avoid 0 0 combination. Possible combinations {1 -1, 1 0, 1 1, 0 -1, **0 0**, 0 1, -1 -1, -1 0, -1 1} **SKIP THIS ONE
        for (int i = -1; i < 2; i += 1) {
            for (int j = -1; j < 2; j += 1) {
                if (inBounds(king.getPositionLetter() + i) && inBounds(king.getPositionNumber() + j)) {
                    if (kingChecker(makeGhostPlaySpace(king, king.getPositionLetter() + i, king.getPositionNumber() + j),
                            new GamePiece(king.getOwner(), "king", king.getPositionLetter() + i, king.getPositionNumber() + j))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // Stalemate happens when no moves can be made
    // This method returns false at the first instance of a possible move
    private boolean isInStalemate (Player player) {
        GamePiece probe;

        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                probe = playSpace[i][j];
                // This checks if there is a GamePiece where the probe is
                if (probe != null) {
                    // This checks if the owner of the probed GamePiece is the same as <player>
                    if (probe.getOwner().equals(player)) {
                        generateMoves(probe);
                        // This checks if any moves were generated
                        if (!probe.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean movePhase (Player currentPlayer, int startLetter, int startNumber, int endLetter, int endNumber) {
        // The string format should be "KC4 C5"
        GamePiece movingPiece;
        //Scanner reader = new Scanner(System.in);

        //printToast(currentPlayer.getName() + " please enter your move: ");

            movingPiece = new GamePiece(currentPlayer, playSpace[startLetter][startNumber].getType(), startLetter, startNumber);
            if(playSpace[startLetter][startNumber] == null){
                printToast("There's no piece at the selection");
                return false;
            }

            // This step is here to make sure the piece they want to move exists
            if (movingPiece.equals(playSpace[startLetter][startNumber])) {
                // Since it exists we don't need the other one that was made
                if (!movingPiece.getColor().equals(currentPlayer.getColor())){
                    printToast("You selected your opponent's piece. Try again");
                    return false;
                }
                movingPiece = playSpace[startLetter][startNumber];

                generateMoves(movingPiece);
                // Now the move can finally be made
                if (movingPiece.isMoveValid(endLetter, endNumber)) {
                    // Updates if the piece in question has moved
                    if (!movingPiece.hasMoved()) {
                        movingPiece.moved();
                    }

                    // Functionality for capturing a piece. The piece is stored in the Player instance of the opponent
                    if (playSpace[endLetter][endNumber] != null) {
                        movingPiece.getOwner().addCapturedPiece(playSpace[endLetter][endNumber]);
                    }

                    playSpace[endLetter][endNumber] = movingPiece;
                    movingPiece.changePosition(endLetter, endNumber);
                    playSpace[startLetter][startNumber] = null;
                    return true;
                }
                else {
                    printToast("\nThat is not a valid move.\n\nPlease enter another move, " + currentPlayer.getName() + ": ");
                    return false;
                }
            }
            else {
                printToast("\nThe piece you are referencing does not exist.\n\nPlease enter another move, " + currentPlayer.getName() + ": ");
                return false;
            }

    }

    public void generateMoves (GamePiece piece) {
        // Only generate moves for the piece in question. Not the whole board
        piece.clearMoves();

        if (piece.getType().equals("pawn")) {
            generatePawnMoves(piece);
        }
        else if (piece.getType().equals("rook")) {
            generateRookMoves(piece);
        }
        else if (piece.getType().equals("knight")) {
            generateKnightMoves(piece);
        }
        else if (piece.getType().equals("bishop")) {
            generateBishopMoves(piece);
        }
        else if (piece.getType().equals("queen")) {
            generateRookMoves(piece);
            generateBishopMoves(piece);
        }
        else {
            generateKingMoves(piece);
        }
    }

    // -------------------------------------------------
    //           generateMoves Helper Methods
    // -------------------------------------------------

    private void generatePawnMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();
        GamePiece probe;

        if (piece.getColor().equals("white")) {
            // This checks if the pawn can move forward
            if (playSpace[letter][number + 1] == null) {
                if (playSpace[letter][number + 2] == null && !piece.hasMoved()) {
                    if (kingChecker(piece, letter, number + 1) && kingChecker(piece, letter, number + 2)) {
                        piece.addMove(letter, number + 1);
                        piece.addMove(letter, number + 2);
                    }
                }
                else {
                    if (kingChecker(piece, letter, number + 1)) {
                        piece.addMove(letter, number + 1);
                    }
                }
            }

            // Possible idea to simplify the following if statements
            for (int i = letter - 1; i < letter + 3; i += 2) {
                if (inBounds(i) && inBounds(number + 1)) {
                    probe = playSpace[i][number + 1];
                    if (probe != null) {
                        if (!probe.getType().equals("king") && !probe.getColor().equals(piece.getColor())) {
                            if (kingChecker(piece, i, number + 1)) {
                                piece.addMove(i, number + 1);
                            }
                        }
                    }
                }
            }

            // This checks if the pawn can capture a piece to the left
            /*if (inBounds(letter - 1) && inBounds(number + 1)) {
                if (playSpace[letter - 1][number + 1] != null && !playSpace[letter - 1][number + 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter - 1, number + 1);
                }
            }

            // This checks if the pawn can capture a piece to the right
            if (inBounds(letter + 1) && inBounds(number + 1)) {
                if (playSpace[letter + 1][number + 1] != null && !playSpace[letter + 1][number + 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter + 1, number + 1);
                }
            }*/
        }
        else if (piece.getColor().equals("black")) {
            // This checks if the pawn can move forward
            if (playSpace[letter][number - 1] == null) {
                if (playSpace[letter][number - 2] == null && !piece.hasMoved()) {
                    if (kingChecker(piece, letter, number - 1) && kingChecker(piece, letter, number - 2)) {
                        piece.addMove(letter, number - 1);
                        piece.addMove(letter, number - 2);
                    }
                }
                else {
                    if (kingChecker(piece, letter, number - 1)) {
                        piece.addMove(letter, number - 1);
                    }
                }
            }

            for (int i = letter - 1; i < letter + 3; i += 2) {
                if (inBounds(i) && inBounds(number - 1)) {
                    probe = playSpace[i][number - 1];

                    if (probe != null) {
                        if (!probe.getType().equals("king") && !probe.getColor().equals(piece.getColor())) {
                            if (kingChecker(piece, i, number - 1)) {
                                piece.addMove(i, number - 1);
                            }
                        }
                    }
                }
            }

            // This checks if the pawn can capture a piece to the left
            /*if (inBounds(letter - 1) && inBounds(number - 1)) {
                if (playSpace[letter - 1][number - 1] != null && !playSpace[letter - 1][number - 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter - 1, number - 1);
                }
            }

            // This checks if the pawn can capture a piece to the right
            if (inBounds(letter + 1) && inBounds(number - 1)) {
                if (playSpace[letter + 1][number - 1] != null && !playSpace[letter + 1][number - 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter + 1, number - 1);
                }
            }*/
        }
    }

    private void generateRookMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();

        // The following four for loops find possible moves in the directions of East, West, North, and South respectively
        // The for loops make sure the move is in bounds as a result of how they are setup
        for (int i = letter + 1; i < 8; i += 1) {
            if (playSpace[i][number] != null) {
                if (!playSpace[i][number].getType().equals("king") && !playSpace[i][number].getColor().equals(piece.getColor())) {
                    if (kingChecker(piece, i, number)) {
                        piece.addMove(i, number);
                    }
                }

                break;
            }
            else {
                if (kingChecker(piece, i, number)) {
                    piece.addMove(i, number);
                }
            }
        }

        for (int i = letter - 1; i > -1; i -= 1) {
            if (playSpace[i][number] != null) {
                if (!playSpace[i][number].getType().equals("king") && !playSpace[i][number].getColor().equals(piece.getColor())) {
                    if (kingChecker(piece, i, number)) {
                        piece.addMove(i, number);
                    }
                }

                break;
            }
            else {
                if (kingChecker(piece, i, number)) {
                    piece.addMove(i, number);
                }
            }
        }

        for (int i = number + 1; i < 8; i += 1) {
            if (playSpace[letter][i] != null) {
                if (!playSpace[letter][i].getType().equals("king") && !playSpace[letter][i].getColor().equals(piece.getColor())) {
                    if (kingChecker(piece, letter, i)) {
                        piece.addMove(letter, i);
                    }
                }

                break;
            }
            else {
                if (kingChecker(piece, letter, i)) {
                    piece.addMove(letter, i);
                }
            }
        }

        for (int i = number - 1; i > -1; i -= 1) {
            if (playSpace[letter][i] != null) {
                if (!playSpace[letter][i].getType().equals("king") && !playSpace[letter][i].getColor().equals(piece.getColor())) {
                    if (kingChecker(piece, letter, i)) {
                        piece.addMove(letter, i);
                    }
                }

                break;
            }
            else {
                if (kingChecker(piece, letter, i)) {
                    piece.addMove(letter, i);
                }
            }
        }
    }

    private void generateKnightMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();

        // This one is a little tricky. I used some symmetry to condense this part. If you read through the loop statements it makes sense
        for (int i = letter - 2; i < letter + 3; i += 4) {
            for (int j = number - 1; j < number + 2; j += 2) {
                if (inBounds(i) && inBounds(j)) {
                    if (playSpace[i][j] != null) {
                        if (!playSpace[i][j].getType().equals("king") && !playSpace[i][j].getColor().equals(piece.getColor())) {
                            if (kingChecker(piece, i, j)) {
                                piece.addMove(i, j);
                            }
                        }
                    }
                    else {
                        if (kingChecker(piece, i, j)) {
                            piece.addMove(i, j);
                        }
                    }
                }
            }
        }

        for (int i = letter - 1; i < letter + 2; i += 2) {
            for (int j = number - 2; j < number + 3; j += 4) {
                if (inBounds(i) && inBounds(j)) {
                    if (playSpace[i][j] != null) {
                        if (!playSpace[i][j].getType().equals("king") && !playSpace[i][j].getColor().equals(piece.getColor())) {
                            if (kingChecker(piece, i, j)) {
                                piece.addMove(i, j);
                            }
                        }
                    }
                    else {
                        if (kingChecker(piece, i, j)) {
                            piece.addMove(i, j);
                        }
                    }
                }
            }
        }
    }

    private void generateBishopMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();

        // I uhhh. I don't really know. I just kept typing and it seemed correct
        // I was trying to use symmetry again
        for (int i = letter + 1, j = 1; i < 8; i += 1, j += 1) {
            if (inBounds(i) && inBounds(number + j)) {
                if (playSpace[i][number + j] != null) {
                    if (!playSpace[i][number + j].getType().equals("king") && !playSpace[i][number + j].getColor().equals(piece.getColor())) {
                        if (kingChecker(piece, i, number + j)) {
                            piece.addMove(i, number + j);
                        }
                    }

                    break;
                }
                else {
                    if (kingChecker(piece, i, number + j)) {
                        piece.addMove(i, number + j);
                    }
                }
            }
        }

        for (int i = letter + 1, j = 1; i < 8; i += 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                if (playSpace[i][number - j] != null) {
                    if (!playSpace[i][number - j].getType().equals("king") && !playSpace[i][number - j].getColor().equals(piece.getColor())) {
                        if (kingChecker(piece, i, number - j)) {
                            piece.addMove(i, number - j);
                        }
                    }

                    break;
                }
                else {
                    if (kingChecker(piece, i, number - j)) {
                        piece.addMove(i, number - j);
                    }
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number + j)) {
                if (playSpace[i][number + j] != null) {
                    if (!playSpace[i][number + j].getType().equals("king") && !playSpace[i][number + j].getColor().equals(piece.getColor())) {
                        if (kingChecker(piece, i, number + j)) {
                            piece.addMove(i, number + j);
                        }
                    }

                    break;
                }
                else {
                    if (kingChecker(piece, i, number + j)) {
                        piece.addMove(i, number + j);
                    }
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                if (playSpace[i][number - j] != null) {
                    if (!playSpace[i][number - j].getType().equals("king") && !playSpace[i][number - j].getColor().equals(piece.getColor())) {
                        if (kingChecker(piece, i, number - j)) {
                            piece.addMove(i, number - j);
                        }
                    }

                    break;
                }
                else {
                    if (kingChecker(piece, i, number - j)) {
                        piece.addMove(i, number - j);
                    }
                }
            }
        }
    }

    private void generateKingMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();
        GamePiece ghostPiece;

        // I need to come back here. I think I can make this better, but I am tired. I don't know what I am doiiibng
        // Avoid 0 0 combination. Possible combinations {1 -1, 1 0, 1 1, 0 -1, **0 0**, 0 1, -1 -1, -1 0, -1 1} **SKIP THIS ONE
        for (int i = -1; i < 2; i += 1) {
            for (int j = -1; j < 2; j += 1) {
                if (i != 0 || j != 0) { // This skips the spot the king occupies
                    ghostPiece = new GamePiece(piece.getOwner(), "king", letter + i, number + j);
                    if (inBounds(letter + i) && inBounds(number + j)) {
                        if (playSpace[letter + i][number + j] != null) {
                            if (!playSpace[letter + i][number + j].getType().equals("king") && kingChecker(makeGhostPlaySpace(piece, letter + i, number + j), ghostPiece) && !playSpace[letter + i][number + j].getColor().equals(piece.getColor())) {
                                piece.addMove(letter + i, number + j);
                            }
                        } else {
                            if (kingChecker(makeGhostPlaySpace(piece, letter + i, number + j), ghostPiece)) {
                                piece.addMove(letter + i, number + j);
                            }
                        }
                    }
                }
            }
        }
    }

    //This method creates a alternate play space for use with the king checker
    // Removes the piece being moved and places a new piece in the proposed position
    private GamePiece[][] makeGhostPlaySpace (GamePiece realPiece, int ghostLetter, int ghostNumber) {
        GamePiece[][] ghostPlaySpace = new GamePiece[8][8];

        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                ghostPlaySpace[i][j] = playSpace[i][j];
            }
        }

        ghostPlaySpace[realPiece.getPositionLetter()][realPiece.getPositionNumber()] = null;
        ghostPlaySpace[ghostLetter][ghostNumber] = new GamePiece(realPiece.getOwner(), realPiece.getType(), ghostLetter, ghostNumber);

        return ghostPlaySpace;
    }

    // This method checks if the proposed move is valid in terms of king safety
    private boolean kingChecker (GamePiece realPiece, int ghostLetter, int ghostNumber) {
        GamePiece[][] ghostPlaySpace = makeGhostPlaySpace(realPiece, ghostLetter, ghostNumber);

        return kingChecker(ghostPlaySpace, findPiece(realPiece.getOwner(), "king")[0]);
    }

    // This takes a proposed position in the form of a 'ghost' piece and sends out probes to see if the king would be in check
    // The method returns true if the proposed position is not in check
    // The ghostPlaySpace is only used when checking if other pieces may move away from the king. Otherwise it is equal to the real playSpace
    // This is a big fucking method. There is probably a better way to do this, but I can't think of it at the moment
    private boolean kingChecker (GamePiece[][] ghostPlaySpace, GamePiece ghostPiece) {
        int letter = ghostPiece.getPositionLetter();
        int number = ghostPiece.getPositionNumber();
        String color = ghostPiece.getColor();
        GamePiece probe;

        // First I check the cardinal directions by modifying the generateRookMoves method
        for (int i = letter + 1; i < 8; i += 1) {
            probe = ghostPlaySpace[i][number];
            // This triggers if the probe finds a ghostPiece
            if (probe != null) {
                // This part checks if the ghostPiece being probed is attacking the original square
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = letter - 1; i > -1; i -= 1) {
            probe = ghostPlaySpace[i][number];
            if (probe != null) {
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = number + 1; i < 8; i += 1) {
            probe = ghostPlaySpace[letter][i];
            if (probe != null) {
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = number - 1; i > -1; i -= 1) {
            probe = ghostPlaySpace[letter][i];
            if (probe != null) {
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        // Next I check the diagonals by modifying the generateBishopMoves method
        for (int i = letter + 1, j = 1; i < 8; i += 1, j += 1) {
            if (inBounds(i) && inBounds(number + j)) {
                probe = ghostPlaySpace[i][number + j];
                if (probe != null) {
                    if ((probe.getType().equals("bishop") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                        return false;
                    }

                    break;
                }
            }
        }

        for (int i = letter + 1, j = 1; i < 8; i += 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                probe = ghostPlaySpace[i][number - j];
                if (probe != null) {
                    if ((probe.getType().equals("bishop") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                        return false;
                    }

                    break;
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number + j)) {
                probe = ghostPlaySpace[i][number + j];
                if (probe != null) {
                    if ((probe.getType().equals("bishop") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                        return false;
                    }

                    break;
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                probe = ghostPlaySpace[i][number - j];
                if (probe != null) {
                    if ((probe.getType().equals("bishop") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                        return false;
                    }

                    break;
                }
            }
        }

        // Next are knight positions
        for (int i = letter - 2; i < letter + 3; i += 4) {
            for (int j = number - 1; j < number + 2; j += 2) {
                if (inBounds(i) && inBounds(j)) {
                    probe = ghostPlaySpace[i][j];
                    if (probe != null) {
                        if (probe.getType().equals("knight") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int i = letter - 1; i < letter + 2; i += 2) {
            for (int j = number - 2; j < number + 3; j += 4) {
                if (inBounds(i) && inBounds(j)) {
                    probe = ghostPlaySpace[i][j];
                    if (probe != null) {
                        if (probe.getType().equals("knight") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        // Lastly is the pawn check. This one is just raw
        if (ghostPiece.getColor().equals("white")) {
            for (int i = letter - 1; i < letter + 3; i += 2) {
                if (inBounds(i) && inBounds(number + 1)) {
                    probe = ghostPlaySpace[i][number + 1];
                    if (probe != null) {
                        if (probe.getType().equals("pawn") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }
        else {
            for (int i = letter - 1; i < letter + 3; i += 2) {
                if (inBounds(i) && inBounds(number - 1)) {
                    probe = ghostPlaySpace[i][number - 1];
                    if (probe != null) {
                        if (probe.getType().equals("pawn") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        // If the method makes it here this means that the king is okay
        return true;
    }

    // ------------------------------------------
    //       Miscellaneous Helper Methods
    // ------------------------------------------

    private GamePiece[] findPiece (Player owner, String type) {
        GamePiece[] pieces = new GamePiece[2];
        int index = 0;

        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                if (playSpace[i][j] != null) {
                    if (playSpace[i][j].getOwner().equals(owner) && playSpace[i][j].getType().equals(type)) {
                        pieces[index] = playSpace[i][j];
                        index += 1;
                    }
                }
            }
        }

        return pieces;
    }

    private boolean inBounds (int x) {
        return x < 8 && x > -1;
    }

    private int letterToInt (char a) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('A', 0);
        map.put('B', 1);
        map.put('C', 2);
        map.put('D', 3);
        map.put('E', 4);
        map.put('F', 5);
        map.put('G', 6);
        map.put('H', 7);

        return map.get(a);
    }

    private String charToType (char typeChar) {
        Map<Character, String> map = new HashMap<>();
        map.put('K', "king");
        map.put('Q', "queen");
        map.put('B', "bishop");
        map.put('N', "knight");
        map.put('R', "rook");
        map.put('P', "pawn");

        return map.get(typeChar);
    }

    private char typeToChar (String type) {
        Map<String, Character> map = new HashMap<>();
        map.put("king", 'K');
        map.put("queen",'Q');
        map.put("bishop",'B');
        map.put("knight",'N');
        map.put("rook", 'R');
        map.put("pawn", 'P');

        return map.get(type);
    }

    public String toString () {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        StringBuilder result = new StringBuilder();

        for (int i = -1; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                if (i == -1) {
                    result.append(" ").append(numbers[j]).append("   ");
                }
                else if (playSpace[i][j] != null) {
                    if (j != 7) {
                        result
                                .append(typeToChar(playSpace[i][j].getType())).append(playSpace[i][j].positionToString()).append(playSpace[i][j].colorToChar()).append("|");
                    }
                    else {
                        result
                                .append(typeToChar(playSpace[i][j].getType())).append(playSpace[i][j].positionToString()).append(playSpace[i][j].colorToChar()).append("|").append(letters[i]);
                    }
                }
                else {
                    result.append("    ").append("|");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }

    private void printToast(String m){
        Toast t = Toast.makeText(c, m,Toast.LENGTH_LONG);
        t.show();
    }

    public GamePiece getPiece(int letter, int number){
        return playSpace[letter][number];
    }
}
