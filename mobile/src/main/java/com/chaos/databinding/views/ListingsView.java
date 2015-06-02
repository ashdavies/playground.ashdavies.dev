package com.chaos.databinding.views;

public interface ListingsView<T> extends BaseView {
    void addItem(T item);

    void addItems(T[] items);

    void clearItems();
}
