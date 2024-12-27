package com.test.a7ara.point_of_sale;

public interface PointOfSaleItemClick {

    void OnIncreaseClicked(int pos);
    void OnDecreaseClicked(int pos);

    void OnRowClicked(int pos);

    void OnCountClicked(int pos);
}