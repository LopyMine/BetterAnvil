package net.lopymine.betteranvil.cit;

import com.mifmif.common.regex.Generex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CitItems {
    private final String items;
    private final String customname;
    private final String damage;
    private final String enchantments;
    private String resourcePack = null;

    public CitItems(String item, String customname, String other) {
        this.items = item;
        this.customname = customname;
        this.damage = other;
        this.enchantments = null;
    }

    public ArrayList<String> getCustomNames() {
        String[] words = StringEscapeUtils.unescapeJava(customname).split("\\|"); // split the string by "|" delimiter

        ArrayList<String> names = new ArrayList<>();
        for(String n : Arrays.stream(words).toList()){
            if(!n.equals(" ")){
                String nn = n.strip();
                names.add(nn);
            }
        }
        //Generex generator = new Generex(StringEscapeUtils.unescapeJava(customname));
        //List<String> names = generator.getAllMatchedStrings();
        return new ArrayList<>(names);
    }

    public String getCustomName() {
        return customname;
    }

    public String getItem() {

        return items;
    }

    public Boolean isMoreNames() {
        return getCustomNames().size() > 0;
    }
    public Boolean isMoreItems() {
        return getCustomNames().size() > 0;
    }

    public ArrayList<String> getItems() {
        String[] names = items.split(" "); // split the string by "|" delimiter
        return new ArrayList<String>(Arrays.asList(names));
    }

    public ArrayList<Integer> getDamage() {
        ArrayList<Integer> damageList = new ArrayList<>();
        if(damage == null){
            return null;
        }
        if(damage.endsWith("%")){
            String[] damages = damage.split("-");
            for(String dmg : damages){
                damageList.add(Integer.parseInt(dmg.replace("%","")));
            }
        }
        return damageList;
    }

    public void setResourcePack(String resourcePack){
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }
    //public ArrayList<String> getEnchantments(){
//
    //}
}