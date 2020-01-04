package me.itzdabbzz.cookclans;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.itzdabbzz.cookclans.listeners.SCEntityListener;
import me.itzdabbzz.cookclans.listeners.SCPlayerListener;
import me.itzdabbzz.cookclans.tasks.CollectFeeTask;
import me.itzdabbzz.cookclans.tasks.CollectUpkeepTask;
import me.itzdabbzz.cookclans.tasks.UpkeepWarningTask;
import me.itzdabbzz.cookclans.uuid.UUIDMigration;
import org.bukkit.plugin.java.JavaPlugin;

import me.itzdabbzz.cookclans.executors.AcceptCommandExecutor;
import me.itzdabbzz.cookclans.executors.AllyCommandExecutor;
import me.itzdabbzz.cookclans.executors.ClanCommandExecutor;
import me.itzdabbzz.cookclans.executors.DenyCommandExecutor;
import me.itzdabbzz.cookclans.executors.GlobalCommandExecutor;
import me.itzdabbzz.cookclans.executors.MoreCommandExecutor;
import me.itzdabbzz.cookclans.managers.ClanManager;
import me.itzdabbzz.cookclans.managers.LanguageManager;
import me.itzdabbzz.cookclans.managers.PermissionsManager;
import me.itzdabbzz.cookclans.managers.RequestManager;
import me.itzdabbzz.cookclans.managers.SettingsManager;
import me.itzdabbzz.cookclans.managers.StorageManager;
import me.itzdabbzz.cookclans.managers.TeleportManager;

/**
 * @author Phaed
 */
public class CookClans extends JavaPlugin {

    private ArrayList<String> messages = new ArrayList<>();
    private static CookClans instance;
    private static final Logger logger = Logger.getLogger("Minecraft");
    private ClanManager clanManager;
    private RequestManager requestManager;
    private StorageManager storageManager;
    private SettingsManager settingsManager;
    private PermissionsManager permissionsManager;
    private TeleportManager teleportManager;
    private LanguageManager languageManager;
    private ChatFormatMigration chatFormatMigration;
    private boolean hasUUID;

    /**
     * @return the logger
     */
    public static Logger getLog() {
        return logger;
    }

    /**
     * @param msg
     */
    public static void debug(String msg) {
        if (getInstance().getSettingsManager().isDebugging()) {
            logger.log(Level.INFO, msg);
        }
    }

    /**
     * @return the instance
     */
    public static CookClans getInstance() {
        return instance;
    }

    public static void log(String msg, Object... arg) {
        if (arg == null || arg.length == 0) {
            logger.log(Level.INFO, msg);
        } else {
            logger.log(Level.INFO, MessageFormat.format(msg, arg));
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        
        settingsManager = new SettingsManager();
        this.hasUUID = UUIDMigration.canReturnUUID();
        languageManager = new LanguageManager();

        permissionsManager = new PermissionsManager();
        requestManager = new RequestManager();
        clanManager = new ClanManager();
        storageManager = new StorageManager();
        teleportManager = new TeleportManager();
        chatFormatMigration = new ChatFormatMigration();
        
        chatFormatMigration.migrateAllyChat();
        chatFormatMigration.migrateClanChat();

        logger.info(MessageFormat.format(getLang("version.loaded"), getDescription().getName(), getDescription().getVersion()));

        getServer().getPluginManager().registerEvents(new SCEntityListener(), this);
        getServer().getPluginManager().registerEvents(new SCPlayerListener(), this);

        permissionsManager.loadPermissions();

        CommandHelper.registerCommand(getSettingsManager().getCommandClan());
        CommandHelper.registerCommand(getSettingsManager().getCommandAccept());
        CommandHelper.registerCommand(getSettingsManager().getCommandDeny());
        CommandHelper.registerCommand(getSettingsManager().getCommandMore());
        CommandHelper.registerCommand(getSettingsManager().getCommandAlly());
        CommandHelper.registerCommand(getSettingsManager().getCommandGlobal());

        getCommand(getSettingsManager().getCommandClan()).setExecutor(new ClanCommandExecutor());
        getCommand(getSettingsManager().getCommandAccept()).setExecutor(new AcceptCommandExecutor());
        getCommand(getSettingsManager().getCommandDeny()).setExecutor(new DenyCommandExecutor());
        getCommand(getSettingsManager().getCommandMore()).setExecutor(new MoreCommandExecutor());
        getCommand(getSettingsManager().getCommandAlly()).setExecutor(new AllyCommandExecutor());
        getCommand(getSettingsManager().getCommandGlobal()).setExecutor(new GlobalCommandExecutor());

        getCommand(getSettingsManager().getCommandClan()).setTabCompleter(new PlayerNameTabCompleter());
        logger.info("[CookClans] Online Mode: " + hasUUID); //TODO: Is this necessary?
        logger.info("[CookClans] Multithreading Mode : " + CookClans.getInstance().getSettingsManager().getUseThreads());
        logger.info("[CookClans] BungeeCord Mode: " + CookClans.getInstance().getSettingsManager().getUseBungeeCord());
        
        startTasks();
    }

    private void startTasks() {
        if (getSettingsManager().isMemberFee()) {
            new CollectFeeTask().start();
        }
        if (getSettingsManager().isClanUpkeep()) {
            new CollectUpkeepTask().start();
            new UpkeepWarningTask().start();
        }
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getStorageManager().closeConnection();
        getPermissionsManager().savePermissions();
    }

    /**
     * @return the clanManager
     */
    public ClanManager getClanManager() {
        return clanManager;
    }

    /**
     * @return the requestManager
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * @return the storageManager
     */
    public StorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * @return the permissionsManager
     */
    public PermissionsManager getPermissionsManager() {
        return permissionsManager;
    }

    /**
     * @param msg the path within the language file
     * @return the lang
     */
    public String getLang(String msg) {
        return languageManager.get(msg);
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public List<String> getMessages() {
        return messages;
    }

    /**
     * @return the hasUUID
     */
    @Deprecated
    public boolean hasUUID() {
        return this.hasUUID;
    }

    /**
     * @param trueOrFalse
     */
    public void setUUID(boolean trueOrFalse) {
        this.hasUUID = trueOrFalse;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
