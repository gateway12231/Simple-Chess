public class Board {
    private Player whitePlayer;
    private Player blackPlayer;
    // This represents the board in a 8X8 array. All empty spaces are null. The first index is the letter and the second is the number
    private GamePiece[][] playSpace;

    public Board (Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        playSpace = new GamePiece[8][8];
        initializePlaySpace();
        // This was used to test move generation
        /*playSpace = new GamePiece[][]
                       {{null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, new GamePiece(whitePlayer, "queen", 2, 3), null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}};*/
    }

    public void testingMethod () {
        GamePiece piece = playSpace[2][3];
        generateMoves(piece);
        System.out.println(piece.allMovesToString());
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

    public void makeMove (String move) {
        char type = move.charAt(0);
        int startLetter = letterToInt(move.charAt(1));
        int startNumber = move.charAt(2);
        int endLetter = letterToInt(move.charAt(4));
        int endNumber = move.charAt(5);

        //This
        /*if (type == 'P') {

        }
        else if (type == 'R') {

        }
        else if (type == 'N') {

        }
        else if (type == 'B') {

        }
        else if (type == 'Q') {

        }
        else if (type == 'K') {

        }
        else {
            throw new IllegalArgumentException(move.charAt(0) + " is not a valid piece label");
        }*/
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

        if (piece.getColor().equals("white")) {
            // This checks if the pawn can move forward
            if (playSpace[letter][number + 1] == null) {
                if (playSpace[letter][number + 2] == null && !piece.hasMoved()) {
                    piece.addMove(letter, number + 1);
                    piece.addMove(letter, number + 2);
                }
                else {
                    piece.addMove(letter, number + 1);
                }
            }

            // Possible idea to simplify the following if statements
            /*GamePiece probe;

            for (int i = number - 1; i < 2; i += 2) {
                probe = playSpace[letter + 1][i];
                if (probe != null) {
                    if (probe.getType().equals("pawn") && !probe.getColor().equals(piece.getColor())) {
                        piece.addMove(letter + 1, i);
                    }
                }
            }*/

            // This checks if the pawn can capture a piece to the left
            if (inBounds(letter - 1) && inBounds(number + 1)) {
                if (playSpace[letter - 1][number + 1] != null && !playSpace[letter - 1][number + 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter - 1, number + 1);
                }
            }

            // This checks if the pawn can capture a piece to the right
            if (inBounds(letter + 1) && inBounds(number + 1)) {
                if (playSpace[letter + 1][number + 1] != null && !playSpace[letter + 1][number + 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter + 1, number + 1);
                }
            }
        }
        else if (piece.getColor().equals("black")) {
            // This checks if the pawn can move forward
            if (playSpace[letter][number - 1] == null) {
                if (playSpace[letter][number - 2] == null && !piece.hasMoved()) {
                    piece.addMove(letter, number - 1);
                    piece.addMove(letter, number - 2);
                }
                else {
                    piece.addMove(letter, number - 1);
                }
            }

            // This checks if the pawn can capture a piece to the left
            if (inBounds(letter - 1) && inBounds(number - 1)) {
                if (playSpace[letter - 1][number - 1] != null && !playSpace[letter - 1][number - 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter - 1, number - 1);
                }
            }

            // This checks if the pawn can capture a piece to the right
            if (inBounds(letter + 1) && inBounds(number - 1)) {
                if (playSpace[letter + 1][number - 1] != null && !playSpace[letter + 1][number - 1].getColor().equals(piece.getColor())) {
                    piece.addMove(letter + 1, number - 1);
                }
            }
        }
    }

    private void generateRookMoves (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();

        // The following four for loops find possible moves in the directions of East, West, North, and South respectively
        // The for loops make sure the move is in bounds as a result of how they are setup
        for (int i = letter + 1; i < 8; i += 1) {
            if (playSpace[i][number] != null) {
                if (!playSpace[i][number].getColor().equals(piece.getColor())) {
                    piece.addMove(i, number);
                }

                break;
            }
            else {
                piece.addMove(i, number);
            }
        }

        for (int i = letter - 1; i > -1; i -= 1) {
            if (playSpace[i][number] != null) {
                if (!playSpace[i][number].getColor().equals(piece.getColor())) {
                    piece.addMove(i, number);
                }

                break;
            }
            else {
                piece.addMove(i, number);
            }
        }

        for (int i = number + 1; i < 8; i += 1) {
            if (playSpace[letter][i] != null) {
                if (!playSpace[letter][i].getColor().equals(piece.getColor())) {
                    piece.addMove(letter, i);
                }

                break;
            }
            else {
                piece.addMove(letter, i);
            }
        }

        for (int i = number - 1; i > -1; i -= 1) {
            if (playSpace[letter][i] != null) {
                if (!playSpace[letter][i].getColor().equals(piece.getColor())) {
                    piece.addMove(letter, i);
                }

                break;
            }
            else {
                piece.addMove(letter, i);
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
                        if (!playSpace[i][j].getColor().equals(piece.getColor())) {
                            piece.addMove(i, j);
                        }
                    }
                    else {
                        piece.addMove(i, j);
                    }
                }
            }
        }

        for (int i = letter - 1; i < letter + 2; i += 2) {
            for (int j = number - 2; j < number + 3; j += 4) {
                if (inBounds(i) && inBounds(j)) {
                    if (playSpace[i][j] != null) {
                        if (!playSpace[i][j].getColor().equals(piece.getColor())) {
                            piece.addMove(i, j);
                        }
                    }
                    else {
                        piece.addMove(i, j);
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
                    if (!playSpace[i][number + j].getColor().equals(piece.getColor())) {
                        piece.addMove(i, number + j);
                    }

                    break;
                }
                else {
                    piece.addMove(i, number + j);
                }
            }
        }

        for (int i = letter + 1, j = 1; i < 8; i += 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                if (playSpace[i][number - j] != null) {
                    if (!playSpace[i][number - j].getColor().equals(piece.getColor())) {
                        piece.addMove(i, number - j);
                    }

                    break;
                }
                else {
                    piece.addMove(i, number - j);
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number + j)) {
                if (playSpace[i][number + j] != null) {
                    if (!playSpace[i][number + j].getColor().equals(piece.getColor())) {
                        piece.addMove(i, number + j);
                    }

                    break;
                }
                else {
                    piece.addMove(i, number + j);
                }
            }
        }

        for (int i = letter - 1, j = 1; i > -1; i -= 1, j += 1) {
            if (inBounds(i) && inBounds(number - j)) {
                if (playSpace[i][number - j] != null) {
                    if (!playSpace[i][number - j].getColor().equals(piece.getColor())) {
                        piece.addMove(i, number - j);
                    }

                    break;
                }
                else {
                    piece.addMove(i, number - j);
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
                    ghostPiece = new GamePiece(piece.getOwner(), "ghost", letter + i, number + j);

                    if (playSpace[letter + i][number + j] != null) {
                        if (kingChecker(ghostPiece) && !playSpace[letter + i][number + j].getColor().equals(piece.getColor())) {
                            piece.addMove(letter + i, number + j);
                        }
                    }
                    else {
                        if (kingChecker(ghostPiece)) {
                            piece.addMove(letter + i, number + j);
                        }
                    }
                }
            }
        }
    }

    // This takes a proposed position in the form of a 'ghost' piece and sends out probes to see if the king would be in check
    // The method returns true if the proposed position is not in check
    // This is a big fucking method. There is probably a better way to do this, but I can't think of it at the moment
    private boolean kingChecker (GamePiece piece) {
        int letter = piece.getPositionLetter();
        int number = piece.getPositionNumber();
        String color = piece.getColor();
        GamePiece probe;

        // First I check the cardinal directions by modifying the generateRookMoves method
        for (int i = letter + 1; i < 8; i += 1) {
            probe = playSpace[i][number];
            // This triggers if the probe finds a piece
            if (probe != null) {
                // This part checks if the piece being probed is attacking the original square
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = letter - 1; i > -1; i -= 1) {
            probe = playSpace[i][number];
            if (probe != null) {
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = number + 1; i < 8; i += 1) {
            probe = playSpace[letter][i];
            if (probe != null) {
                if ((probe.getType().equals("rook") || probe.getType().equals("queen")) && !probe.getColor().equals(color)) {
                    return false;
                }

                break;
            }
        }

        for (int i = number - 1; i > -1; i -= 1) {
            probe = playSpace[letter][i];
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
                probe = playSpace[i][number + j];
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
                probe = playSpace[i][number - j];
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
                probe = playSpace[i][number + j];
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
                probe = playSpace[i][number - j];
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
                    probe = playSpace[i][j];
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
                    probe = playSpace[i][j];
                    if (probe != null) {
                        if (probe.getType().equals("knight") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        // Lastly is the pawn check. This one is just raw
        if (piece.getColor().equals("white")) {
            for (int i = letter - 1; i < 2; i += 2) {
                if (inBounds(i) && inBounds(number + 1)) {
                    probe = playSpace[i][number + 1];
                    if (probe != null) {
                        if (probe.getType().equals("pawn") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }
        else {
            for (int i = letter - 1; i < 2; i += 2) {
                if (inBounds(i) && inBounds(number - 1)) {
                    probe = playSpace[i][number - 1];
                    if (probe != null) {
                        if (probe.getType().equals("pawn") && !probe.getColor().equals(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    // ------------------------------------------
    //       Miscellaneous Helper Methods
    // ------------------------------------------

    private boolean inBounds (int x) {
        return x < 8 && x > -1;
    }

    private int letterToInt (char a) {
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (int i = 0; i < 8; i += 1) {
            if (a == letters[i]) {
                return i;
            }
        }

        return -1;
    }

    public String toString () {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 8; j += 1) {
                if (playSpace[i][j] != null) {
                    result.append(playSpace[i][j].positionToString()).append(" ").append(playSpace[i][j].getType()).append(" | ");
                }
                else {
                    result.append("null").append(" | ");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }
}
