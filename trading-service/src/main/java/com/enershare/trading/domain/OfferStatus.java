package com.enershare.trading.domain;

public enum OfferStatus {
    OPEN,       // L'offre est en ligne, personne ne l'a encore achetée
    COMPLETED,  // Vendue ! (Matché avec un Bid)
    CANCELLED   // Le vendeur a annulé l'offre
}
