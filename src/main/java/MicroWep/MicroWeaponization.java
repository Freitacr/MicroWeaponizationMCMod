package MicroWep;


import MicroWep.common.proxy.ServerProxy;
import MicroWep.lib.ConstantsLib;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ConstantsLib.MODID)
public class MicroWeaponization {

    @Mod.Instance(ConstantsLib.MODID)
    public static MicroWeaponization instance;

    @SidedProxy(serverSide = ConstantsLib.SERVERPROXY, clientSide = ConstantsLib.CLIENTPROXY)
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    public void postinit(FMLPostInitializationEvent event) {

    }


}
