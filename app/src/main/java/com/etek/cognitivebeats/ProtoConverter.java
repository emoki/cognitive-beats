package com.etek.cognitivebeats;

import com.etek.cognitivebeats.BeatConfiguration;
import com.etek.cognitivebeats.CognitiveBeatsProto;

import java.util.ArrayList;

public class ProtoConverter {

    static public ArrayList<BeatConfiguration> toBeatConfigurationList(CognitiveBeatsProto.BeatConfigurationList proto) {
        ArrayList<BeatConfiguration> beats = new ArrayList<BeatConfiguration>();
        for(CognitiveBeatsProto.BeatConfiguration b : proto.getConfigsList()) {
            beats.add(toBeatConfiguration(b));
        }
        return beats;
    }

    static public CognitiveBeatsProto.BeatConfigurationList toBeatConfigurationListProto(ArrayList<BeatConfiguration> beats) {
        CognitiveBeatsProto.BeatConfigurationList.Builder builder = CognitiveBeatsProto.BeatConfigurationList.newBuilder();
        for(BeatConfiguration b : beats) {
            builder.addConfigs(toBeatConfigurationProto(b));
        }
        return builder.build();
    }

    static public BeatConfiguration toBeatConfiguration(CognitiveBeatsProto.BeatConfiguration proto) {
        ArrayList<String> effects = new ArrayList<String>();
        for (String i : proto.getEffectsList()) {
            effects.add(i);
        }
        ArrayList<String> refs = new ArrayList<String>();
        for (String i : proto.getReferencesList()) {
            refs.add(i);
        }
        ArrayList<BeatConfiguration.BeatParameters> beats = new ArrayList<BeatConfiguration.BeatParameters>();
        for (CognitiveBeatsProto.BeatConfiguration.BeatParameters i : proto.getBeatsList()) {
            beats.add(new BeatConfiguration.BeatParameters(i.getFreq0(), i.getFreq1(), i.getDuration()));
        }
        return new BeatConfiguration(proto.getTitle(), proto.getDescription(),
                effects, refs, beats);
    }

    static public CognitiveBeatsProto.BeatConfiguration toBeatConfigurationProto(BeatConfiguration b) {
        CognitiveBeatsProto.BeatConfiguration.Builder builder = CognitiveBeatsProto.BeatConfiguration.newBuilder()
                .setTitle(b.mTitle)
                .setDescription(b.mDescription)
                .addAllEffects(b.mEffects)
                .addAllReferences(b.mReferences);
        for (BeatConfiguration.BeatParameters p : b.mBeats) {
            builder.addBeats(CognitiveBeatsProto.BeatConfiguration.BeatParameters.newBuilder()
                    .setFreq0(p.mFreq0)
                    .setFreq1(p.mFreq1)
                    .setDuration(p.mDuration));
        }
        return builder.build();
    }
}
