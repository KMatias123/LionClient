/*    */ package xaero.map.core;
/*    */ 
/*    */ import com.google.common.eventbus.EventBus;
/*    */ import java.util.Arrays;
/*    */ import net.minecraftforge.fml.common.DummyModContainer;
/*    */ import net.minecraftforge.fml.common.LoadController;
/*    */ import net.minecraftforge.fml.common.ModMetadata;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CoreModContainer
/*    */   extends DummyModContainer
/*    */ {
/*    */   public CoreModContainer() {
/* 15 */     super(new ModMetadata());
/* 16 */     ModMetadata meta = getMetadata();
/* 17 */     meta.modId = "xaeroworldmap_core";
/* 18 */     meta.name = "XaeroWorldMapCore";
/* 19 */     meta.description = "Required by Xaero's World Map.";
/* 20 */     meta.version = "1.12.2-1.0";
/* 21 */     meta.authorList = Arrays.asList(new String[] { "Xaero" });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean registerBus(EventBus bus, LoadController controller) {
/* 26 */     bus.register(this);
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\core\CoreModContainer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */