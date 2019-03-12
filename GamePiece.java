public class GamePiece {
    private class Position { // The position of this piece. Also serves as a node in a singly linked linear list
        private int letter; // The letters A through H, inclusive, are represented by the numbers 0 through 7
        private int number; // The numbers 1 through 8, inclusive, are represented by the numbers 0 through 7
        private Position next;

        private Position (int letter, int number) {
            this.letter = letter;
            this.number = number;
            next = null;
        }

        private Position (int letter, int number, Position next) {
            this.letter = letter;
            this.number = number;
            this.next = next;
        }
    }

    private Player owner;
    private String type;
    private boolean captured = false;
    private boolean hasMovedStatus;
    private Position position;
    private Position top; // The first possible move

    public GamePiece (Player owner, String type, int letter, int number) {
        this.owner = owner;
        this.type = type;
        captured = false;
        hasMovedStatus = false;
        top = null;
        position = new Position(letter, number);
    }

    // I don't think this is needed anymore
    /*private Position getInitialPosition (int initializer) { // This is what actually places pieces on the board
        if (owner.getColor().equals("white")) {
            if (type.equals("pawn")) {
                return new Position(initializer, 1);
            }
            else {
                return new Position(initializer, 0);
            }
        }
        else {
            if (type.equals("pawn")) {
                return new Position(initializer, 6);
            }
            else {
                return new Position(initializer, 7);
            }
        }
    }*/

    public void addMove (int letter, int number) { // Checks if given move is in the bounds of the board and then adds it
        if (isEmpty()) {
            top = new Position(letter, number);
        } else {
            top = new Position(letter, number, top);
        }
    }

    public boolean isMoveValid (int letter, int number) {
        Position temp = top;
        while (temp != null) {
            if (temp.letter == letter && temp.number == number) {
                return true;
            }
            temp = temp.next;
        }

        return false;
    }

    public void clearMoves () {
        top = null;
    }

    public int getPositionLetter () { // Why am I even doing this? Yeah, an int is a better idea
        return position.letter;
    }

    public int getPositionNumber () {
        return position.number;
    }

    private String convertLetter (int letter) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        return letters[letter];
    }

    public String getType () {
        return type;
    }

    public String getColor () {
        return owner.getColor();
    }

    public Player getOwner () {
        return owner;
    }

    private boolean isEmpty () {
        return top == null;
    }

    public boolean hasMoved () {
        return hasMovedStatus;
    }

    public boolean equals (GamePiece right) {
        if (this.owner.equals(right.getOwner()) && this.type.equals(right.getType()) && this.position.letter == right.getPositionLetter() && this.position.number == right.getPositionNumber()) {
            return true;
        }

        return false;
    }

    public String positionToString () {
        StringBuilder result = new StringBuilder();
        result.append(convertLetter(position.letter)).append(position.number + 1);
        return result.toString();
    }

    // Remove this after testing
    public String allMovesToString () {
        Position current = top;
        StringBuilder result = new StringBuilder();
        while (current != null) {
            result.append(convertLetter(current.letter)).append(current.number + 1).append(" | ");
            current = current.next;
        }

        return result.toString();
    }
}