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

    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_HEALTH;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_MOVE_SPEED;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_ATTACK_DAMAGE;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_TOUGHNESS;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_ARMOR;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_KNOCKBACK_RESISTANCE;
    public static ForgeConfigSpec.DoubleValue EASY_MOTHER_ATTACK_KNOCKBACK;
    public static ForgeConfigSpec.IntValue EASY_QUAKE_COOLDOWN;


    public static ForgeConfigSpec.IntValue EASY_SUMMON_BABY;
    public static ForgeConfigSpec.IntValue EASY_CHARGE_COOLDOWN;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        SERVER_BUILDER.comment("Mob settings").push("easy_mother");
        EASY_SUMMON_BABY = SERVER_BUILDER.comment("How often the mother summons a baby in ticks").defineInRange("easySummon", 100, 0, 1000);
        EASY_CHARGE_COOLDOWN = SERVER_BUILDER.comment("How often the mother charges in ticks").defineInRange("easyCharge", 200, 0, 1000);
        EASY_QUAKE_COOLDOWN = SERVER_BUILDER.comment("How often the mother causes an earthquake").defineInRange("easyQuake", 400, 0, 1000);

        EASY_MOTHER_HEALTH = SERVER_BUILDER.comment("Mother health").defineInRange("easyHealth", 600.0, 1, Integer.MAX_VALUE);
        EASY_MOTHER_MOVE_SPEED = SERVER_BUILDER.comment("Mother move speed").defineInRange("easyMovement", 0.28, 0, 10);
        EASY_MOTHER_ATTACK_DAMAGE = SERVER_BUILDER.comment("Mother attack damage").defineInRange("easyDamage", 8D, 0, 1000);
        EASY_MOTHER_TOUGHNESS = SERVER_BUILDER.comment("Mother toughness").defineInRange("easyToughness", 1d, 0, 1000);
        EASY_MOTHER_ARMOR = SERVER_BUILDER.comment("Mother armor").defineInRange("easyArmor", 6d, 0, 1000);
        EASY_MOTHER_KNOCKBACK_RESISTANCE = SERVER_BUILDER.comment("Mother knockback resistance").defineInRange("easyKnockbackRes", 0.6d, 0, 1000);
        EASY_MOTHER_ATTACK_KNOCKBACK = SERVER_BUILDER.comment("Mother attack knockback").defineInRange("easyAttackKnockback", 1.0d, 0, 1000);




        SERVER_BUILDER.pop();
        SERVER_BUILDER.comment("Baby Settings").push("babies");
        LEECH_HEAL_AMOUNT = SERVER_BUILDER.comment("How much the leech baby heals the mother").defineInRange("leechHeal", 3, 0.0, 1000);
        POISON_LEVEL = SERVER_BUILDER.comment("Potion level of poison the poison fish inflicts").defineInRange("poisonLevel", 1, 0, 5);
        POISON_DURATION = SERVER_BUILDER.comment("Duration in ticks the poison lasts").defineInRange("posionDuration", 60, 0, 1000);


        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
