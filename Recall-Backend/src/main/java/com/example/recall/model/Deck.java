package com.example.recall.model;

import java.util.ArrayList;
import java.util.Objects;

public class Deck {
    private String name;
    private ArrayList<Card> cards;
    

    public Deck() {
    }

    public Deck(String name, ArrayList<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, cards);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", cards='" + getCards() + "'" +
            "}";
    }

}
