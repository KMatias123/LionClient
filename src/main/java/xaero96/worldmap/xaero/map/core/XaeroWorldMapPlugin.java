/*    */ package xaero.map.core;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
/*    */ 
/*    */ @MCVersion("1.12.2")
/*    */ @TransformerExclusions({"xaero.map.core.transformer", "xaero.map.core"})
/*    */ public class XaeroWorldMapPlugin
/*    */   implements IFMLLoadingPlugin {
/*    */   public String[] getASMTransformerClass() {
/* 13 */     return new String[] { "xaero.map.core.transformer.ChunkTransformer", "xaero.map.core.transformer.NetHandlerPlayClientTransformer", "xaero.map.core.transformer.EntityPlayerTransformer", "xaero.map.core.transformer.AbstractClientPlayerTransformer" };
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 18 */     return "xaero.map.core.CoreModContainer";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 23 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> data) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\core\XaeroWorldMapPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */