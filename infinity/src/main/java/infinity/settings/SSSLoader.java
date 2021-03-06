/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;

/**
 *
 * @author Asser Fahrenholz
 */
public class SSSLoader implements AssetLoader {

    @SuppressWarnings("unused")
    private static final int GROUP = 1;
    @SuppressWarnings("unused")
    private static final int KEY = 2;
    @SuppressWarnings("unused")
    private static final int VALUE = 3;
    @SuppressWarnings("unused")
    private static final int MIN = 4;
    @SuppressWarnings("unused")
    private static final int MAX = 5;
    @SuppressWarnings("unused")
    private static final int DESC = 6;

    @Override
    public ArrayList<String[]> load(final AssetInfo assetInfo) throws IOException {

        final ArrayList<String[]> result = new ArrayList<>();

        final InputStream stream = assetInfo.openStream();

        final InputStreamReader streamReader = new InputStreamReader(stream);

        try (BufferedReader br = new BufferedReader(streamReader)) {
            String line;

            while ((line = br.readLine()) != null) {
                final String[] values = line.split(":");
                /*
                 * for (String str : values) { System.out.println(str); }
                 */
                result.add(values);
            }
        }

        return result;
    }
}
