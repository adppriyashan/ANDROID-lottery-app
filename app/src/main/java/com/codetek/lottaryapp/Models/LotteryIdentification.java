package com.codetek.lottaryapp.Models;

import android.content.Context;
import android.widget.Toast;

import com.codetek.lottaryapp.Models.DB.Lottery;

import java.net.URL;

import commons.validator.routines.UrlValidator;

public class LotteryIdentification {

    UrlValidator urlValidator;

    public Lottery checkLottery(Context context, String content){

        Lottery lottery=new Lottery();
        urlValidator=new UrlValidator();

        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

        if(content.contains("www.dlb.lk")){
            int drawNumberStartIndex=0;
            int nameSplitLength=0;
            boolean drawerNumberDetected=false;
            boolean dateDetected=false;
            boolean serialDetected=false;
            int numbersCheck=1;
            int index=0;

            for (String sector : content.split(" ")){
                if(!isNumeric(sector) && drawerNumberDetected==false && (drawNumberStartIndex==0 || drawNumberStartIndex==1 || drawNumberStartIndex==2)){
                    nameSplitLength++;
                    lottery.setName(((lottery.getName()==null)?"":lottery.getName()).concat(" "+sector));
                    drawNumberStartIndex++;
                }else if(isNumeric(sector) && drawerNumberDetected==false){
                    lottery.setName(lottery.getName().trim());
                    drawerNumberDetected=true;
                    lottery.setDraw_no(sector);
                    drawNumberStartIndex++;
                }else if(drawerNumberDetected==true && dateDetected==false){
                    lottery.setDate(sector);
                    dateDetected=true;
                }else if(dateDetected==true && serialDetected==false){
                    lottery.setSerial(sector);
                    serialDetected=true;
                }else if(serialDetected==true){

                    boolean isFirstLetter=false;

                    if(!isNumeric(sector) && lottery.getLetter()==null){
                        lottery.setLetter(sector);
                        nameSplitLength++;
                    }

                    if(isNumeric(sector) && (index==(nameSplitLength+3) || index==(nameSplitLength+4) || index==(nameSplitLength+5) || index==(nameSplitLength+6))){
                        if(numbersCheck==1){
                            lottery.setNumber1(sector);
                        }
                        if(numbersCheck==2){
                            lottery.setNumber2(sector);
                        }
                        if(numbersCheck==3){
                            lottery.setNumber3(sector);
                        }
                        if(numbersCheck==4){
                            lottery.setNumber4(sector);
                        }
                        numbersCheck++;
                    }else{
                        if(!isNumeric(sector) && !isValid(sector)){
                            if(sector.length()==1){
                                lottery.setLetter(sector);
                                break;
                            }else{
                                lottery.setSymbol(sector);
                                break;
                            }
                        }
                    }
                }
                index++;
            }
        }else if(content.contains("https://r.nlb.lk")){

        }else if(content.contains("lotto")){

        }else{
            Toast.makeText(context, "Invalid Ticket", Toast.LENGTH_SHORT).show();
        }

        return lottery;
    }

    private static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    private boolean isNumeric(String textContent){
        try {
            Integer.parseInt(textContent);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
