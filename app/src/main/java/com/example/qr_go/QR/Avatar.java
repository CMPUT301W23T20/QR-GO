package com.example.qr_go.QR;

public class Avatar {
    public String generateAvatar(String hash) {
        String hex = hash.substring(0, 6);

        String avatar = "";

        // Use the first 3 hex digits to determine the face shape
        int faceInt = Integer.parseInt(hex.substring(0, 2), 16);
        String faceShape = getFaceShape(faceInt);

        // Use the next 3 hex digits to determine the body and leg features
        int bodyInt = Integer.parseInt(hex.substring(2, 4), 16);
        int legInt = Integer.parseInt(hex.substring(4, 6), 16);

        String body = getBody(bodyInt);
        String legs = getLegs(legInt);

        // Get the length of the longest line in the avatar
        int maxLineLength = Math.max(Math.max(faceShape.length(), body.length()), legs.length());

        // Pad the face shape with spaces to align it to the center
        int facePadding = (maxLineLength - faceShape.length()) / 2;
        avatar += padString(faceShape, facePadding) + "\n";

        // Pad the body with spaces to align it to the center
        int bodyPadding = (maxLineLength - body.length()) / 2;
        avatar += padString(body, bodyPadding) + "\n";

        // Pad the legs with spaces to align it to the center
        int legsPadding = (maxLineLength - legs.length()) / 2;
        avatar += padString(legs, legsPadding);

        return avatar;
    }

    private String padString(String str, int padding) {
        String paddedString = "";
        for (int i = 0; i < padding; i++) {
            paddedString += " ";
        }
        paddedString += str;
        return paddedString;
    }

    private String getFaceShape(int shape) {
        switch (shape % 11) {
            case 0:
                return "  ( o_O )   ";
            case 1:
                return "  ( o.o )  ";
            case 2:
                return "  ( >.< )  ";
            case 3:
                return " @( o_o )@  ";
            case 4:
                return "  ( ._. )   ";
            case 5:
                return "  ( Q_Q )   ";
            case 6:
                return "  ( O O )   ";
            case 7:
                return "  ( -_- )   ";
            case 8:
                return "  ( ^_^ )   ";
            case 9:
                return "  ( >_< )   ";
            case 10:
                return "  (* > *)  ";
            default:
                return "";
        }
    }

    private String getBody(int features) {
        switch (features % 11) {
            case 0:
                return "  |   |  ";
            case 1:
                return "  |===|  ";
            case 2:
                return "  |___|  ";
            case 3:
                return "  /   \\  ";
            case 4:
                return "  [   ]  ";
            case 5:
                return "  {   }  ";
            case 6:
                return "  (   )  ";
            case 7:
                return "  \\   /  ";
            case 8:
                return "  | o |  ";
            case 9:
                return "  / o \\  ";
            case 10:
                return "  ) . (  ";
            default:
                return "";
        }
    }

    private String getLegs(int features) {
        switch (features % 11) {
            case 0:
                return "/   \\";
            case 1:
                return "|   |";
            case 2:
                return "| | |";
            case 3:
                return "\\___/";
            case 4:
                return "|-|-|";
            case 5:
                return "/ | \\";
            case 6:
                return "|___|";
            case 7:
                return "| . |";
            case 8:
                return "\\   /";
            case 9:
                return "|/|\\|";
            case 10:
                return "|>|<|";
            default:
                return "";
        }
    }
}
