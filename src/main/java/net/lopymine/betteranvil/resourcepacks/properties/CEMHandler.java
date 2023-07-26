package net.lopymine.betteranvil.resourcepacks.properties;

import com.mifmif.common.regex.Generex;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackParserVersion;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.StringJoiner;

public class CEMHandler {
    public static boolean isName(String line){
        return line.startsWith("name.");
    }

    public static String getName(String line){
        ResourcePackParserVersion v = BetterAnvilConfigManager.INSTANCE.PARSER_VERSION;
        if(v == ResourcePackParserVersion.V1){
            String l2 = line.replaceAll("iregex", "").replaceAll("ipattern", "").replaceAll("regex", "").replaceAll("pattern", "").replace("(", "").replace(")", "").replaceAll("nbt.display.Name=", "").replaceAll(":", "").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\$", "").replace("*", "");
            String l3 = StringEscapeUtils.unescapeJava(l2.replace("\\\\", "\\"));
            String upLine3 = l3.replaceAll("/", "").replaceAll("\\\\", "");
            return upLine3;
        } else {
            String firstname = line.substring(line.indexOf('=') + 1);
            String name = firstname.replaceAll("iregex", "").replaceAll("ipattern", "").replaceAll("regex", "").replaceAll("pattern", "").replaceAll(":", "");//.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "").replace("*", "");
            String namew = name.replaceAll("\\(\\\\ \\.\\*\\|\\$\\)", "").replaceAll("\\(\\.\\*\\\\ \\|\\^\\)", "");
            String namee = namew.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\.", "").replaceAll("\\^", "").replaceAll("\\$", "").replace("*", "");
            String named = StringEscapeUtils.unescapeJava(namee.strip());

            char[] chars = named.toCharArray();

            //System.out.println(chars);
            if(chars.length != 0){
                if(chars[0] == '|'){
                    chars[0] = ' ';
                }
                if(chars[chars.length-1] == '|'){
                    chars[chars.length-1] = ' ';
                }
            }

            Generex generator = new Generex(StringEscapeUtils.unescapeJava(String.valueOf(chars).strip()));
            List<String> names = generator.getAllMatchedStrings();
            StringJoiner sj = new StringJoiner("|");
            for (String word : names) {
                sj.add(word);
            }

            return sj.toString();
        }
    }

}
