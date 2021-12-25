package com.bgsoftware.wildstacker.handlers;

import com.bgsoftware.wildstacker.WildStackerPlugin;
import com.bgsoftware.wildstacker.api.enums.StackCheckResult;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import com.bgsoftware.wildstacker.hooks.ClaimsProvider;
import com.bgsoftware.wildstacker.hooks.ConflictPluginFixer;
import com.bgsoftware.wildstacker.hooks.EconomyHook;
import com.bgsoftware.wildstacker.hooks.EntityNameProvider;
import com.bgsoftware.wildstacker.hooks.EntitySimilarityProvider;
import com.bgsoftware.wildstacker.hooks.EntityTypeProvider;
import com.bgsoftware.wildstacker.hooks.IDataSerializer;
import com.bgsoftware.wildstacker.hooks.ItemEnchantProvider;
import com.bgsoftware.wildstacker.hooks.PluginHooks;
import com.bgsoftware.wildstacker.hooks.RegionsProvider;
import com.bgsoftware.wildstacker.hooks.SlimefunHook;
import com.bgsoftware.wildstacker.hooks.SpawnersProvider;
import com.bgsoftware.wildstacker.hooks.SpawnersProvider_Default;
import com.bgsoftware.wildstacker.hooks.listeners.IEntityCombatListener;
import com.bgsoftware.wildstacker.hooks.listeners.IEntityDeathListener;
import com.bgsoftware.wildstacker.hooks.listeners.IEntityDuplicateListener;
import com.bgsoftware.wildstacker.hooks.listeners.INameChangeListener;
import com.bgsoftware.wildstacker.hooks.listeners.IStackedBlockListener;
import com.bgsoftware.wildstacker.hooks.listeners.IStackedItemListener;
import com.bgsoftware.wildstacker.listeners.PaperListener;
import com.bgsoftware.wildstacker.listeners.ProvidersListener;
import com.bgsoftware.wildstacker.listeners.plugins.BossListener;
import com.bgsoftware.wildstacker.listeners.plugins.ClearLaggListener;
import com.bgsoftware.wildstacker.listeners.plugins.CustomBossesListener;
import com.bgsoftware.wildstacker.listeners.plugins.EchoPetListener;
import com.bgsoftware.wildstacker.listeners.plugins.EliteBossesListener;
import com.bgsoftware.wildstacker.listeners.plugins.EpicBossesListener;
import com.bgsoftware.wildstacker.listeners.plugins.EpicSpawnersListener;
import com.bgsoftware.wildstacker.listeners.plugins.JetsMinionsListener;
import com.bgsoftware.wildstacker.listeners.plugins.MiniaturePetsListener;
import com.bgsoftware.wildstacker.listeners.plugins.MoreBossesListener;
import com.bgsoftware.wildstacker.listeners.plugins.MyPetListener;
import com.bgsoftware.wildstacker.listeners.plugins.PinataPartyListener;
import com.bgsoftware.wildstacker.utils.threads.Executor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class ProvidersHandler {

    private final WildStackerPlugin plugin;

    private SpawnersProvider spawnersProvider;
    private final List<ClaimsProvider> claimsProviders = new ArrayList<>();
    private final List<EntityTypeProvider> entityTypeProviders = new ArrayList<>();
    private final List<RegionsProvider> regionsProviders = new ArrayList<>();
    private final List<EntitySimilarityProvider> entitySimilarityProviders = new ArrayList<>();
    private final List<EntityNameProvider> entityNameProviders = new ArrayList<>();
    private final List<ItemEnchantProvider> itemEnchantProviders = new ArrayList<>();
    private final List<ConflictPluginFixer> conflictPluginFixers = new ArrayList<>();

    private final List<IStackedBlockListener> stackedBlocksListeners = new ArrayList<>();
    private final List<IStackedItemListener> stackedItemsListeners = new ArrayList<>();
    private final List<IEntityDeathListener> entityDeathListeners = new ArrayList<>();
    private final List<IEntityCombatListener> entityCombatListeners = new ArrayList<>();
    private final List<INameChangeListener> nameChangeListeners = new ArrayList<>();
    private final List<IEntityDuplicateListener> entityDuplicateListeners = new ArrayList<>();

    public ProvidersHandler(WildStackerPlugin plugin) {
        this.plugin = plugin;

        Executor.sync(() -> {
            WildStackerPlugin.log("Loading providers started...");
            long startTime = System.currentTimeMillis();

            loadSpawnersProvider();
            loadClaimsProvider();
            loadEntityTypeProviders();
            loadRegionsProviders();
            loadEntitySimilarityProviders();
            loadEntityNameProviders();
            loadDataSerializers();
            loadConflictPluginFixers();
            loadPluginHooks(plugin, null, true);

            Bukkit.getPluginManager().registerEvents(new ProvidersListener(plugin), plugin);

            this.conflictPluginFixers.forEach(ConflictPluginFixer::fixConflict);

            if (hasPaperEntityRemoveSupport())
                Bukkit.getPluginManager().registerEvents(new PaperListener(), plugin);

            WildStackerPlugin.log("Loading providers done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
        }, 0L);

        if (plugin.getSettings().superiorSkyblockHook)
            registerHook("SuperiorSkyblockHook");

        Executor.sync(() -> {
            if (Bukkit.getPluginManager().isPluginEnabled("ASkyBlock") &&
                    Bukkit.getPluginManager().getPlugin("ASkyBlock").getDescription().getAuthors().stream().noneMatch(a -> a.contains("Ome_R"))) {
                WildStackerPlugin.log("&c#################################################################");
                WildStackerPlugin.log("&c##                                                             ##");
                WildStackerPlugin.log("&c## Seems like you're using ASkyBlock, but not the custom fork. ##");
                WildStackerPlugin.log("&c##            <The custom fork supports WildStacker>           ##");
                WildStackerPlugin.log("&c##           https://github.com/OmerBenGera/askyblock          ##");
                WildStackerPlugin.log("&c##                                                             ##");
                WildStackerPlugin.log("&c#################################################################");
            }
        }, 10L);

    }

    private static boolean hasPaperEntityRemoveSupport() {
        try {
            Class.forName("com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent");
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    private static boolean doesClassExist(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    private void loadSpawnersProvider() {
        Optional<SpawnersProvider> spawnersProvider;

        if (Bukkit.getPluginManager().isPluginEnabled("SilkSpawners") && Bukkit.getPluginManager()
                .getPlugin("SilkSpawners").getDescription().getAuthors().contains("xGhOsTkiLLeRx")) {
            spawnersProvider = createInstance("SpawnersProvider_SilkSpawners");
        } else if (Bukkit.getPluginManager().isPluginEnabled("MineableSpawners")) {
            spawnersProvider = createInstance("SpawnersProvider_MineableSpawners");
        } else {
            spawnersProvider = Optional.of(new SpawnersProvider_Default());
        }

        spawnersProvider.ifPresent(this::registerSpawnersProvider);
    }

    private void registerSpawnersProvider(SpawnersProvider spawnersProvider) {
        this.spawnersProvider = spawnersProvider;
    }

    private void loadClaimsProvider() {
        claimsProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
            if (Bukkit.getPluginManager().getPlugin("Factions").getDescription().getAuthors().contains("drtshock")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_FactionsUUID");
                claimsProvider.ifPresent(claimsProviders::add);
            } else {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_MassiveFactions");
                claimsProvider.ifPresent(claimsProviders::add);
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlotSquared")) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("PlotSquared");
            if (plugin.getDescription().getVersion().startsWith("6.")) {
                try {
                    Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared6");
                    claimsProvider.ifPresent(claimsProviders::add);
                } catch (Exception ex) {
                    WildStackerPlugin.log("&cYour version of PlotSquared is not supported. Please contact Ome_R for support.");
                }
            } else if (plugin.getDescription().getVersion().startsWith("5.")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared5");
                claimsProvider.ifPresent(claimsProviders::add);
            } else if (plugin.getDescription().getMain().contains("com.github")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquared4");
                claimsProvider.ifPresent(claimsProviders::add);
            } else {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_PlotSquaredLegacy");
                claimsProvider.ifPresent(claimsProviders::add);
            }
        }
        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
            if (plugin.getDescription().getVersion().startsWith("6")) {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_WorldGuard6");
                claimsProvider.ifPresent(claimsProviders::add);
            } else {
                Optional<ClaimsProvider> claimsProvider = createInstance("ClaimsProvider_WorldGuard7");
                claimsProvider.ifPresent(claimsProviders::add);
            }
        }
    }

    private void loadEntityTypeProviders() {
        entityTypeProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            Optional<EntityTypeProvider> entityTypeProvider = createInstance("EntityTypeProvider_Citizens");
            entityTypeProvider.ifPresent(entityTypeProviders::add);
        }
    }

    private void loadRegionsProviders() {
        regionsProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
            if (plugin.getDescription().getVersion().startsWith("6")) {
                Optional<RegionsProvider> regionsProvider = createInstance("RegionsProvider_WorldGuard6");
                regionsProvider.ifPresent(regionsProviders::add);
            } else {
                Optional<RegionsProvider> regionsProvider = createInstance("RegionsProvider_WorldGuard7");
                regionsProvider.ifPresent(regionsProviders::add);
            }
        }
    }

    private void loadEntitySimilarityProviders() {
        entitySimilarityProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("LevelledMobs")) {
            Optional<EntitySimilarityProvider> entitySimilarityProvider = createInstance("EntitySimilarityProvider_LevelledMobs");
            entitySimilarityProvider.ifPresent(entitySimilarityProviders::add);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            Optional<EntitySimilarityProvider> entitySimilarityProvider = createInstance("EntitySimilarityProvider_MythicMobs");
            entitySimilarityProvider.ifPresent(entitySimilarityProviders::add);
        }
    }

    private void loadEntityNameProviders() {
        entityNameProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("LevelledMobs")) {
            Optional<EntityNameProvider> entityNameProvider = createInstance("EntityNameProvider_LevelledMobs");
            entityNameProvider.ifPresent(entityNameProviders::add);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            Optional<EntityNameProvider> entityNameProvider = createInstance("EntityNameProvider_MythicMobs");
            entityNameProvider.ifPresent(entityNameProviders::add);
        }
    }

    private void loadItemEnchantProviders() {
        itemEnchantProviders.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("WildTools")) {
            Optional<ItemEnchantProvider> itemEnchantProvider = createInstance("ItemEnchantProvider_WildTools");
            itemEnchantProvider.ifPresent(itemEnchantProviders::add);
        }
    }

    private void loadDataSerializers() {
        if (Bukkit.getPluginManager().isPluginEnabled("NBTInjector")) {
            Optional<IDataSerializer> dataSerializer = createInstance("DataSerializer_NBTInjector");
        }
    }

    private void loadConflictPluginFixers() {
        conflictPluginFixers.clear();

        if (Bukkit.getPluginManager().isPluginEnabled("FastAsyncWorldEdit")) {
            Optional<ConflictPluginFixer> conflictPluginFixer = createInstance("ConflictPluginFixer_FastAsyncWorldEdit");
            conflictPluginFixer.ifPresent(conflictPluginFixers::add);
        }
    }

    public void loadPluginHooks(WildStackerPlugin plugin, Plugin toCheck, boolean enable) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        // Load listeners
        if (enable && isPlugin(toCheck, "Citizens") && pluginManager.isPluginEnabled("Citizens"))
            PluginHooks.isCitizensEnabled = true;
        if (enable && isPlugin(toCheck, "ClearLag") && pluginManager.isPluginEnabled("ClearLag"))
            pluginManager.registerEvents(new ClearLaggListener(plugin), plugin);
        if (enable && isPlugin(toCheck, "SilkSpawners") && pluginManager.isPluginEnabled("SilkSpawners"))
            registerHook("SilkSpawnersHook");
        if (enable && isPlugin(toCheck, "CustomBosses") && pluginManager.isPluginEnabled("CustomBosses"))
            pluginManager.registerEvents(new CustomBossesListener(), plugin);
        if (enable && isPlugin(toCheck, "EpicBosses") && pluginManager.isPluginEnabled("EpicBosses"))
            pluginManager.registerEvents(new EpicBossesListener(), plugin);
        if (enable && isPlugin(toCheck, "MythicMobs") && pluginManager.isPluginEnabled("MythicMobs"))
            registerHook("MythicMobs");
        if (enable && isPlugin(toCheck, "MyPet") && pluginManager.isPluginEnabled("MyPet"))
            pluginManager.registerEvents(new MyPetListener(), plugin);
        if (enable && isPlugin(toCheck, "EchoPet") && pluginManager.isPluginEnabled("EchoPet"))
            pluginManager.registerEvents(new EchoPetListener(), plugin);
        if (enable && isPlugin(toCheck, "EpicSpawners") && doesClassExist("com.songoda.epicspawners.api.events.SpawnerSpawnEvent"))
            EpicSpawnersListener.register(plugin);
        if (enable && isPlugin(toCheck, "CrazyEnchantments") && pluginManager.isPluginEnabled("CrazyEnchantments"))
            registerHook("CrazyEnchantmentsHook");
        if (enable && isPlugin(toCheck, "Boss") && pluginManager.isPluginEnabled("Boss"))
            BossListener.register(plugin);
        if (enable && isPlugin(toCheck, "EliteBosses") && pluginManager.isPluginEnabled("EliteBosses"))
            pluginManager.registerEvents(new EliteBossesListener(), plugin);
        if (enable && isPlugin(toCheck, "JetsMinions") && pluginManager.isPluginEnabled("JetsMinions"))
            pluginManager.registerEvents(new JetsMinionsListener(), plugin);
        if (enable && isPlugin(toCheck, "Morebosses") && pluginManager.isPluginEnabled("Morebosses"))
            pluginManager.registerEvents(new MoreBossesListener(), plugin);
        if (enable && isPlugin(toCheck, "PinataParty") && pluginManager.isPluginEnabled("PinataParty"))
            pluginManager.registerEvents(new PinataPartyListener(), plugin);
        if (enable && isPlugin(toCheck, "MiniaturePets") && pluginManager.isPluginEnabled("MiniaturePets"))
            pluginManager.registerEvents(new MiniaturePetsListener(), plugin);

        //Load plugin hooks
        if (isPlugin(toCheck, "mcMMO") && pluginManager.isPluginEnabled("mcMMO")) {
            Plugin mcmmo = pluginManager.getPlugin("mcMMO");
            if (mcmmo.getDescription().getVersion().startsWith("1")) {
                registerHook("McMMOHook");
            } else {
                registerHook("McMMO2Hook");
            }
        }
        if (isPlugin(toCheck, "CoreProtect") && pluginManager.isPluginEnabled("CoreProtect"))
            registerHook("CoreProtectHook");
        if (isPlugin(toCheck, "WorldGuard") && pluginManager.isPluginEnabled("WorldGuard"))
            PluginHooks.isWorldGuardEnabled = enable;
        if (isPlugin(toCheck, "WildTools") && pluginManager.isPluginEnabled("WildTools"))
            PluginHooks.isWildToolsEnabled = enable;
        if (isPlugin(toCheck, "ProtocolLib") && pluginManager.isPluginEnabled("ProtocolLib"))
            registerHook("ProtocolLibHook");
        if (isPlugin(toCheck, "Vault") && pluginManager.isPluginEnabled("Vault"))
            EconomyHook.setEnabled(enable);
        if (isPlugin(toCheck, "MergedSpawner") && pluginManager.isPluginEnabled("MergedSpawner"))
            PluginHooks.isMergedSpawnersEnabled = enable;
        if (enable && isPlugin(toCheck, "FactionsTop") && doesClassExist("net.novucs.ftop.FactionsTopPlugin"))
            registerHook("NovucsHook");
        if (enable && isPlugin(toCheck, "ShopGUIPlus") && doesClassExist("net.brcdev.shopgui.ShopGuiPlugin")) {
            Plugin shopGUIPlus = pluginManager.getPlugin("ShopGUIPlus");
            if (shopGUIPlus.getDescription().getVersion().startsWith("1.18")) {
                registerHook("ShopGUIPlus18Hook");
            } else {
                registerHook("ShopGUIPlus20Hook");
            }
        }
        if (isPlugin(toCheck, "Jobs") && pluginManager.isPluginEnabled("Jobs"))
            registerHook("JobsHook");
        if (enable && isPlugin(toCheck, "FabledSkyBlock") && pluginManager.isPluginEnabled("FabledSkyBlock"))
            registerHook("FabledSkyblockHook");
        if (enable && isPlugin(toCheck, "SuperiorSkyblock2") && pluginManager.isPluginEnabled("SuperiorSkyblock2"))
            registerHook("SuperiorSkyblockHook");
        if (isPlugin(toCheck, "Slimefun") && pluginManager.isPluginEnabled("Slimefun"))
            SlimefunHook.setEnabled(enable);
    }

    public SpawnersProvider getSpawnersProvider() {
        return spawnersProvider;
    }

    public boolean hasClaimAccess(Player player, Location location) {
        for (ClaimsProvider claimsProvider : claimsProviders) {
            if (!claimsProvider.hasClaimAccess(player, location))
                return false;
        }

        return true;
    }

    @Nullable
    public String checkStackEntity(Entity entity) {
        for (EntityTypeProvider entityTypeProvider : entityTypeProviders) {
            String failureReason = entityTypeProvider.checkStackEntity(entity);
            if (failureReason != null)
                return failureReason;
        }

        return null;
    }

    public List<String> getRegionNames(Location location) {
        List<String> regions = new ArrayList<>();
        regionsProviders.forEach(regionsProvider -> regions.addAll(regionsProvider.getRegionNames(location)));
        return regions;
    }

    @Nullable
    public String getCustomName(LivingEntity livingEntity) {
        for (EntityNameProvider entityNameProvider : entityNameProviders) {
            String customName = entityNameProvider.getCustomName(livingEntity);
            if (customName != null)
                return customName;
        }

        return null;
    }

    public StackCheckResult areSimilar(Entity entity, Entity other) {
        for (EntitySimilarityProvider entitySimilarityProvider : entitySimilarityProviders) {
            StackCheckResult stackCheckResult = entitySimilarityProvider.areSimilar(entity, other);
            if (stackCheckResult != StackCheckResult.SUCCESS)
                return stackCheckResult;
        }

        return StackCheckResult.SUCCESS;
    }

    public boolean hasEnchantmentLevel(ItemStack itemStack, Enchantment enchantment, int requiredLevel) {
        for (ItemEnchantProvider itemEnchantProvider : itemEnchantProviders) {
            if (itemEnchantProvider.hasEnchantmentLevel(itemStack, enchantment, requiredLevel))
                return true;
        }

        return false;
    }

    public void registerStackedBlockListener(IStackedBlockListener stackedBlockListener) {
        this.stackedBlocksListeners.add(stackedBlockListener);
    }

    public void notifyStackedBlockListeners(OfflinePlayer offlinePlayer, Block block,
                                            IStackedBlockListener.Action action) {
        // noinspection deprecation
        notifyStackedBlockListeners(offlinePlayer, block.getLocation(), block.getType(), block.getData(), action);
    }

    public void notifyStackedBlockListeners(OfflinePlayer offlinePlayer, Location location, Material type, byte data,
                                            IStackedBlockListener.Action action) {
        this.stackedBlocksListeners.forEach(stackedBlockListener -> stackedBlockListener
                .recordBlockChange(offlinePlayer, location, type, data, action));
    }

    public void registerStackedItemListener(IStackedItemListener stackedItemListener) {
        this.stackedItemsListeners.add(stackedItemListener);
    }

    public void notifyStackedItemListeners(OfflinePlayer offlinePlayer, Item item, int amount) {
        this.stackedItemsListeners.forEach(stackedItemListener -> stackedItemListener
                .recordItemPickup(offlinePlayer, item, amount));
    }

    public void registerEntityDeathListener(IEntityDeathListener entityDeathListener) {
        this.entityDeathListeners.add(entityDeathListener);
    }

    public void notifyEntityDeathListeners(StackedEntity stackedEntity, IEntityDeathListener.Type type) {
        this.entityDeathListeners.forEach(entityDeathListener -> entityDeathListener.handleDeath(stackedEntity, type));
    }

    public void registerEntityCombatListener(IEntityCombatListener entityCombatListener) {
        this.entityCombatListeners.add(entityCombatListener);
    }

    public void notifyEntityCombatListeners(LivingEntity livingEntity, Player killer,
                                            Entity entityDamager, double finalDamage) {
        this.entityCombatListeners.forEach(entityCombatListener -> entityCombatListener.handleCombat(livingEntity,
                killer, entityDamager, finalDamage));
    }

    public void registerNameChangeListener(INameChangeListener nameChangeListener) {
        this.nameChangeListeners.add(nameChangeListener);
    }

    public void notifyNameChangeListeners(Entity entity) {
        this.nameChangeListeners.forEach(nameChangeListener -> nameChangeListener.notifyNameChange(entity));
    }

    public void registerEntityDuplicateListener(IEntityDuplicateListener entityDuplicateListener) {
        this.entityDuplicateListeners.add(entityDuplicateListener);
    }

    @Nullable
    public <T extends LivingEntity> T tryDuplicateEntity(T entity) {
        for (IEntityDuplicateListener entityDuplicateListener : entityDuplicateListeners) {
            T duplicated = entityDuplicateListener.duplicateEntity(entity);
            if (duplicated != null)
                return duplicated;
        }

        return null;
    }

    private static boolean isPlugin(Plugin plugin, String pluginName) {
        return plugin == null || plugin.getName().equals(pluginName);
    }

    private void registerHook(String className) {
        try {
            Class<?> clazz = Class.forName("com.bgsoftware.wildstacker.hooks." + className);
            Method registerMethod = clazz.getMethod("register", WildStackerPlugin.class);
            registerMethod.invoke(null, plugin);
        } catch (Exception ignored) {
        }
    }

    private <T> Optional<T> createInstance(String className) {
        try {
            Class<?> clazz = Class.forName("com.bgsoftware.wildstacker.hooks." + className);
            try {
                Method compatibleMethod = clazz.getDeclaredMethod("isCompatible");
                if (!(boolean) compatibleMethod.invoke(null))
                    return Optional.empty();
            } catch (Exception ignored) {
            }

            try {
                Constructor<?> constructor = clazz.getConstructor(WildStackerPlugin.class);
                // noinspection unchecked
                return Optional.of((T) constructor.newInstance(plugin));
            } catch (Exception error) {
                // noinspection unchecked
                return Optional.of((T) clazz.newInstance());
            }
        } catch (ClassNotFoundException ignored) {
            return Optional.empty();
        } catch (Exception error) {
            error.printStackTrace();
            return Optional.empty();
        }
    }

}
