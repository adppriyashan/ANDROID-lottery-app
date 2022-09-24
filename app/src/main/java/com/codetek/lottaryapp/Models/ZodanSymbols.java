package com.codetek.lottaryapp.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.codetek.lottaryapp.R;

public class ZodanSymbols {
    public static Drawable getSymbol(String key, Context context){
        Drawable symbol=null;
        switch (key){
            case "AQUARIUS":
                symbol=context.getDrawable(R.drawable.symbol_aquarius);
                break;
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "CANCER":
                symbol=context.getDrawable(R.drawable.symbol_cancer);
                break;
            case "CAPRICORN":
                symbol=context.getDrawable(R.drawable.symbol_capricorn);
                break;
            case "GEMINI":
                symbol=context.getDrawable(R.drawable.symbol_gemini);
                break;
            case "LEO":
                symbol=context.getDrawable(R.drawable.symbol_leo);
                break;
            case "LIBRA":
                symbol=context.getDrawable(R.drawable.symbol_libra);
                break;
            case "PISCES":
                symbol=context.getDrawable(R.drawable.symbol_pisces);
                break;
            case "SAGITTARIUS":
                symbol=context.getDrawable(R.drawable.symbol_sagittarius);
                break;
            case "SCORPIO":
                symbol=context.getDrawable(R.drawable.symbol_scorpio);
                break;
            case "TAURUS":
                symbol=context.getDrawable(R.drawable.symbol_taurus);
                break;
            case "VIRGO":
                symbol=context.getDrawable(R.drawable.symbol_virgo);
                break;
        }

        return symbol;
    }
}
