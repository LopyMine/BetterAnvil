package net.lopymine.betteranvil.config.resourcepacks.cit;

import net.lopymine.betteranvil.config.resourcepacks.ConfigWriter;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.CITMetaDataParser;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CITConfigWriter extends ConfigWriter<CITItem> {
    private static final CITConfigWriter INSTANCE = new CITConfigWriter();

    private CITConfigWriter() {
        super(ResourcePackType.CIT);
    }

    public static CITConfigWriter getInstance() {
        return INSTANCE;
    }

    @Override
    protected LinkedHashSet<CITItem> readFiles(Path folderPath) {
        LinkedHashSet<CITItem> items = new LinkedHashSet<>();
        List<Path> paths;

        try {
            paths = Files.walk(folderPath).filter(path -> path.toString().endsWith(".properties")).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return items;
        }

        for (Path path : paths) {
            try (InputStream inputStream = Files.newInputStream(path); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                try {
                    CITItem item = CITMetaDataParser.parseMeta(reader.lines().toList());

                    if (item != null) {
                        items.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return items;
    }
}
