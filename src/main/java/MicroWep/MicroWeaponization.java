package MicroWep;


import MicroWep.common.proxy.ServerProxy;
import MicroWep.disease.DiseaseInit;
import MicroWep.disease.DiseaseManager;
import MicroWep.disease.infection_managers.EntityAttackInfectionManager;
import MicroWep.event.EntityAttackListener;
import MicroWep.lib.ConstantsLib;
import net.minecraftforge.common.MinecraftForge;
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

    public static DiseaseManager manager = new DiseaseManager();
    public static EntityAttackInfectionManager EAttackManager = new EntityAttackInfectionManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DiseaseInit.init(manager);
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EntityAttackListener());
        MinecraftForge.EVENT_BUS.register(new DiseaseManager());
    }


}
