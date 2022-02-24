package com.hollingsworth.mother_silverfish;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec.DoubleValue LEECH_HEAL_AMOUNT;

    public static ForgeConfigSpec.IntValue POISON_LEVEL;
    public static ForgeConfigSpec.IntValue POISON_DURATION;

    public static ForgeConfigSpec.IntValue EASY_SUMMON_BABY;
    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        SERVER_BUILDER.comment("Mob settings").push("easy_mother");
        EASY_SUMMON_BABY = SERVER_BUILDER.comment("How often the mother summons a baby").defineInRange("easySummon", 100, 0, 1000);
        SERVER_BUILDER.pop();
        SERVER_BUILDER.comment("Baby Settings").push("babies");
        LEECH_HEAL_AMOUNT = SERVER_BUILDER.comment("How much the leech baby heals the mother").defineInRange("leechHeal", 3, 0.0, 1000);
        POISON_LEVEL = SERVER_BUILDER.comment("Potion level of poison the poison fish inflicts").defineInRange("poisonLevel", 1, 0, 5);
        POISON_DURATION = SERVER_BUILDER.comment("Duration in ticks the poison lasts").defineInRange("posionDuration", 60, 0, 1000);
        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
