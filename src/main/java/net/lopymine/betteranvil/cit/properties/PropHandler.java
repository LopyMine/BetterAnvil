package net.lopymine.betteranvil.cit.properties;

import com.mifmif.common.regex.Generex;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackParserVersion;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class PropHandler {

    private static ArrayList<String> enchantments = new ArrayList<>();//

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
        return line.replaceAll("matchItems=", "").replaceAll("items=","").replaceAll("minecraft:", "").replaceAll("\\\\", "");
    }

    public static boolean isCustomName(String line){
        if(line.startsWith("nbt.display.Name=")){
            return true;
        } else {
            return false;
        }
    }

    public static String getCustomName(String line){
        ResourcePackParserVersion v = BetterAnvilConfigManager.read().PARSER_VERSION;
        if(v == ResourcePackParserVersion.V1){
            String l2 = line.replaceAll("iregex", "").replaceAll("ipattern", "").replaceAll("regex", "").replaceAll("pattern", "").replace("(", "").replace(")", "").replaceAll("nbt.display.Name=", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\$", "").replace("*", "");
            String l3 = StringEscapeUtils.unescapeJava(l2.replace("\\\\", "\\"));
            String upLine3 = l3.replaceAll("/", "").replaceAll("\\\\", "");
            return upLine3;
        } else {
            String name = line.replaceAll("nbt.display.Name=", "").replaceAll("iregex", "").replaceAll("ipattern", "").replaceAll("regex", "").replaceAll("pattern", "").replaceAll(":", "");//.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "").replace("*", "");
            String namew = name.replaceAll("\\(\\\\ \\.\\*\\|\\$\\)", "").replaceAll("\\(\\.\\*\\\\ \\|\\^\\)", "");
            String namee = namew.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "").replace("*", "");
            String named = StringEscapeUtils.unescapeJava(namee.strip());
            Generex generator = new Generex(StringEscapeUtils.unescapeJava(named));
            List<String> names = generator.getAllMatchedStrings();
            StringJoiner sj = new StringJoiner("|");
            for (String word : names) {
                sj.add(word);
            }

            return sj.toString();
        }

        //String name = line.replaceAll("nbt.display.Name=", "").replaceAll("iregex", "").replaceAll("ipattern", "").replaceAll("regex", "").replaceAll("pattern", "").replaceAll(":", "");//.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "").replace("*", "");
        //String namew = name.replaceAll("\\(\\\\ \\.\\*\\|\\$\\)", "").replaceAll("\\(\\.\\*\\\\ \\|\\^\\)", "");
        //String namee = namew.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "");
        //if(namee.contains("*")){
        //    namee = namee.replace("*", "");
        //    namee = namee.strip();
        //    namee = namee + " ";
        //}
        //String named = StringEscapeUtils.unescapeJava(namee);
        //Generex generator = new Generex(StringEscapeUtils.unescapeJava(named));
        //List<String> names = generator.getAllMatchedStrings();
        //StringJoiner sj = new StringJoiner("|");
        //for (String word : names) {
        //    sj.add(word);
        //}

        //return sj.toString();

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