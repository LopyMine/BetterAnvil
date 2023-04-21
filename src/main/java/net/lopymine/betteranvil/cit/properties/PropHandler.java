package net.lopymine.betteranvil.cit.properties;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropHandler {

    private static ArrayList<String> enchantments = new ArrayList<>();

    private static final ArrayList<String> itemsProp = new ArrayList<>();

    public static boolean isItem(String line){
        if(line.startsWith("matchItems=")){
            return true;
        }
        if (line.startsWith("items=")){
            return true;
        }
        return false;
    }
    public static String getItem(String line){
        return line.replaceAll("matchItems=", "").replaceAll("items=","").replaceAll("minecraft:", "");
    }

    public static boolean isCustomName(String line){
        if(line.startsWith("nbt.display.Name=")){
            return true;
        } else {
            return false;
        }
    }

    public static String getCustomName(String line){
        String upLine = StringEscapeUtils.unescapeJava(line.replaceAll("iregex:", "").replaceAll("ipattern:", "").replaceAll("regex:", "").replaceAll("pattern:", "").replace("(", "").replace(")", "").replaceAll("nbt.display.Name=", ""));

        if(upLine.contains("*")){
            return upLine.replace("*", "") + " ";
        }
        return upLine;
    }



    public static boolean isDamage(String line){
        if(line.strip().startsWith("damage=")){
            return true;
        } else {
            return false;
        }
    }
    public static int getDamage(String line){
        return Integer.parseInt(line.replaceAll("damage=", ""));
    }
    public static String getDamageString(String line){
        return line.replaceAll("damage=", "");
    }

    public static boolean isEnchantment(String line){
        for(String en : enchantments){
            if(en.equals(line)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //private static String getEnchantmentString(String line){
    //
    //}
//


    public static boolean isStackSize(String line){
        if(line.strip().startsWith("stackSize=")){
            return true;
        }else {
            return false;
        }
    }

    public static ArrayList<String> getClearResourcePackNames(List<String> names) {
        ArrayList<String> newNames = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).startsWith("file/")) {
                newNames.add(names.get(i).replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }



}
