package com.bgsoftware.wildstacker.nms.v1_18_R1.mappings.net.minecraft.world.entity;

import com.bgsoftware.common.remaps.Remap;
import com.bgsoftware.wildstacker.nms.v1_18_R1.mappings.MappedObject;
import com.bgsoftware.wildstacker.nms.v1_18_R1.mappings.net.minecraft.world.entity.ai.BehaviorController;
import com.bgsoftware.wildstacker.nms.v1_18_R1.mappings.net.minecraft.world.level.World;
import com.bgsoftware.wildstacker.nms.v1_18_R1.mappings.net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.ai.gossip.Reputation;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntityStrider;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.monster.EntityZombieVillager;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantRecipeList;
import net.minecraft.world.level.WorldAccess;
import net.minecraft.world.level.gameevent.GameEvent;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import javax.annotation.Nullable;
import java.util.List;

public class Entity extends MappedObject<net.minecraft.world.entity.Entity> {

    public static Entity ofNullable(net.minecraft.world.entity.Entity handle) {
        return handle == null ? null : new Entity(handle);
    }

    public Entity(net.minecraft.world.entity.Entity handle) {
        super(handle);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getLevel",
            type = Remap.Type.METHOD,
            remappedName = "W")
    public World getWorld() {
        return new World(handle.W());
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "level",
            type = Remap.Type.FIELD,
            remappedName = "t")
    public void setWorld(net.minecraft.world.level.World world) {
        handle.t = world;
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "setCustomName",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setCustomName(IChatBaseComponent chatBaseComponent) {
        handle.a(chatBaseComponent);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getCustomName",
            type = Remap.Type.METHOD,
            remappedName = "Z")
    public IChatBaseComponent getCustomName() {
        return handle.Z();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "hasCustomName",
            type = Remap.Type.METHOD,
            remappedName = "Y")
    public boolean hasCustomName() {
        return handle.Y();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "isCustomNameVisible",
            type = Remap.Type.METHOD,
            remappedName = "cr")
    public boolean getCustomNameVisible() {
        return handle.cr();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "setCustomNameVisible",
            type = Remap.Type.METHOD,
            remappedName = "n")
    public void setCustomNameVisible(boolean isCustomNameVisible) {
        handle.n(isCustomNameVisible);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getX",
            type = Remap.Type.METHOD,
            remappedName = "dc")
    public double getX() {
        return handle.dc();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getY",
            type = Remap.Type.METHOD,
            remappedName = "de")
    public double getY() {
        return handle.de();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getZ",
            type = Remap.Type.METHOD,
            remappedName = "di")
    public double getZ() {
        return handle.di();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getType",
            type = Remap.Type.METHOD,
            remappedName = "ad")
    public EntityTypes<?> getType() {
        return handle.ad();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getBoundingBox",
            type = Remap.Type.METHOD,
            remappedName = "cw")
    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(handle.cw());
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getEntityData",
            type = Remap.Type.METHOD,
            remappedName = "ai")
    public DataWatcher getEntityData() {
        return handle.ai();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "playSound",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void playSound(SoundEffect soundEffect, float volume, float pitch) {
        handle.a(soundEffect, volume, pitch);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "setPosRaw",
            type = Remap.Type.METHOD,
            remappedName = "o")
    public void setPosRaw(double x, double y, double z) {
        handle.o(x, y, z);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "discard",
            type = Remap.Type.METHOD,
            remappedName = "ah")
    public void discard() {
        handle.ah();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "moveTo",
            type = Remap.Type.METHOD,
            remappedName = "b")
    public void moveTo(double x, double y, double z, float yaw, float pitch) {
        handle.b(x, y, z, yaw, pitch);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "blockPosition",
            type = Remap.Type.METHOD,
            remappedName = "cW")
    public BlockPosition blockPosition() {
        return handle.cW();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getVehicle",
            type = Remap.Type.METHOD,
            remappedName = "cN")
    public Entity getVehicle() {
        return ofNullable(handle.cN());
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getIndirectPassengers",
            type = Remap.Type.METHOD,
            remappedName = "cJ")
    public Iterable<net.minecraft.world.entity.Entity> getIndirectPassengers() {
        return handle.cJ();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "getPassengers",
            type = Remap.Type.METHOD,
            remappedName = "cF")
    public List<net.minecraft.world.entity.Entity> getPassengers() {
        return handle.cF();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "copyPosition",
            type = Remap.Type.METHOD,
            remappedName = "s")
    public void copyPosition(net.minecraft.world.entity.Entity entity) {
        handle.s(entity);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "gameEvent",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void gameEvent(GameEvent gameEvent) {
        handle.a(gameEvent);
    }

    public CraftEntity getBukkitEntity() {
        return handle.getBukkitEntity();
    }

    @Remap(classPath = "net.minecraft.world.entity.AgeableMob",
            name = "isBaby",
            type = Remap.Type.METHOD,
            remappedName = "y_")
    public boolean isBaby() {
        return ((EntityAgeable) handle).y_();
    }

    @Remap(classPath = "net.minecraft.world.entity.animal.Animal",
            name = "setInLove",
            type = Remap.Type.METHOD,
            remappedName = "g")
    public void setInLove(EntityPlayer entityPlayer) {
        ((EntityAnimal) handle).g(entityPlayer);
    }

    @Remap(classPath = "net.minecraft.world.entity.animal.Animal",
            name = "resetLove",
            type = Remap.Type.METHOD,
            remappedName = "fF")
    public void resetLove() {
        ((EntityAnimal) handle).fF();
    }

    @Remap(classPath = "net.minecraft.world.entity.animal.Animal",
            name = "isInLove",
            type = Remap.Type.METHOD,
            remappedName = "fE")
    public boolean isInLove() {
        return ((EntityAnimal) handle).fE();
    }

    @Remap(classPath = "net.minecraft.world.entity.animal.Animal",
            name = "isFood",
            type = Remap.Type.METHOD,
            remappedName = "n")
    public boolean isFood(ItemStack itemStack) {
        return ((EntityAnimal) handle).n(itemStack);
    }

    @Remap(classPath = "net.minecraft.world.entity.decoration.ArmorStand",
            name = "isMarker",
            type = Remap.Type.METHOD,
            remappedName = "t")
    public boolean isMarker() {
        return ((EntityArmorStand) handle).t();
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "isNoAi",
            type = Remap.Type.METHOD,
            remappedName = "fr")
    public boolean isNoAI() {
        return ((EntityInsentient) handle).fr();
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "setNoAi",
            type = Remap.Type.METHOD,
            remappedName = "s")
    public void setNoAI(boolean isNoAI) {
        ((EntityInsentient) handle).s(isNoAI);
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "spawnAnim",
            type = Remap.Type.METHOD,
            remappedName = "L")
    public void spawnAnim() {
        ((EntityInsentient) handle).L();
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "getNavigation",
            type = Remap.Type.METHOD,
            remappedName = "D")
    public NavigationAbstract getNavigation() {
        return ((EntityInsentient) handle).D();
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "setPersistenceRequired",
            type = Remap.Type.METHOD,
            remappedName = "fg")
    public void setPersistenceRequired() {
        ((EntityInsentient) handle).fg();
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "finalizeSpawn",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public GroupDataEntity finalizeSpawn(WorldAccess worldAccess, DifficultyDamageScaler difficulty,
                                         EnumMobSpawn spawnReason, @Nullable GroupDataEntity entityData,
                                         @Nullable NBTTagCompound entityNbt) {
        return ((EntityInsentient) handle).a(worldAccess, difficulty, spawnReason, entityData, entityNbt);
    }

    @Remap(classPath = "net.minecraft.world.entity.Mob",
            name = "armorDropChances",
            type = Remap.Type.FIELD,
            remappedName = "bU")
    public float[] getArmorDropChances() {
        return ((EntityInsentient) handle).bU;
    }

    @Remap(classPath = "net.minecraft.world.entity.item.ItemEntity",
            name = "getItem",
            type = Remap.Type.METHOD,
            remappedName = "h")
    public ItemStack getItem() {
        return ((EntityItem) handle).h();
    }

    @Remap(classPath = "net.minecraft.world.entity.item.ItemEntity",
            name = "pickupDelay",
            type = Remap.Type.FIELD,
            remappedName = "aq")
    public void setPickupDelay(int delay) {
        ((EntityItem) handle).aq = delay;
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "setHealth",
            type = Remap.Type.METHOD,
            remappedName = "c")
    public void setHealth(float health) {
        ((EntityLiving) handle).c(health);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "onItemPickup",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void onItemPickup(EntityItem entityItem) {
        ((EntityLiving) handle).a(entityItem);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "hurt",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void hurt(DamageSource damageSource, float damage) {
        handle.a(damageSource, damage);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "take",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void take(net.minecraft.world.entity.Entity entity, int amount) {
        ((EntityLiving) handle).a(entity, amount);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "getOffhandItem",
            type = Remap.Type.METHOD,
            remappedName = "es")
    public ItemStack getOffhandItem() {
        return ((EntityLiving) handle).es();
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "setItemSlot",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setItemSlot(EnumItemSlot itemSlot, ItemStack itemStack) {
        handle.a(itemSlot, itemStack);
    }

    @Remap(classPath = "net.minecraft.world.entity.Entity",
            name = "spawnAtLocation",
            type = Remap.Type.METHOD,
            remappedName = "b")
    public void spawnAtLocation(ItemStack itemStack) {
        handle.b(itemStack);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "broadcastBreakEvent",
            type = Remap.Type.METHOD,
            remappedName = "d")
    public void broadcastBreakEvent(EnumItemSlot itemSlot) {
//        ((EntityLiving) handle).d(itemSlot);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "getItemBySlot",
            type = Remap.Type.METHOD,
            remappedName = "b")
    public ItemStack getItemBySlot(EnumItemSlot enumItemSlot) {
        return ((EntityLiving) handle).b(enumItemSlot);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "addAdditionalSaveData",
            type = Remap.Type.METHOD,
            remappedName = "b")
    public void addAdditionalSaveData(NBTTagCompound nbtTagCompound) {
        ((EntityLiving) handle).b(nbtTagCompound);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "getMaxHealth",
            type = Remap.Type.METHOD,
            remappedName = "el")
    public float getMaxHealth() {
        return ((EntityLiving) handle).el();
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "readAdditionalSaveData",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void readAdditionalSaveData(NBTTagCompound nbtTagCompound) {
        ((EntityLiving) handle).a(nbtTagCompound);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "getItemInHand",
            type = Remap.Type.METHOD,
            remappedName = "b")
    public ItemStack getItemInHand(EnumHand enumHand) {
        return ((EntityLiving) handle).b(enumHand);
    }

    public void removeAllEffects(EntityPotionEffectEvent.Cause cause) {
        ((EntityLiving) handle).removeAllEffects(cause);
    }

    public void addEffect(MobEffect mobEffect, EntityPotionEffectEvent.Cause cause) {
        ((EntityLiving) handle).addEffect(mobEffect, cause);
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.piglin.Piglin",
            name = "getBrain",
            type = Remap.Type.METHOD,
            remappedName = "dt")
    public BehaviorController<EntityPiglin> getBrain() {
        return new BehaviorController<>(((EntityPiglin) handle).dt());
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.Strider",
            name = "isSaddled",
            type = Remap.Type.METHOD,
            remappedName = "d")
    public boolean isSaddled() {
        return ((EntityStrider) handle).d();
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.Strider",
            name = "steering",
            type = Remap.Type.FIELD,
            remappedName = "cf")
    public SaddleStorage getSaddleStorage() {
        return new SaddleStorage(((EntityStrider) handle).cf);
    }

    @Remap(classPath = "net.minecraft.world.entity.npc.Villager",
            name = "getVillagerData",
            type = Remap.Type.METHOD,
            remappedName = "fJ")
    public VillagerData getVillagerData() {
        return ((EntityVillager) handle).fJ();
    }

    @Remap(classPath = "net.minecraft.world.entity.npc.Villager",
            name = "getGossips",
            type = Remap.Type.METHOD,
            remappedName = "fV")
    public Reputation getGossips() {
        return ((EntityVillager) handle).fV();
    }

    @Remap(classPath = "net.minecraft.world.entity.npc.AbstractVillager",
            name = "getOffers",
            type = Remap.Type.METHOD,
            remappedName = "fA")
    public MerchantRecipeList getOffers() {
        return ((EntityVillagerAbstract) handle).fA();
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.Zombie",
            name = "setBaby",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setBaby(boolean isBaby) {
        ((EntityZombie) handle).a(isBaby);
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.ZombieVillager",
            name = "setVillagerData",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setVillagerData(VillagerData villagerData) {
        ((EntityZombieVillager) handle).a(villagerData);
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.ZombieVillager",
            name = "setTradeOffers",
            type = Remap.Type.METHOD,
            remappedName = "c")
    public void setTradeOffers(NBTTagCompound offerTag) {
        ((EntityZombieVillager) handle).c(offerTag);
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.ZombieVillager",
            name = "setVillagerXp",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setVillagerXp(int experience) {
        ((EntityZombieVillager) handle).a(experience);
    }

    @Remap(classPath = "net.minecraft.world.entity.monster.ZombieVillager",
            name = "setVillagerXp",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public void setGossips(NBTBase gossips) {
        ((EntityZombieVillager) handle).a(gossips);
    }

    @Remap(classPath = "net.minecraft.world.entity.ExperienceOrb",
            name = "value",
            type = Remap.Type.FIELD,
            remappedName = "ar")
    public void setExperienceAmount(int experience) {
        ((EntityExperienceOrb) handle).ar = experience;
    }

    @Remap(classPath = "net.minecraft.world.damagesource.DamageSource",
            name = "playerAttack",
            type = Remap.Type.METHOD,
            remappedName = "a")
    public static DamageSource playerAttack(EntityHuman entityHuman) {
        return DamageSource.a(entityHuman);
    }

    @Remap(classPath = "net.minecraft.world.entity.LivingEntity",
            name = "getEquipmentSlotForItem",
            type = Remap.Type.METHOD,
            remappedName = "i")
    public static EnumItemSlot getEquipmentSlotForItem(ItemStack itemStack) {
        return EntityLiving.i(itemStack);
    }

}