package com.example.shoppingapi.modelhelper;
import com.example.shoppingapi.model.Store;

public class StoreModelHelper implements ModelHelper<Store> {

    private final Long storeId1 = 1L;
    private final String storename1 = "Pet shop";
    private final String storedesc1 = "El yapping";

    private final Long storeId2 = 2L;
    private final String storename2 = "Book store";
    private final String storedesc2 = "(.... aja sendiri)";

    public StoreModelHelper(){

    }

        @Override
    public Store createModel(Integer num){
        if (num == 1) {
            Store store1 = new Store();
            store1.setStoreId(storeId1);
            store1.setStoreName(storename1);
            store1.setStoreDescription(storedesc1);
            return store1;
        } else {
            Store store2 = new Store();
            store2.setStoreId(storeId2);
            store2.setStoreName(storename2);
            store2.setStoreDescription(storedesc2);
            return store2;
        }
    }



}