package com.etek.cognitivebeats;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2017.
 */

public class DefaultBeatConfiguration {
    static public ArrayList<BeatConfiguration> get() {
        ArrayList<BeatConfiguration> beatConfigList = new ArrayList<BeatConfiguration>();
        ArrayList<BeatConfiguration.BeatParameters> beatParams = new ArrayList<BeatConfiguration.BeatParameters>();
        beatParams.add(new BeatConfiguration.BeatParameters(200, 215, 1000 * 60 * 60 * 5));
        ArrayList<String> effects = new ArrayList<String>();
        effects.add("effect1");
        effects.add("effect2");
        ArrayList<String> refs = new ArrayList<String>();
        refs.add("ref1");
        refs.add("ref2");
        beatConfigList.add(new BeatConfiguration("Config1",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                effects, refs, beatParams));
        beatParams.clear();
        beatParams.add(new BeatConfiguration.BeatParameters(100, 110, 1000 * 60 * 60 * 5));
        beatConfigList.add(new BeatConfiguration("Config2",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                effects, refs, beatParams));
        beatParams.add(new BeatConfiguration.BeatParameters(100, 105, 1000 * 60 * 60 * 5));
        beatConfigList.add(new BeatConfiguration("Config3",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
                effects, refs, beatParams));

        return beatConfigList;
    }
}
