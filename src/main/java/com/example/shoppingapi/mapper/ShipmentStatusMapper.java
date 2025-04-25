package com.example.shoppingapi.mapper;

public class ShipmentStatusMapper {

    public static String getStatusDescription(int code) {
        return switch (code) {
            case 1 -> "Pending";
            case 2 -> "Picked Up";
            case 3 -> "In Transit";
            case 4 -> "Arrived at Hub";
            case 5 -> "Out for Delivery";
            case 6 -> "Delivered";
            case 7 -> "Failed Delivery";
            case 8 -> "Returned";
            case 9 -> "Delivered to Pickup Point";
            case 10 -> "Delayed";
            case 11 -> "Customs Clearance";
            case 12 -> "Held for Inspection";
            case 13 -> "In Warehouse";
            case 14 -> "Lost";
            case 15 -> "Damaged";
            case 16 -> "Awaiting Pickup";
            default -> "Unknown Status Code";
        };
    }
}
