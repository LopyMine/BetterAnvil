package net.lopymine.betteranvil.gui.descriptions.interfaces;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;

import java.util.LinkedHashSet;

public interface CITDescription {
    LinkedHashSet<CITItem> getSearchData(WTextField wTextField, LinkedHashSet<CITItem> data);
    LinkedHashSet<CITItem> getSearchItemData(WTextField wTextField, LinkedHashSet<CITItem> data);
    LinkedHashSet<CITItem> getSearchPackData(WTextField wTextField, LinkedHashSet<CITItem> data);
    LinkedHashSet<CITItem> getPackItems(String pack);
    LinkedHashSet<CITItem> getServerPackItems();
    int getWPos(int width);
    void clearButtons();
    void createFavoriteNameList(WPlainPanel root, LinkedHashSet<CITItem> data);
    void createAllNameList(WPlainPanel root, LinkedHashSet<CITItem> data);
    void setButtons();
    void createButtons(LinkedHashSet<WTabButton> buttonsSub);
    void nextButtons();
    void backButtons();
    default Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white) {
        return LibGui.isDarkMode() ? dark : white;
    }
    default String cutString(String text, int length) {
        if (text.length() <= length) {
            return text;
        } else {
            String cutText = text.substring(0, length);
            if (cutText.endsWith(" ")) {
                cutText = cutText.substring(0, cutText.length() - 1);
            }
            return cutText + "...";
        }
    }
    default String getPack(ResourcePackProfile profile) {
        return profile.getName().replaceAll(".zip", "").replaceAll("file/", "");
    }
    default String getPackWithZip(ResourcePackProfile profile) {
        return profile.getName().replaceAll("file/", "");
    }
}
