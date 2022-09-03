package com.codetek.lottaryapp.Models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.codetek.lottaryapp.R;

public class ZodanSymbols {
    public static Drawable getSymbol(String key, Context context){
        Drawable symbol=null;

        switch (key){
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "TAURUS":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "GEMINI":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "CANCER":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "LEO":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
            case "ARIES":
                symbol=context.getDrawable(R.drawable.symbol_aries);
                break;
        }

        return symbol;
    }
}
