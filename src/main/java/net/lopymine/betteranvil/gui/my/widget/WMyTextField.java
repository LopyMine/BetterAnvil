package net.lopymine.betteranvil.gui.my.widget;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class WMyTextField extends WTextField {

    private int i = 0;
    private ArrayList<String> resourcePacks;

    public WMyTextField(ArrayList<String> resourcePacks, Text suggestion){
        this.resourcePacks = resourcePacks;
        setSuggestion(suggestion);
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        int a = (int) amount;
        if(getText().startsWith("*")){
            if(a == 1){
                setText("*" + resourcePacks.get(i));
                i++;
                if(i > resourcePacks.size()-1){
                    i = 0;
                }
            }
            if(a == -1){
                setText("*" + resourcePacks.get(i));
                i--;
                if(i < 0){
                    i = resourcePacks.size()-1;
                }
            }
        }
        return super.onMouseScroll(x, y, amount);
    }
}