package com.dcms.Multi_Tenancy_Demo.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DebitCard {
    @Id
    private String card_id;

    private String cvv;

    @JsonProperty("card_network")
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

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_network() {
        return card_network;
    }

    public void setCard_network(String card_network) {
        this.card_network = card_network;
    }


}
