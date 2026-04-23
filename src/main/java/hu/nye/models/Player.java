package hu.nye.models;

public class Player {
    private final String name;
    private final char disc;

    public Player(String name, char disc) {
        this.name = name;
        this.disc = disc;
    }

    public String getName() {
        return name;
    }

    public char getDisc() {
        return disc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ").append(this.name).append(" disc: ").append(this.disc);
        return sb.toString();
    }
}
