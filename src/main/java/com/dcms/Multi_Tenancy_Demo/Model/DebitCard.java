package com.dcms.Multi_Tenancy_Demo.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DebitCard {

    private String cvv;

    private String card_network;

    public DebitCard(String card_network, String cvv) {
        this.card_network = card_network;
        this.cvv = cvv;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public String getCardNetwork() {
        return card_network;
    }


    public String getCard_network() {
        return card_network;
    }

    public void setCard_network(String card_network) {
        this.card_network = card_network;
    }


}
