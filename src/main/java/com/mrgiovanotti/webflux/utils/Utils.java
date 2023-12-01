package com.mrgiovanotti.webflux.utils;

public class Utils {
    
    private Utils() {
        
    }
    
    public static String cleanFileName(String filename) {
        return filename.replace(" ", "")
                .replace(":", "")
                .replace("\\", "");
    }
    
}
