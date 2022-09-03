package com.codetek.lottaryapp.Models.DB;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.codetek.lottaryapp.R;

public class Lottery {
    private int id;
    private String name,draw_no,number1,number2,number3,number4,symbol,letter,serial,date;
    private boolean isLotto;
    private double price;

    public Lottery() {
    }

    public static Drawable getLotteryImage(Context context, String lotteryName){
        if(lotteryName.toLowerCase().equals("lagna wasana")){
            return context.getDrawable(R.drawable.lagna_wasana);
        }else if(lotteryName.toLowerCase().equals("shanida")){
            return context.getDrawable(R.drawable.shanida);
        }else if(lotteryName.toLowerCase().equals("ADA KOTIPATHI".toLowerCase())){
            return context.getDrawable(R.drawable.ada_kotipathi);
        }else if(lotteryName.toLowerCase().equals("KOTIPATHI KAPRUKA".toLowerCase())){
            return context.getDrawable(R.drawable.kotipathi_kapruka);
        }else{
            return context.getDrawable(R.drawable.lottary);
        }

    }

    public Lottery(int id, String name, String draw_no, String number1, String number2, String number3, String number4, String symbol, String letter, String serial, String date, boolean isLotto, double price) {
        this.id = id;
        this.name = name;
        this.draw_no = draw_no;
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
        this.number4 = number4;
        this.symbol = symbol;
        this.letter = letter;
        this.serial = serial;
        this.date = date;
        this.isLotto = isLotto;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDraw_no() {
        return draw_no;
    }

    public String getNumber1() {
        return number1;
    }

    public String getNumber2() {
        return number2;
    }

    public String getNumber3() {
        return number3;
    }

    public String getNumber4() {
        return number4;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLetter() {
        return letter;
    }

    public String getSerial() {
        return serial;
    }

    public String getDate() {
        return date;
    }

    public boolean isLotto() {
        return isLotto;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDraw_no(String draw_no) {
        this.draw_no = draw_no;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public void setNumber3(String number3) {
        this.number3 = number3;
    }

    public void setNumber4(String number4) {
        this.number4 = number4;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLotto(boolean lotto) {
        isLotto = lotto;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
