package com.nwurth.simplechess;

public class Player {
    private class CaptureNode {
        private GamePiece capturedPiece;
        private CaptureNode next;

        private CaptureNode (GamePiece capturedPiece, CaptureNode next) {
            this.capturedPiece = capturedPiece;
            this.next = next;
        }

        private CaptureNode (GamePiece capturedPiece) {
            this.capturedPiece = capturedPiece;
            this.next = null;
        }
    }

    private String color;
    private String name;
    private CaptureNode top;

    public Player (String color, String name) {
        this.color = color;
        this.name = name;
        top = null;
    }

    public void addCapturedPiece (GamePiece capturedPiece) {
        if (top == null) {
            top = new CaptureNode(capturedPiece);
        }
        else {
            top = new CaptureNode(capturedPiece, top);
        }
    }

    public String getColor () {
        return color;
    }

    public String getName () {
        return name;
    }
}