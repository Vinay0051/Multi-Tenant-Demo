package com.dcms.Multi_Tenancy_Demo.Exception;

public class CollectionOrDbNotFoundException extends RuntimeException{
    public CollectionOrDbNotFoundException(String message){
        super(message);
    }
}
