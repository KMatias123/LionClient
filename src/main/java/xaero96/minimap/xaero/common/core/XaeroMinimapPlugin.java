/*    */ package xaero.common.core;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
/*    */ 
/*    */ @MCVersion("1.12.2")
/*    */ @TransformerExclusions({"xaero.common.core.transformer", "xaero.common.core"})
/*    */ public class XaeroMinimapPlugin
/*    */   implements IFMLLoadingPlugin {
/*    */   public String[] getASMTransformerClass() {
/* 13 */     return new String[] { "xaero.common.core.transformer.ChunkTransformer", "xaero.common.core.transformer.NetHandlerPlayClientTransformer", "xaero.common.core.transformer.EntityPlayerTransformer", "xaero.common.core.transformer.AbstractClientPlayerTransformer" };
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 18 */     return "xaero.common.core.CoreModContainer";
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


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\core\XaeroMinimapPlugin.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */