package com.dcms.Multi_Tenancy_Demo.Exception;

public class CollectionNotFoundException extends RuntimeException{
    public CollectionNotFoundException(String message){
        super(message);
    }
}
