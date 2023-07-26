package net.lopymine.betteranvil.gui.widgets;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class WMyTextField extends WTextField {

    private int i = 0;
    private final LinkedHashSet<String> resourcePacks;

    public WMyTextField(LinkedHashSet<String> resourcePacks, Text suggestion){
        this.resourcePacks = resourcePacks;
        setSuggestion(suggestion);
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {

        if(resourcePacks == null){
            return InputResult.IGNORED;
        }
        if(resourcePacks.isEmpty()){
            return InputResult.IGNORED;
        }
        int d = -1;
        int a = (int) amount;
        if(getText().startsWith("*")){
            if(a == d){
                setText("*" + resourcePacks.stream().toList().get(i));
                i--;
                if(i < 0){
                    i = resourcePacks.size()-1;
                }
                return InputResult.PROCESSED;
            }
            if(a == 1){
                setText("*" + resourcePacks.stream().toList().get(i));
                i++;
                if(i > resourcePacks.size()-1){
                    i = 0;
                }
                return InputResult.PROCESSED;
            }
        }
        return super.onMouseScroll(x, y, amount);
    }
}
