package com.scarredions.stab;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class STDataController {
    
    private final NumberFormat formatter = NumberFormat.getCurrencyInstance();
    
    private HashMap<Integer, HashSet<Integer>> personToSelections;
    private int currentPersonId;
    private int nextPersonId = 0;
    
    private ArrayList<MenuItem> menuItems;
    
    private LinearLayout totalView;
    
    public STDataController() {
        personToSelections = new HashMap<Integer, HashSet<Integer>>();
        menuItems = new ArrayList<MenuItem>();
    }
    
    public HashSet<Integer> getPersonSelections(int personId) {
        return personToSelections.get(Integer.valueOf(personId));
    }
    
    public HashSet<Integer> getCurrentPersonSelections() {
        return getPersonSelections(currentPersonId);
    }
    
    public Iterator<HashSet<Integer>> getPersonSelectionsIter() {
        return personToSelections.values().iterator();
    }
    
    public int getCurrentPersonId() {
        return currentPersonId;
    }
    
    public boolean currentPersonHasSelected(int menuListPosition) {
        Set<Integer> selections = getCurrentPersonSelections();
        return selections.contains(Integer.valueOf(menuListPosition));
    }
    
    public int getNumberOfPeopleWithSelection(int menuListPosition) {
        int counter = 0;
        Iterator<HashSet<Integer>> selectionsIter = getPersonSelectionsIter();
        HashSet<Integer> selections;
        Integer integer;
        
        while(selectionsIter.hasNext()) {
            selections = selectionsIter.next(); 
            Iterator<Integer> integerIter = selections.iterator();
            while(integerIter.hasNext()) {
                integer = integerIter.next();
                if (integer.intValue() == menuListPosition)
                    counter++;
            }
        }
        
        return counter;
    }
    
    public Double getTotal() {
        double total = 0;
        Set<Integer> selections = getCurrentPersonSelections();
        Iterator<Integer> selectionsIter = selections.iterator();
        Integer integer;
        while(selectionsIter.hasNext()) {
            integer = selectionsIter.next();
            total += getMenuItemPrice(integer.intValue()).doubleValue() /
                getNumberOfPeopleWithSelection(integer.intValue());
        }
        return total;
    }
    
    public MenuItem getMenuItem(int position) {
        return menuItems.get(position);
    }
    
    public String getMenuItemName(int position) {
        return menuItems.get(position).getName();
    }
    
    public Double getMenuItemPrice(int position) {
        return menuItems.get(position).getPrice();
    }
    
    public String getFormattedPrice(Double price) {
        return formatter.format(price.doubleValue());
    }
    
    public int getMenuItemCount() {
        return menuItems.size();
    }
    
    public View getTotalView() {
        return totalView;
    }
    
    public void addPerson(String name) {
        personToSelections.put(Integer.valueOf(nextPersonId), new HashSet<Integer>());
        nextPersonId++;
    }
    
    public void addMenuItem(String name, Double price) {
        menuItems.add(new MenuItem(name, price));
    }
    
    public void setTotalView(LinearLayout totalView) {
        this.totalView = totalView;
    }
    
    public void setCurrentPersonId(int personId) {
        currentPersonId = personId;
    }
    
    public void setSelection(int menuListPosition, boolean checked) {
        HashSet<Integer> selections = getCurrentPersonSelections();
        if (checked) {
            selections.add(Integer.valueOf(menuListPosition));
        } else {
            selections.remove(Integer.valueOf(menuListPosition));
        }
    }
    
    public void updateTotal() {
        TextView total = (TextView) totalView.getChildAt(1);
        total.setText(getFormattedPrice(getTotal()));
    }
    
    public class MenuItem {
        private String name;
        private Double price;
        
        public MenuItem(String name, Double price) {
            this.name = name;
            this.price = price;
        }
        
        public String getName() {
            return name;
        }
        
        public Double getPrice() {
            return price;
        }
    }
}