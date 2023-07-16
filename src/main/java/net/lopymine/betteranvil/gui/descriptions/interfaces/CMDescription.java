package net.lopymine.betteranvil.gui.descriptions.interfaces;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedHashSet;

public interface CMDescription {
    LinkedHashSet<CMDItem> getSearchData(WTextField wTextField, LinkedHashSet<CMDItem> data);
    LinkedHashSet<CMDItem> getSearchItemData(WTextField wTextField, LinkedHashSet<CMDItem> data);
    LinkedHashSet<CMDItem> getSearchPackData(WTextField wTextField, LinkedHashSet<CMDItem> data);
    LinkedHashSet<CMDItem> getPackItems(String pack);
    LinkedHashSet<CMDItem> getServerPackItems();
    int getWPos(int width);
    void clearButtons();
    void createFavoriteNameList(WPlainPanel root, LinkedHashSet<CMDItem> data);
    void createAllNameList(WPlainPanel root, LinkedHashSet<CMDItem> data);
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

    default String getId(String item) {
        return item.replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
    }
    default Text getTextByID(int id) {
        return Text.of("ID: " + id);
    }
}
